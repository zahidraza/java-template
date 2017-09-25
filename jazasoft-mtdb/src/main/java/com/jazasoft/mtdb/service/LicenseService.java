package com.jazasoft.mtdb.service;

import com.jazasoft.mtdb.dto.License;

import java.util.List;

/**
 * Created by mdzahidraza on 23/09/17.
 */
public interface LicenseService {

    /**
     * If username, produce code and key are available, keep checking license daily.
     * Scheduler shoul use thid method for polling license information
     */
    void checkLicense(List<String> tenants);

    /**
     * Activate License for a tenant using username, prduct code and key
     * @param tenant
     * @param username
     * @param productCode
     * @param ProductKey
     */
    License activateLicense(String tenant, String username, String productCode, String ProductKey);

    /**
     * Get License for a tenant
     * @return
     */
    License getLicense(String tenant);

    boolean isLicensed(String tenant);
}
