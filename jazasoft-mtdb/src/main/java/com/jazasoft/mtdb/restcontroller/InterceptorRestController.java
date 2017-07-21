package com.jazasoft.mtdb.restcontroller;

import com.jazasoft.mtdb.ApiUrls;
import com.jazasoft.mtdb.Constants;
import com.jazasoft.mtdb.assember.UrlInterceptorAssembler;
import com.jazasoft.mtdb.dto.RestError;
import com.jazasoft.mtdb.entity.Company;
import com.jazasoft.mtdb.entity.UrlInterceptor;
import com.jazasoft.mtdb.service.CompanyService;
import com.jazasoft.mtdb.service.InterceptorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping(ApiUrls.ROOT_URL_INTERCEPTORS)
public class InterceptorRestController {
    private final Logger LOGGER = LoggerFactory.getLogger(InterceptorRestController.class);

    @Autowired
    InterceptorService urlInterceptorService;

    @Autowired
    UrlInterceptorAssembler urlInterceptorAssembler;

    @Autowired
    CompanyService companyService;

    @GetMapping
    public ResponseEntity<?> getAllUrlInterceptors(HttpServletRequest req) {
        Company company = (Company)req.getAttribute(Constants.CURRENT_TENANT);
        LOGGER.debug("getAllUrlInterceptors(): tenant = {}", company != null ? company.getName() : "");
        List<UrlInterceptor> urlInterceptors = urlInterceptorService.findAll(company);
        Resources resources = new Resources(urlInterceptorAssembler.toResources(urlInterceptors), linkTo(InterceptorRestController.class).withSelfRel());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(ApiUrls.URL_INTERCEPTORS_INTERCEPTOR)
    public ResponseEntity<?> getUrlInterceptor(HttpServletRequest req, @PathVariable("urlInterceptorId") Long urlInterceptorId) {
        Company company = (Company)req.getAttribute(Constants.CURRENT_TENANT);
        LOGGER.debug("getUrlInterceptor(): tenant = {},  urlInterceptorId = {}", company != null ? company.getName() : "", urlInterceptorId);
        if (!urlInterceptorService.exists(urlInterceptorId)) {
            return ResponseEntity.notFound().build();
        }
        if (company != null && !urlInterceptorService.exists(company,urlInterceptorId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        UrlInterceptor urlInterceptor = urlInterceptorService.findOne(urlInterceptorId);
        return ResponseEntity.ok(urlInterceptorAssembler.toResource(urlInterceptor));
    }

    @PostMapping
    public ResponseEntity<?> save(HttpServletRequest req, @Valid @RequestBody UrlInterceptor urlInterceptor){
        Company company = (Company)req.getAttribute(Constants.CURRENT_TENANT);
        LOGGER.debug("save(): tenant = {}", company != null ? company.getName() : "");
        if (company != null) {
            urlInterceptor.setCompany(company);
        }else {
            if (urlInterceptor.getCompanyId() != null && !companyService.exists(urlInterceptor.getCompanyId())) {
                RestError error = new RestError(404, 40401,"Company with Id=" + urlInterceptor.getCompanyId() + " not found","","");
                return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
            }
        }
        urlInterceptor = urlInterceptorService.save(urlInterceptor);
        Link selfLink = linkTo(InterceptorRestController.class).slash(urlInterceptor.getId()).withSelfRel();
        return ResponseEntity.created(URI.create(selfLink.getHref())).build();
    }

    @PutMapping(ApiUrls.URL_INTERCEPTORS_INTERCEPTOR)
    public ResponseEntity<?> update(HttpServletRequest req, @PathVariable("urlInterceptorId") Long urlInterceptorId, @Valid @RequestBody UrlInterceptor urlInterceptor){
        Company company = (Company)req.getAttribute(Constants.CURRENT_TENANT);
        LOGGER.debug("update(): tenant = {}, urlInterceptorId = {}", company != null ? company.getName() : "", urlInterceptorId);
        if (!urlInterceptorService.exists(urlInterceptorId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (company != null && !urlInterceptorService.exists(company, urlInterceptorId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        urlInterceptor.setId(urlInterceptorId);
        urlInterceptor = urlInterceptorService.update(urlInterceptor);
        return ResponseEntity.ok(urlInterceptorAssembler.toResource(urlInterceptor));
    }

    @DeleteMapping(ApiUrls.URL_INTERCEPTORS_INTERCEPTOR)
    public ResponseEntity<?> delete(HttpServletRequest req, @PathVariable("urlInterceptorId") Long urlInterceptorId){
        Company company = (Company)req.getAttribute(Constants.CURRENT_TENANT);
        LOGGER.debug("update(): tenant = {}, urlInterceptorId = {}", company != null ? company.getName() : "", urlInterceptorId);
        if (!urlInterceptorService.exists(urlInterceptorId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (company != null && !urlInterceptorService.exists(company, urlInterceptorId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        urlInterceptorService.delete(urlInterceptorId);
        return ResponseEntity.noContent().build();
    }
}
