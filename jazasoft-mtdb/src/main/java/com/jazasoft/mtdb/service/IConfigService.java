package com.jazasoft.mtdb.service;

import com.jazasoft.mtdb.dto.Config;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Difference between Configuration and Setting is:
 * Configuration can be of any type at any node in conf file.
 * Setting are of type "com.jazasoft.mtdb.dto.Config" under node "settings"
 * Created by mdzahidraza on 04/09/17.
 */
public interface IConfigService {
    /**
     * Read default configs from main config file
     * @return
     */
    Map<String, Object> readDefaultConfigs();

    /**
     * Read complete configuration from config file of the tenant
     * @param tenant
     * @return
     */
    Map<String, Object> readAllConfig(String tenant);

    /**
     * Read complete configuration of the tenant
     * @param tenant
     * @param properties
     * @return
     */
    boolean updateAllConfig(String tenant, Map<String, Object> properties);

    /**
     * Read specific Config.
     * @param tenant
     * @param key
     * @return
     */
    Object readConfig(String tenant, String key);

    /**
     * Update Specific config
     * @param tenant
     * @param key
     * @param value
     * @return
     */
    boolean updateConfig(String tenant, String key, Object value);

    /**
     * Insert new Config.
     * @param tenant
     * @param key
     * @param value
     * @return
     */
    boolean writeConfig(String tenant, String key, Object value);


    /**
     * Read complete Settings/Preferences of application of specific tenant.
     * @param tenant
     * @return
     */
    Collection<Config> readAllSettings(String tenant);

    /**
     * Read specific Setting
     * @param tenant
     * @param key
     * @return
     */
    Config readSetting(String tenant, String key);

    /**
     * Update Specific Setting.
     * @param tenant
     * @param key
     * @param config
     * @return
     */
    boolean updateSetting(String tenant, String key, Config config);

    /**
     * Add new Setting
     * @param tenant
     * @param key
     * @param config
     * @return
     */
    boolean writeSetting(String tenant, String key, Config config);

}
