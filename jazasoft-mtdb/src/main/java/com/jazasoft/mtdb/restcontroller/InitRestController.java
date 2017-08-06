package com.jazasoft.mtdb.restcontroller;

import com.jazasoft.mtdb.ApiUrls;
import com.jazasoft.mtdb.Constants;
import com.jazasoft.mtdb.assember.CompanyAssembler;
import com.jazasoft.mtdb.assember.UserAssembler;
import com.jazasoft.mtdb.dto.RestError;
import com.jazasoft.mtdb.entity.Company;
import com.jazasoft.mtdb.service.CompanyService;
import com.jazasoft.mtdb.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by mdzahidraza on 06/08/17.
 */
public class InitRestController {

    protected CompanyService companyService;
    protected CompanyAssembler companyAssembler;
    protected UserService userService;
    protected UserAssembler userAssembler;

    public InitRestController(CompanyService companyService, CompanyAssembler companyAssembler, UserService userService, UserAssembler userAssembler) {
        this.companyService = companyService;
        this.companyAssembler = companyAssembler;
        this.userService = userService;
        this.userAssembler = userAssembler;
    }

    @GetMapping(ApiUrls.URL_INIT_MASTER)
    public ResponseEntity<?> initMaster(@RequestParam("role") String role) {
        Pattern pattern = Pattern.compile("ROLE_MASTER|ROLE_ADMIN|ROLE_OTHER", Pattern.CASE_INSENSITIVE);
        if (!pattern.matcher(role).matches()) {
            return new ResponseEntity<Object>(new RestError(400,402,"value of role can be one of [ROLE_MASTER,ROLE_ADMIN,ROLE_OTHER]"), HttpStatus.BAD_REQUEST);
        }
        Map<String, Object> response = new HashMap<>();
        if (role.equalsIgnoreCase(Constants.ROLE_MASTER)) {
            response.put("tenants", companyAssembler.toResources(companyService.findAll()));
            response.put("users", userAssembler.toResources(userService.findAll()));

        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
