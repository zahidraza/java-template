package com.jazasoft.mtdb.tenant;

import com.jazasoft.mtdb.IConfigKeys;
import com.jazasoft.mtdb.IConstants;
import com.jazasoft.mtdb.TenantCreatedEvent;
import com.jazasoft.mtdb.entity.Company;
import com.jazasoft.mtdb.repository.CompanyRepository;
import com.jazasoft.mtdb.service.IConfigService;
import com.jazasoft.mtdb.util.Utils;
import com.jazasoft.util.ProcessUtils;
import com.jazasoft.util.YamlUtils;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mdzahidraza on 26/06/17.
 */

@Component
@Scope( proxyMode = ScopedProxyMode.TARGET_CLASS )
@Transactional(value="masterTransactionManager", readOnly = true)
@Profile("default")
public class MultiTenantConnectionProviderImpl
        extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl
        implements IMultiTenantConnectionProvider, ApplicationListener<TenantCreatedEvent>, ResourceLoaderAware {

    private static final long serialVersionUID = 6246085840652870138L;

    private final static Logger logger = LoggerFactory.getLogger(MultiTenantConnectionProviderImpl.class);

    private Map<String, DataSource> map; // map holds the companyKey => DataSource

    private String defaultTenant;

    private ResourceLoader resourceLoader;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private IConfigService configurationService;

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.host}")
    private String host;
    @Value("${spring.datasource.port}")
    private String port;
    @Value("${spring.datasource.masterdb}")
    private String masterdb;
    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.platform}")
    private String platform;

    @Autowired
    private DataSource dataSource; // injected here to get properties and to provide default.

    @PostConstruct
    public void load() {
        map = new HashMap<>();
        init();
    }

    public void init() {
        for (Company company : companyRepository.findAll()) {
            // in this experiment we are just using one instance of mysql. URL is generated by replacing master database
            // name with company key to get new database URL
            try {
                addDatasource(company.getDbName());
                initDefaultConfiguration(company.getDbName());
            } catch (Exception e) {
                logger.error("Error in database URL {}", url, e);
            }
        }
    }

    @Override
    protected DataSource selectAnyDataSource() {
        logger.debug("######### Selecting any data source");
        return dataSource;
    }

    @Override
    public DataSource selectDataSource(String tenantIdentifier) {
        DataSource result;
        if (defaultTenant != null && map.containsKey(defaultTenant)) {
            result = map.get(defaultTenant);
            //defaultTenant = null;
            return result;
        } else {
            result = map.containsKey(tenantIdentifier) ? map.get(tenantIdentifier) : dataSource;
        }
        return result;
    }

    @Override
    public void onApplicationEvent(TenantCreatedEvent tenantCreatedEvent) {
        String dbName = tenantCreatedEvent.getDbName();
        addDatasource(dbName);
        initDefaultConfiguration(dbName);
    }

    @Override
    public void setTenantIdentifier(String tenantIdentifier) {
        logger.debug("setTenantIdentifier: tenant = {}", tenantIdentifier);
        this.defaultTenant = tenantIdentifier;
    }

    private void addDatasource(String tenantIdentifier) {
        logger.debug("addDatasource");
        DataSource dataSource = getDatasource(tenantIdentifier);
        map.put(tenantIdentifier, dataSource);
        initDb(tenantIdentifier);
        SpringLiquibase liquibase = getLiquibase(dataSource);
        try {
            liquibase.afterPropertiesSet();
        } catch (LiquibaseException e) {
            logger.error("Unable to perform liquibase migration. error: {}", e.getMessage());
        }
    }

    private SpringLiquibase getLiquibase(DataSource dataSource) {

        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        String changeLog = null;
        try {
            changeLog = (String) Utils.getConfProperty(IConfigKeys.LIQUIBASE_TENANT_CHANGELOG);
        } catch (IOException e) {
            logger.info("Liquibase Tenant changeLog filename not provided in Config file.");
        }
        if (changeLog == null) {
            logger.info("Using default value for tenant changelog file = 'classpath:/db/schema-tenant.xml'");
            changeLog = "classpath:/db/schema-tenant.xml";
        }
        liquibase.setChangeLog(changeLog);
        liquibase.setResourceLoader(resourceLoader);
        return liquibase;
    }

    private DataSource getDatasource(String tenantId) {
        String newUrl = url.replace(Utils.databaseNameFromJdbcUrl(url), tenantId);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(newUrl);
        dataSource.setUsername(user);
        dataSource.setPassword(password);
        return dataSource;
    }


    private void initDb(String tenant) {
        logger.info("initDb");
        String script = null;
        try {
            script = (String) Utils.getConfProperty(IConstants.DB_INIT_SCRIPT_FILENAME_KEY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String schemaFile = null;
        if (platform.equalsIgnoreCase("mysql")) {
            schemaFile = "schema-mysql.sql";
        }else if (platform.equalsIgnoreCase("postgresql")) {
            schemaFile = "schema-postgresql.sql";
        }
        if (script == null || schemaFile == null) {
            logger.error("Database|Schema initialization file not specified.");
            return;
        }
        schemaFile = Utils.getAppHome() + File.separator + "conf" + File.separator + schemaFile;
        File dir = new File(Utils.getAppHome() + File.separator + "bin");
        logger.info("Executing: {} {} {} {}", script, platform, tenant, schemaFile);


        try {
            Process process = ProcessUtils.createProcess(dir, "/bin/bash", script, platform, tenant, schemaFile, user, password, host, port, masterdb);
            Map<String, Object> result = ProcessUtils.execute(process);
            if ((Integer)result.get(ProcessUtils.EXIT_CODE) == 0) {
                logger.info("Database initialized successfully for tenant = {}", tenant);
            } else {
                logger.info("Database initialization failed for tenant = {} with exitCode = {}", tenant,result.get(ProcessUtils.EXIT_CODE));
                logger.error("console Output = {}", result.get(ProcessUtils.CONSOLE_OUTPUT));
                logger.error("console Error = {}", result.get(ProcessUtils.CONSOLE_ERROR));

            }
        } catch (IOException e) {
            logger.error("Error creating process. error - [{}]", e.getMessage());
        }

    }

    private void initDefaultConfiguration(String tenant) {
        logger.debug("initDefaultConfiguration");
        String filename = Utils.getAppHome() + File.separator + "conf" + File.separator + tenant + ".yml";
        File file = new File(filename);
        if (file.exists()) return;
        logger.info("Initializing default configuration. config file = {}", filename);
        try {
            YamlUtils.getInstance().writeProperties(file, configurationService.readDefaultConfigs());
        } catch (IOException e) {
            logger.error("Error occured initializing Default configuration. {}", e.getMessage());
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}


