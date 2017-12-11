package com.jazasoft.mtdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jazasoft.mtdb.entity.User;
import com.jazasoft.mtdb.service.ILicenseService;
import com.jazasoft.mtdb.util.ApplicationContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mdzahidraza on 23/09/17.
 */
public class LicenseCheckFilter extends GenericFilterBean {
    private final Logger logger = LoggerFactory.getLogger(LicenseCheckFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        logger.trace("Inside LicenseCheckFilter");

        HttpServletResponse resp = (HttpServletResponse)response;
        HttpServletRequest req = (HttpServletRequest) request;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {

            ILicenseService ILicenseService = ApplicationContextUtil.getApplicationContext().getBean(ILicenseService.class);
            User user = (User) authentication.getPrincipal();

            if (user.getTenant() != null && !user.getTenant().equalsIgnoreCase(Constants.TENANT_MASTER)) {
                boolean isAdmin = user.getAuthorities().stream().filter(o -> o.getAuthority().equalsIgnoreCase(Constants.ROLE_ADMIN)).count() == 1;
                if (isAdmin) {
                    if (req.getRequestURI().contains(ApiUrls.ROOT_URL_LICENSE + ApiUrls.URL_LICENSE_ACTIVATE)
                            || req.getRequestURI().contains(ApiUrls.ROOT_URL_LICENSE + ApiUrls.URL_LICENSE_CHECK)
                            || req.getRequestURI().contains(ApiUrls.URL_USERS_PROFILE)
                            ) {
                        filterChain.doFilter(request,response);
                    } else {
                        //If Product license not activated
                        if (ILicenseService.getLicense(user.getTenant()) == null) {
                            Map<String, Object> body = new HashMap<>();
                            body.put("status", "PRODUCT_LICENSE_NOT_ACTIVATED");
                            body.put("message", "Product License not activated. Activate Product License first.");
                            writeResponse(resp, body);
                        } else {
                            //If product license expired
                            if (!ILicenseService.isLicensed(user.getTenant())) {
                                Map<String, Object> body = new HashMap<>();
                                body.put("status", "PRODUCT_LICENSE_EXPIRED");
                                body.put("message", "Product License expired. Renew Product License first.");
                                writeResponse(resp,body);
                            }else {
                                filterChain.doFilter(request,response);
                            }
                        }
                    }

                }else {
                    //If Product license not activated
                    if (ILicenseService.getLicense(user.getTenant()) == null) {
                        Map<String, Object> body = new HashMap<>();
                        body.put("status", "PRODUCT_LICENSE_NOT_ACTIVATED");
                        body.put("message", "Product License not activated. Ask Admininistrator to activate Product License.");
                        writeResponse(resp, body);
                    } else {
                        //If product license expired
                        if (!ILicenseService.isLicensed(user.getTenant())) {
                            Map<String, Object> body = new HashMap<>();
                            body.put("status", "PRODUCT_LICENSE_EXPIRED");
                            body.put("message", "Product License expired. Ask Admininistrator to renew Product License.");
                            writeResponse(resp,body);
                        }else {
                            filterChain.doFilter(request,response);
                        }
                    }
                }

            }else {
                filterChain.doFilter(request,response);
            }
        } else {
            filterChain.doFilter(request,response);
        }
    }

    private void writeResponse(HttpServletResponse response, Map<String, Object> body) throws IOException {
        ObjectMapper objectMapper = ApplicationContextUtil.getApplicationContext().getBean(ObjectMapper.class);

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.write(objectMapper.writeValueAsString(body));
        out.flush();
        out.close();
    }
}
