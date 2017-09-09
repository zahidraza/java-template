package com.jazasoft.util;

import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;

import java.io.*;
import java.util.Map;

/**
 * Created by mdzahidraza on 02/07/17.
 */
public class YamlUtils {

    private static YamlUtils INSTANCE;

    private YamlUtils() {

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
     * Read all properties from a file
     * @param file
     * @return
     * @throws IOException
     */
    public Object getProperty(File file) throws IOException{
        YamlReader reader = new YamlReader(new FileReader(file));
        return reader.read();
    }

    public Object getProperty(String filename) throws IOException{
        return getProperty(new File(filename));
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


    public Object getNestedProperty(Map root, String key) {
        String[] keys = key.split("\\.");
        for (int i = 0; i < keys.length-1; i++) {
            root = (Map) root.get(keys[i]);
        }
        return root.get(keys[keys.length-1]);
    }

    public void writeProperties(File file, Map<String, Object> properties) throws IOException {
        YamlWriter yamlWriter = new YamlWriter(new FileWriter(file));
        yamlWriter.write(properties);
        yamlWriter.close();
    }

    public void writeProperties(String filename, Map<String, Object> properties) throws IOException {
        writeProperties(new File(filename), properties);
    }
}
