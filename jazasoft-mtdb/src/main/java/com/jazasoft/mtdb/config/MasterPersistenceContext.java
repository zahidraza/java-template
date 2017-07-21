package com.jazasoft.mtdb.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Configuration
@EnableConfigurationProperties(JpaProperties.class)
@EnableJpaRepositories(
        entityManagerFactoryRef = "masterEntityManager",
        transactionManagerRef = "masterTransactionManager",
        basePackages = "com.jazasoft.mtdb.repository",
        repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class
)
@EnableTransactionManagement
public class MasterPersistenceContext {
    private final Logger LOGGER = LoggerFactory.getLogger(MasterPersistenceContext.class);

    @Autowired
    private JpaProperties jpaProperties;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource(){
        LOGGER.debug("-$$$- datasource()");
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Autowired
    @Bean(name = "masterEntityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LOGGER.debug("-$$$- masterEntityManager()");
        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName("master");
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.jazasoft.mtdb.entity");
        em.setJpaVendorAdapter(jpaVendorAdapter);
        em.setJpaProperties(additionalJpaProperties(dataSource));
        return em;
    }

    @Primary
    @Bean(name = "masterTransactionManager")
    public JpaTransactionManager transactionManager(@Qualifier("masterEntityManager") EntityManagerFactory masterEntityManager){
        LOGGER.debug("-$$$- masterTransactionManager()");
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(masterEntityManager);
        return transactionManager;
    }

    @Autowired
    private Properties additionalJpaProperties(DataSource dataSource) {
        Properties properties = new Properties();
        for (Map.Entry<String, String> entry : jpaProperties.getHibernateProperties(dataSource).entrySet()) {
            properties.setProperty(entry.getKey(), entry.getValue());
        }
        return properties;
    }
}
