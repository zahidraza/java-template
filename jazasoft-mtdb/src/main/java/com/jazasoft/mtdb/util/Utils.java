package com.jazasoft.mtdb.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utils.
 *
 * @author mdzahidraza
 */
public class Utils {

    public static String getAppHome() {
        return System.getenv("TEMPLATE_DB_HOME");
    }

    public static String databaseNameFromJdbcUrl(String url) {
        try {
            URI uri = new URI(url.replace("jdbc:", ""));
            return uri.getPath().substring(1);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> getRoleList(String roles) {
        List<String> result = new ArrayList<>();
        String[] rls = roles.split(",");
        for (String r: rls) {
            if (r.trim().length() != 0) {
                result.add(r.trim());
            }
        }
        return result;
    }
}
