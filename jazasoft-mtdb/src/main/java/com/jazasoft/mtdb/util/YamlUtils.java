package com.jazasoft.mtdb.util;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

import java.io.*;
import java.util.Map;

/**
 * Created by mdzahidraza on 02/07/17.
 */
public class YamlUtils {

    private static YamlUtils INSTANCE;
    private String appFile = "application.yaml";
    private String confFile = Utils.getAppHome() + File.separator + "conf" + File.separator + "config.yaml";
    Map appRoot;

    private YamlUtils() {
        InputStream is = null;
        try {
            is = YamlUtils.class.getClassLoader().getResourceAsStream(appFile);
            YamlReader readerAppProps = new YamlReader(new InputStreamReader(is));
            appRoot = (Map)readerAppProps.read();
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

    public static YamlUtils getInstance() {
        if (INSTANCE == null)
            INSTANCE = new YamlUtils();
        return INSTANCE;
    }

    /**
     * Read specific key from particular file
     * @param file File to be read
     * @param key key to be read. It can be dot seprated for nested key
     * @return  Object which can either String or Map or List, null if not found
     * @throws IOException
     */
    public Object getProperty(File file, String key) throws IOException{
        YamlReader reader = new YamlReader(new FileReader(file));
        return getNestedProperty((Map)reader.read(),key);
    }

    /**
     * Read specific key from particular Input Stream
     * @param is Input Stream to be read
     * @param key key to be read. It can be dot seprated for nested key
     * @return  Object which can either String or Map or List, null if not found
     * @throws IOException
     */
    public Object getProperty(InputStream is, String key) throws IOException{
        YamlReader reader = new YamlReader(new InputStreamReader(is));
        return getNestedProperty((Map)reader.read(),key);
    }

    /**
     *  Read particular property of application.yaml file
     * @param key
     * @return Object which can either String or Map or List, null if not found
     * @throws IOException
     */
    public Object getAppProperty(String key) throws IOException{
        return getNestedProperty(appRoot,key);
    }

    /**
     *  Read particular property of config.yaml file
     * @param key
     * @return Object which can either String or Map or List, null if not found
     * @throws IOException
     */
    public Object getConfProperty(String key) throws IOException{
        YamlReader reader = new YamlReader(new FileReader(confFile));
        return getNestedProperty((Map)reader.read(),key);
    }

    public Object getNestedProperty(Map root, String key) {
        String[] keys = key.split("\\.");
        for (int i = 0; i < keys.length-1; i++) {
            root = (Map) root.get(keys[i]);
        }
        return root.get(keys[keys.length-1]);
    }
}
