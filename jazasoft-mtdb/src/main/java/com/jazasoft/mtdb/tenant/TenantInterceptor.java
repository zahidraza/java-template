package com.jazasoft.mtdb.tenant;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jazasoft.mtdb.ApiUrls;
import com.jazasoft.mtdb.Constants;
import com.jazasoft.mtdb.dto.RestError;
import com.jazasoft.mtdb.entity.User;
import com.jazasoft.mtdb.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Here, Decision about tenant is made
 *
 * General case:
 * For Master role, data should be served from master datasource
 * For Other roles, data should be served from tenant specific datasource
 *
 * Special Case:
 * Admin role user may want access to master datasource if resources are: User or Role or UrlInterceptor
 * Other user may want access to master datasource if resource is: user profile
 *
 * Set Tenant Identifier. CurrentTenantIdentifierResolver extracts this Tenant Identifier and returns.
 * Then MultiTenantConnectionProvider uses this identifier to provide appropriate datasource
 * Created by mdzahidraza on 26/06/17.
 */
@Component
public class TenantInterceptor extends HandlerInterceptorAdapter {
    private final Logger LOGGER = LoggerFactory.getLogger(TenantInterceptor.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired ObjectMapper mapper;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler)
            throws Exception {
        Principal principal = req.getUserPrincipal();
        boolean tenantSet = true;
        if (principal != null) {
            User user = null;
            Optional<User> uo = userRepository.findOneByEmail(principal.getName());
            if (uo.isPresent()){
                user = uo.get();
            }else {
                uo = userRepository.findOneByUsername(principal.getName());
                if (uo.isPresent()) {
                    user = uo.get();
                }else {
                    return false;
                }
            }

            String url = req.getRequestURI();
            List<String> roles = user.getRoleList().stream().map(role -> role.getName()).collect(Collectors.toList());
            if (roles.contains(Constants.ROLE_MASTER)) {
                if (roles.size() == 1) {
                    req.setAttribute(Constants.CURRENT_TENANT_IDENTIFIER, Constants.TENANT_MASTER);
                }else {
                    LOGGER.warn("ROLE_MASTER cannot have any other roles. possible security breach.");
                    sendResponse(res,403,40301,"Forbidden","","");
                    tenantSet = false;
                }
            }else {
                if (user.getCompany() == null) {
                    LOGGER.warn("User is not MASTER and does not belong to any company. UserId = {}", user.getId());
                    sendResponse(res,403,40301,"Forbidden","","");
                    tenantSet =  false;
                }else {
                    if (roles.contains(Constants.ROLE_ADMIN)) {
                        if (url.contains(ApiUrls.ROOT_URL_USERS) || url.contains(ApiUrls.ROOT_URL_ROLES) || url.contains(ApiUrls.ROOT_URL_INTERCEPTORS)) {
                            req.setAttribute(Constants.CURRENT_TENANT_IDENTIFIER, Constants.TENANT_MASTER);
                            req.setAttribute(Constants.CURRENT_TENANT, user.getCompany());
                        }else {
                            req.setAttribute(Constants.CURRENT_TENANT_IDENTIFIER, user.getCompany().getDbName());
                        }
                    }
                    //Other Users can access master only for profile
                    else {
                        if(url.contains(ApiUrls.URL_USERS_USER_SEARCH_BY_EMAIL)
                                || url.contains(ApiUrls.URL_USERS_USER_SEARCH_BY_USERNAME)
                                || url.contains(ApiUrls.URL_USERS_USER_PROFILE)) {

                            req.setAttribute(Constants.CURRENT_TENANT_IDENTIFIER, Constants.TENANT_MASTER);
                        }else {
                            req.setAttribute(Constants.CURRENT_TENANT_IDENTIFIER, user.getCompany().getDbName());
                        }
                    }
                    req.setAttribute(Constants.CURRENT_USER, user.getId());
                    req.setAttribute(Constants.CURRENT_TENANT_ID, user.getCompany().getId());
                }
            }
        }
        return tenantSet;
    }

    @Override
    public void postHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
    }

    private void sendResponse(HttpServletResponse res, int status,int code, String message, String devMessage, String moreInfo) throws Exception{
        RestError error = new RestError(status,code,message,devMessage,moreInfo);
        String response = mapper.writeValueAsString(error);
        res.setStatus(status);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.getWriter().write(response);
        res.getWriter().flush();
    }
}
