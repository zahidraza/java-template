package com.jazasoft.mtdb.restcontroller;


import com.jazasoft.mtdb.ApiUrls;
import com.jazasoft.mtdb.Constants;
import com.jazasoft.mtdb.assember.UserAssembler;
import com.jazasoft.mtdb.dto.RestError;
import com.jazasoft.mtdb.dto.UserDto;
import com.jazasoft.mtdb.entity.Company;
import com.jazasoft.mtdb.entity.User;
import com.jazasoft.mtdb.service.CompanyService;
import com.jazasoft.mtdb.service.RoleService;
import com.jazasoft.mtdb.service.UserService;
import com.jazasoft.mtdb.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@RestController
@RequestMapping(ApiUrls.ROOT_URL_USERS)
public class UserRestController {

    private final Logger logger = LoggerFactory.getLogger(UserRestController.class);

    @Autowired
    UserService userService;  //Service which will do all data retrieval/manipulation work

    @Autowired
    CompanyService companyService;

    @Autowired
    RoleService roleService;

    @Autowired
    UserAssembler userAssembler;

    @GetMapping
    public ResponseEntity<?> listAllUsers(HttpServletRequest req, @RequestParam(value = "after", defaultValue = "0") Long after) {
        Company company = (Company)req.getAttribute(Constants.CURRENT_TENANT);
        logger.debug("listAllUsers(): tenant = {}", company != null ? company.getName() : "");
        List<User> users;
        if (company != null) {
            users = userService.findAllByCompanyAfter(company, after);
        }else {
            users = userService.findAllAfter(after);
        }
        Resources resources = new Resources(userAssembler.toResources(users), linkTo(UserRestController.class).withSelfRel());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(ApiUrls.URL_USERS_USER)
    public ResponseEntity<?> getUser(HttpServletRequest req, @PathVariable("userId") long id) {
        logger.debug("getUser(): id = {}",id);
        User user = userService.findOne(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userAssembler.toResource(user), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createUser(HttpServletRequest req, @Valid @RequestBody User user) {
        Company company = (Company)req.getAttribute(Constants.CURRENT_TENANT);
        logger.debug("createUser(): tenant= {}", company != null ? company.getName() : "");

        if (company != null) {
            user.setCompany(company);
        }else {
            if (user.getCompanyId() != null && !companyService.exists(user.getCompanyId())) {
                RestError error = new RestError(404, 40401,"Company with Id=" + user.getCompanyId() + " not found","","");
                return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
            }
        }
        if (user.getRoles() != null) {
            StringBuilder builder = new StringBuilder();
            for(String role: Utils.getRoleList(user.getRoles())){
                if (!roleService.exists(role)){
                    builder.append(role).append(",");
                }
            }
            if (builder.length() > 0) {
                builder.setLength(builder.length()-1);
                RestError error = new RestError(404, 40401,"["+builder.toString() +"] not found","","");
                return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
            }
        }
        user = userService.save(user);
        Link selfLink = linkTo(UserRestController.class).slash(user.getId()).withSelfRel();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(selfLink.getHref()));
        return new ResponseEntity<>(userAssembler.toResource(user), headers, HttpStatus.CREATED);
    }

    @PatchMapping(ApiUrls.URL_USERS_USER)
    public ResponseEntity<?> updateUser(HttpServletRequest req, @PathVariable("userId") long id,@Validated @RequestBody UserDto userDto) {
        Company company = (Company)req.getAttribute(Constants.CURRENT_TENANT);
        logger.debug("updateUser(): tenant ={}, id = {}",company != null ? company.getName() : "", id);
        if (!userService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (company != null && !userService.exists(company, id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (userDto.getRoles() != null) {
            StringBuilder builder = new StringBuilder();
            for(String role: Utils.getRoleList(userDto.getRoles())){
                if (!roleService.exists(role)){
                    builder.append(role).append(",");
                }
            }
            if (builder.length() > 0) {
                builder.setLength(builder.length()-1);
                RestError error = new RestError(404, 40401,"["+builder.toString() +"] not found","","");
                return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
            }
        }
        userDto.setId(id);
        User user = userService.update(userDto);
        return ResponseEntity.ok(userAssembler.toResource(user));
    }

    @DeleteMapping(ApiUrls.URL_USERS_USER)
    public ResponseEntity<Void> deleteUser(HttpServletRequest req, @PathVariable("userId") long id) {
        Company company = (Company)req.getAttribute(Constants.CURRENT_TENANT);
        logger.debug("deleteUser(): tenant = {} id = {}",company != null ? company.getName() : "",id);
        if (!userService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (company != null && !userService.exists(company, id)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(ApiUrls.URL_USERS_USER_SEARCH_BY_USERNAME)
    public ResponseEntity<?> searchByName(HttpServletRequest req, @RequestParam("username") String username){
        logger.debug("searchByUsername(): username = {}",username);
        Long userId = (Long) req.getAttribute(Constants.CURRENT_USER);
        User user = userService.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else if (userId != null && user.getId() != userId) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(userAssembler.toResource(user), HttpStatus.OK);
    }

    @GetMapping(ApiUrls.URL_USERS_USER_SEARCH_BY_EMAIL)
    public ResponseEntity<?> searchByEmail(HttpServletRequest req, @RequestParam("email") String email){
        logger.debug("searchByName(): name = {}",email);
        Long userId = (Long) req.getAttribute(Constants.CURRENT_USER);
        User user = userService.findByEmail(email);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else if (userId != null && user.getId() != userId) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(userAssembler.toResource(user), HttpStatus.OK);
    }

    @PutMapping(ApiUrls.URL_USERS_USER_PROFILE)
    public ResponseEntity<?> updateProfile() {
        return null;
    }
}
