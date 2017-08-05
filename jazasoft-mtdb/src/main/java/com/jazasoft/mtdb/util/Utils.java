package com.jazasoft.mtdb.util;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.jazasoft.util.YamlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private static String confFile = getAppHome() + File.separator + "conf" + File.separator + "config.yaml";

    public static String getAppHome() {
        String aapHome = null;
        try {
            aapHome = (String) getAppProperty("app.home");
            return System.getenv(aapHome);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Define APP HOME environment variable name in application.yml");
        }
        return null;
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
}
