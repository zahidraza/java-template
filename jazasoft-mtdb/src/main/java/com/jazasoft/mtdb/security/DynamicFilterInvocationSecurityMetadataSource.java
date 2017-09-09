package com.jazasoft.mtdb.security;

import com.jazasoft.mtdb.ApiUrls;
import com.jazasoft.mtdb.Constants;
import com.jazasoft.mtdb.entity.Role;
import com.jazasoft.mtdb.entity.UrlInterceptor;
import com.jazasoft.mtdb.entity.User;
import com.jazasoft.mtdb.service.InterceptorService;
import com.jazasoft.mtdb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Here, Decision is made about which roles are authorized for a particular resource
 * Created by mdzahidraza on 28/06/17.
 */
public class DynamicFilterInvocationSecurityMetadataSource extends DefaultFilterInvocationSecurityMetadataSource {

    private final Logger LOGGER = LoggerFactory.getLogger(DynamicFilterInvocationSecurityMetadataSource.class);

    private InterceptorService interceptorService;

    private UserService userService;

    public DynamicFilterInvocationSecurityMetadataSource(LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> requestMap) {
        super(requestMap);
    }

    public void setInterceptorService(InterceptorService urlInterceptorRepository) {
        this.interceptorService = urlInterceptorRepository;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();
        String httpMethod = fi.getRequest().getMethod();

        //Fully public resources
        if (url.equals("/") || url.startsWith("/static/") || url.contains(ApiUrls.URL_USERS_FORGOT_PASSWORD)) {
            return new ArrayList<>();
        }
        Principal principal = fi.getHttpRequest().getUserPrincipal();
        if (principal != null) {
            User user = userService.findByUsername(principal.getName());
            if (user == null){
                user = userService.findByEmail(principal.getName());
                if (user == null) {
                    //TODO: handle if user does not exists
                    return getForbiddenAttribute();
                }
            }

            if (url != null) {
                url = getUrl(url);
                LOGGER.debug("Request for Url = {}, method = {}", url, httpMethod);
                List<String> roles = user.getRoleList().stream().map(Role::getName).collect(Collectors.toList());

                //More Specific Url case at top
                //Only Master is authorized to acces Company Resource
                if (url.contains(ApiUrls.ROOT_URL_COMPANIES)) {
                    if (roles.contains(Constants.ROLE_MASTER)) {
                        if (roles.size() == 1){
                            return getMasterAttribute();
                        }else {
                            LOGGER.warn("MASTER cannot have other roles. possible security breach. UserId = {}", user.getId());
                            return getForbiddenAttribute();
                        }
                    }else {
                        return getForbiddenAttribute();
                    }
                }
                // Every one is allowed access profile|other  resource
                // Public but for authenticated users
                else if(url.contains(ApiUrls.URL_USERS_USER_SEARCH_BY_EMAIL)
                        || url.contains(ApiUrls.URL_USERS_USER_SEARCH_BY_USERNAME)
                        || url.contains(ApiUrls.URL_USERS_PROFILE)
                        || url.contains("/users/{\\d+}/changePassword")
                        || url.contains(ApiUrls.ROOT_URL_INIT)
                        || url.contains(ApiUrls.ROOT_URL_TUSERS + ApiUrls.URL_TUSERS_PRIVILEGE)) {
                    logger.debug("Authenticated Public resource.");
                    return new ArrayList<>();
                }
                //Only Master and Admin are authorized for User,Role,UrlInterceptor Resource
                else if (url.contains(ApiUrls.ROOT_URL_USERS) || url.contains(ApiUrls.ROOT_URL_ROLES) || url.contains(ApiUrls.ROOT_URL_INTERCEPTORS)){
                    Collection<ConfigAttribute> attributes = getMasterAttribute();
                    attributes.add(new DynamicConfigAttribute(Constants.ROLE_ADMIN));
                    return attributes;
                }
                // Tenant specific Resource. dynamic role management. Admin will have access to all resources
                else {
                    if (user.getCompany() == null) {
                        //User in neither Master nor belong to any company. Forbidden to access any resource
                        return getForbiddenAttribute();
                    }
                    List<UrlInterceptor> interceptors = this.interceptorService.findAllByCompanyAndUrl(user.getCompany(), url);

                    Collection<ConfigAttribute> configAttributes = interceptors.stream()
                            //If the httpMethod is null is because it is valid for all methods
                            .filter(in -> in.getHttpMethod() == null || in.getHttpMethod().equals(httpMethod))
                            .map(in -> new DynamicConfigAttribute(in.getAccess()))
                            .collect(Collectors.toList());
                    configAttributes.add(new DynamicConfigAttribute(Constants.ROLE_ADMIN));
                    configAttributes.forEach(configAttribute -> LOGGER.debug("Authorized user Roles: " + configAttribute.getAttribute()));
                    return configAttributes;
                }
            }
        }
        return getForbiddenAttribute();
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }


    /**
     * {@link ConfigAttribute} with specific attribute (access rule).
     * Possible values to getAttribute's return:
     *  - IS_AUTHENTICATED_ANONYMOUSLY - No token in the request
     *  - IS_AUTHENTICATED_REMEMBERED
     *  - IS_AUTHENTICATED_FULLY - With a valid token
     *  - SCOPE_<scope> - Token with a specific scope
     *  - ROLE_<role> - Token's user with specific role
     * @author mariane.vieira
     *
     */
    public class DynamicConfigAttribute implements ConfigAttribute {
        private static final long serialVersionUID = 1201502296417220314L;
        private String attribute;
        public DynamicConfigAttribute(String attribute) {
            this.attribute = attribute;
        }
        @Override
        public String getAttribute() {
            /* Possible values to getAttribute's return:
             * IS_AUTHENTICATED_ANONYMOUSLY, IS_AUTHENTICATED_REMEMBERED
             * IS_AUTHENTICATED_FULLY, SCOPE_<scope>, ROLE_<role>
             */
            return this.attribute;
        }
        @Override
        public String toString() {
            return this.attribute;
        }
    }

    private String getUrl(String url) {
        //System.out.println("Actual url: " + url);
        url = url.split("\\?")[0];
        String[] urls = url.split("/");
        Pattern pattern = Pattern.compile("\\d+");
        for (int i = 0; i < urls.length; i++) {
            if (pattern.matcher(urls[i]).matches()) {
                urls[i] = "{\\d+}";
            }
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < urls.length ; i++) {
            builder.append("/").append(urls[i]);
        }
        return builder.toString();
    }

    private Collection<ConfigAttribute> getForbiddenAttribute() {
        Collection<ConfigAttribute> attributes = new ArrayList<>();
        attributes.add(new DynamicConfigAttribute("ROLE_FORBIDDEN"));
        return attributes;
    }

    private Collection<ConfigAttribute> getMasterAttribute() {
        Collection<ConfigAttribute> attributes = new ArrayList<>();
        attributes.add(new DynamicConfigAttribute(Constants.ROLE_MASTER));
        return attributes;
    }
    private Collection<ConfigAttribute> getAdminAttribute() {
        Collection<ConfigAttribute> attributes = new ArrayList<>();
        attributes.add(new DynamicConfigAttribute(Constants.ROLE_ADMIN));
        return attributes;
    }
}

/*
* 1. Protect company resource for MASTER Only.
* 2. Protect Role, UrlInterceptor, User resource for MASTER and ADMIN only.
*
* */