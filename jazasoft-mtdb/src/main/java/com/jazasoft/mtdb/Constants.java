package com.jazasoft.mtdb;

/**
 * Created by mdzahidraza on 26/06/17.
 */
public class Constants {
    public static final String CURRENT_TENANT_IDENTIFIER = "CURRENT_TENANT_IDENTIFIER"; // tenant data base name for datasource selection
    public static final String CURRENT_TENANT = "CURRENT_TENANT"; // Current Tenant (Company) Object, for Admin privileged work
    public static final String CURRENT_USER = "CURRENT_USER";   // Current User Id.
    public static final String CURRENT_TENANT_ID = "CURRENT_TENANT_ID"; // Current Tenant (Company) Id

    public static final String TENANT_MASTER = "tnt_db_master";
    public static final String UNKNOWN_TENANT = "tnt_db_default";
    public static final String DB_INIT_SCRIPT_FILENAME_KEY = "tenant.database.init-script-filename";

    public static final String ROLE_MASTER = "ROLE_MASTER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
}
