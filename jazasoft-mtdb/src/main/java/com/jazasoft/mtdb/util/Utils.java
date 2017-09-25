package com.jazasoft.mtdb.util;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.jazasoft.mtdb.dto.Permission;
import com.jazasoft.mtdb.entity.UrlInterceptor;
import com.jazasoft.mtdb.service.IResourceService;
import com.jazasoft.util.YamlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utils.
 *
 * @author mdzahidraza
 */
public class Utils {
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    private static String appFile = "application.yml";
    private static Map appRoot;

    static {
        InputStream is = null;
        try {
            is = YamlUtils.class.getClassLoader().getResourceAsStream(appFile);
            YamlReader readerAppProps = new YamlReader(new InputStreamReader(is));
            appRoot = (Map) readerAppProps.read();
        } catch (YamlException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String confFile = getAppHome() + File.separator + "conf" + File.separator + "config.yml";

    public static String getAppHome() {
        String result = null;
        String aapHome = null;
        try {
            aapHome = (String) getAppProperty("app.home");
            result =  System.getenv(aapHome);
        } catch (IOException e) {
            LOGGER.error("Define APP HOME environment variable name in application.yml");
        }
        if (result == null) {
            throw new RuntimeException("APP HOME Environment variable not defined.");
        }
        return result;
    }

    /**
     * Read particular property of application.yaml file
     *
     * @param key
     * @return Object which can either String or Map or List, null if not found
     * @throws IOException
     */
    public static Object getAppProperty(String key) throws IOException {
        return YamlUtils.getInstance().getNestedProperty(appRoot, key);
    }

    /**
     * Read particular property of config.yaml file
     *
     * @param key
     * @return Object which can either String or Map or List, null if not found
     * @throws IOException
     */
    public static Object getConfProperty(String key) throws IOException {
        YamlReader reader = new YamlReader(new FileReader(confFile));
        return YamlUtils.getInstance().getNestedProperty((Map) reader.read(), key);
    }

    /**
     * Read particular config property for specific tenant
     * @param tenant
     * @param key
     * @return
     * @throws IOException
     */
    public static Object getConfProperty(String tenant, String key) throws IOException {
        String file = getAppHome() + File.separator + "conf" + File.separator + tenant + ".yml";
        YamlReader reader = new YamlReader(new FileReader(file));
        return YamlUtils.getInstance().getNestedProperty((Map) reader.read(), key);
    }

    public static Object getAllConfigurations(String tenant) throws IOException {
        String file = getAppHome() + File.separator + "conf" + File.separator + tenant + ".yml";
        return YamlUtils.getInstance().getProperty(new File(file));
    }

    public static String databaseNameFromJdbcUrl(String url) {
        try {
            URI uri = new URI(url.replace("jdbc:", ""));
            return uri.getPath().substring(1);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get List From comma separated String value
     * @param csv
     * @return
     */
    public static List<String> getListFromCsv(String csv) {
        List<String> result = new ArrayList<>();
        String[] values = csv.split(",");
        for (String value : values) {
            if (value.trim().length() != 0) {
                result.add(value.trim());
            }
        }
        return result;
    }

    public static String getCsvFromIterable(Iterable<String> iterable) {
        StringBuilder builder = new StringBuilder();
        iterable.forEach(itr -> builder.append(itr).append(","));
        if (builder.length() > 0)
            builder.setLength(builder.length()-1);
        return builder.toString();
    }

    public static List<String> getRoleList(String roles) {
        List<String> result = new ArrayList<>();
        String[] rls = roles.split(",");
        for (String r : rls) {
            if (r.trim().length() != 0) {
                result.add(r.trim());
            }
        }
        return result;
    }

    public static String getScope(String method) {
        method = method.toUpperCase();
        String scope = "";
        switch (method) {
            case "GET":
                scope = "read";
                break;
            case "POST":
                scope = "write";
                break;
            case "PUT":
            case "PATCH":
                scope = "update";
                break;
            case "DELETE":
                scope = "delete";
                break;
        }
        return scope;
    }

    /**
     * Get permission with [read,full] scopes only
     * @param interceptors
     * @return
     */
    public static Set<Permission> getPermissions(List<UrlInterceptor> interceptors) {
        IResourceService resourceService = ApplicationContextUtil.getApplicationContext().getBean(IResourceService.class);
        Set<Permission> permissions = new HashSet<>();
        Map<String, Set<String>> map = new HashMap<>();
        interceptors.forEach(interceptor -> {
            String res = resourceService.getResource(interceptor.getUrl());
            String scope = Utils.getScope(interceptor.getHttpMethod());
            Set<String> scopes = map.get(res);
            if (scopes == null) scopes = new HashSet<>();
            if (scope.equalsIgnoreCase("read")) {
                scopes.add(scope);
            }else {
                scopes.add("full");
            }
            map.put(res, scopes);
        });

        map.forEach((key, value) -> {
            permissions.add(new Permission(key, Utils.getListFromCsv(getCsvFromIterable(value)).stream().collect(Collectors.toSet())));
        });
        //LOGGER.info("perm szie = {}", permissions.size());
        return permissions;
    }
    /**
     * Get permission with [read,write,update,delete] scopes
     * @param interceptors
     * @return
     */
    public static Set<Permission> getPermissionsAllScope(List<UrlInterceptor> interceptors) {
        IResourceService resourceService = ApplicationContextUtil.getApplicationContext().getBean(IResourceService.class);
        Set<Permission> permissions = new HashSet<>();
        Map<String, String> map = new HashMap<>();
        interceptors.forEach(interceptor -> {
            String res = resourceService.getResource(interceptor.getUrl());

            String scope = map.get(res);
            if (scope == null) {
                map.put(res, Utils.getScope(interceptor.getHttpMethod()));
            }else {
                scope = scope + "," + Utils.getScope(interceptor.getHttpMethod());
                map.put(res, scope);
            }
        });

        map.forEach((key, value) -> {
            permissions.add(new Permission(key, Utils.getListFromCsv(value).stream().collect(Collectors.toSet())));
        });
        return permissions;
    }
}
