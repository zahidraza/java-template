package com.jazasoft.mtdb;

/**
 * Created by mdzahidraza on 26/06/17.
 */
public class ApiUrls {

    public static final String OAUTH_URL = "/oauth/token";

    public static final String ROOT_URL_USERS = "/api/users";
    public static final String URL_USERS_USER = "{userId}";
    public static final String URL_USERS_USER_SEARCH_BY_USERNAME = "/search/byUsername";
    public static final String URL_USERS_USER_SEARCH_BY_EMAIL = "/search/byEmail";
    public static final String URL_USERS_USER_PROFILE = "/profile";

    public static final String ROOT_URL_ROLES = "/api/roles";
    public static final String URL_ROLES_ROLE = "{roleId}";
    public static final String ROOT_URL_COMPANIES = "/api/companies";
    public static final String URL_COMPANIES_COMPANY = "{companyId}";
    public static final String ROOT_URL_PRODUCTS = "/api/products";
    public static final String URL_PRODUCTS_PRODUCT = "{productId}";
    public static final String ROOT_URL_INTERCEPTORS = "/api/interceptors";
    public static final String URL_INTERCEPTORS_INTERCEPTOR = "{interceptorId}";



}
