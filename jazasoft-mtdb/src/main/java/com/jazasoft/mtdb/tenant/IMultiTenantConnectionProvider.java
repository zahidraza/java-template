package com.jazasoft.mtdb.tenant;

/**
 * Created by mdzahidraza on 21/10/17.
 */
public interface IMultiTenantConnectionProvider {
    void setTenantIdentifier(String tenantIdentifier);
}
