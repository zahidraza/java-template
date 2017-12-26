package com.jazasoft.mtdb;

/**
 * Created by mdzahidraza on 26/06/17.
 */
public interface IApiUrls {

    public static final String OAUTH_URL = "/oauth/token";

    public static final String ROOT_URL_LICENSE = "/api/license";
    public static final String URL_LICENSE_CHECK = "/check";
    public static final String URL_LICENSE_ACTIVATE = "/activate";

    public static final String ROOT_URL_USERS = "/api/users";
    public static final String URL_USERS_USER = "{userId}";
    public static final String URL_USERS_USER_SEARCH_BY_USERNAME = "/search/byUsername";
    public static final String URL_USERS_USER_SEARCH_BY_EMAIL = "/search/byEmail";
    public static final String URL_USERS_PROFILE = "/profile";
    public static final String URL_USERS_USER_CHANGE_PASSWORD = "{userId}/changePassword";
    public static final String URL_USERS_FORGOT_PASSWORD = "/forgotPassword";

    /* Tenant Specific Resource*/
    public static final String ROOT_URL_TUSERS = "/api/tUsers";
    public static final String URL_TUSERS_PRIVILEGE = "/privilege";
    public static final String URL_TUSERS_TUSER = "/{tUserId}";

    public static final String ROOT_URL_ROLES = "/api/roles";
    public static final String URL_ROLES_ROLE = "{roleId}";
    public static final String ROOT_URL_COMPANIES = "/api/companies";
    public static final String URL_COMPANIES_COMPANY = "{companyId}";

    public static final String ROOT_URL_INTERCEPTORS = "/api/interceptors";
    public static final String URL_INTERCEPTORS_INTERCEPTOR = "{interceptorId}";

    public static final String ROOT_URL_INIT = "/api/init";
    public static final String URL_INIT_MASTER = "/master";
    public static final String URL_INIT_TENANT = "/tenant";

    public static final String ROOT_URL_REPORTS = "/api/reports";
    public static final String URL_REPORTS_COMMON = "/common";

}
