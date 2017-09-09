package com.jazasoft.mtdb.service;

import java.util.Map;

/**
 * Created by mdzahidraza on 04/09/17.
 */
public interface IConfigurationService {

    Map<String, Object> getDefaultConfiguration();

    Map<String, Object> getAllConfiguration(String tenant);

    Map<String, Object> updateConfiguartion(String tenant, String key, Object value);

    Map<String, Object> updateConfiguartion(String tenant, Map<String, Object> properties);
}
