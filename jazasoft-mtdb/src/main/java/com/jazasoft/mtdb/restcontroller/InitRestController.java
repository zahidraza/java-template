package com.jazasoft.mtdb.restcontroller;

import com.jazasoft.mtdb.ApiUrls;
import com.jazasoft.mtdb.Constants;
import com.jazasoft.mtdb.assember.CompanyAssembler;
import com.jazasoft.mtdb.assember.RoleAssembler;
import com.jazasoft.mtdb.assember.UrlInterceptorAssembler;
import com.jazasoft.mtdb.assember.UserAssembler;
import com.jazasoft.mtdb.dto.RestError;
import com.jazasoft.mtdb.entity.Company;
import com.jazasoft.mtdb.entity.Role;
import com.jazasoft.mtdb.service.CompanyService;
import com.jazasoft.mtdb.service.InterceptorService;
import com.jazasoft.mtdb.service.RoleService;
import com.jazasoft.mtdb.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by mdzahidraza on 06/08/17.
 */
public class InitRestController {

    private final Logger LOGGER = LoggerFactory.getLogger(InitRestController.class);

    protected CompanyService companyService;
    protected CompanyAssembler companyAssembler;
    protected UserService userService;
    protected UserAssembler userAssembler;
    protected RoleService roleService;
    protected RoleAssembler roleAssembler;
    protected InterceptorService interceptorService;
    protected UrlInterceptorAssembler interceptorAssembler;

    public InitRestController(CompanyService companyService, CompanyAssembler companyAssembler, UserService userService, UserAssembler userAssembler, RoleService roleService, RoleAssembler roleAssembler, InterceptorService interceptorService, UrlInterceptorAssembler interceptorAssembler) {
        this.companyService = companyService;
        this.companyAssembler = companyAssembler;
        this.userService = userService;
        this.userAssembler = userAssembler;
        this.roleService = roleService;
        this.roleAssembler = roleAssembler;
        this.interceptorService = interceptorService;
        this.interceptorAssembler = interceptorAssembler;
    }

    @GetMapping(ApiUrls.URL_INIT_MASTER)
    public ResponseEntity<?> initMaster(HttpServletRequest req, @RequestParam("role") String role) {
        Company company = (Company)req.getAttribute(Constants.CURRENT_TENANT);
        LOGGER.debug("initMaster(): role = {} ", role);

        Pattern pattern = Pattern.compile("ROLE_MASTER|ROLE_ADMIN|ROLE_OTHER", Pattern.CASE_INSENSITIVE);
        if (!pattern.matcher(role).matches()) {
            return new ResponseEntity<Object>(new RestError(400,402,"value of role can be one of [ROLE_MASTER,ROLE_ADMIN,ROLE_OTHER]"), HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> response = new HashMap<>();
        if (role.equalsIgnoreCase(Constants.ROLE_MASTER)) {
            response.put("tenants", companyAssembler.toResources(companyService.findAll()));
            response.put("users", userAssembler.toResources(userService.findAll()));
        }else if (role.equalsIgnoreCase(Constants.ROLE_ADMIN)) {
            //userService.findByEmail()
            response.put("users", userAssembler.toResources(userService.findAllByCompanyAfter(company, 0L)));
            List<Role> roles = roleService.findAll(company);
            roles.add(roleService.findOne(2L));  //Admin Role
            response.put("roles", roleAssembler.toResources(roles));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
