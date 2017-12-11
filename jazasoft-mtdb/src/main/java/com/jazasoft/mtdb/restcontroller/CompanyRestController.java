package com.jazasoft.mtdb.restcontroller;

import com.jazasoft.mtdb.ApiUrls;
import com.jazasoft.mtdb.assember.CompanyAssembler;
import com.jazasoft.mtdb.entity.Company;
import com.jazasoft.mtdb.service.CompanyService;
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

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@RestController
@RequestMapping(ApiUrls.ROOT_URL_COMPANIES)
public class CompanyRestController {
    private final Logger logger = LoggerFactory.getLogger(CompanyRestController.class);

    @Autowired
    CompanyService companyService;  //Service which will do all content retrieval/manipulation work

    @Autowired
    CompanyAssembler companyAssembler;

    @GetMapping
    public ResponseEntity<?> listAllCompanys() {
        logger.debug("listAllCompanys()");
        List<Company> companies = companyService.findAll();
        Resources resources = new Resources(companyAssembler.toResources(companies), linkTo(CompanyRestController.class).withSelfRel());
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(ApiUrls.URL_COMPANIES_COMPANY)
    public ResponseEntity<?> getCompany(@PathVariable("companyId") long id) {
        logger.debug("getCompany(): id = {}",id);
        Company company = companyService.findOne(id);
        if (company == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(companyAssembler.toResource(company), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createCompany(@Valid @RequestBody Company company) {
        logger.debug("createCompany()");
        company = companyService.save(company);
        Link selfLink = linkTo(CompanyRestController.class).slash(company.getId()).withSelfRel();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(selfLink.getHref()));
        return new ResponseEntity<>(companyAssembler.toResource(company), headers, HttpStatus.CREATED);
    }

    @PatchMapping(ApiUrls.URL_COMPANIES_COMPANY)
    public ResponseEntity<?> updateCompany(@PathVariable("companyId") long id,@Validated @RequestBody Company company) {
        logger.debug("updateCompany(): id = {}",id);
        if (!companyService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        company.setId(id);
        company = companyService.update(company);
        return new ResponseEntity<>(companyAssembler.toResource(company), HttpStatus.OK);
    }

    @DeleteMapping(ApiUrls.URL_COMPANIES_COMPANY)
    public ResponseEntity<Void> deleteCompany(@PathVariable("companyId") long id) {
        logger.debug("deleteCompany(): id = {}",id);
        if (!companyService.exists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        companyService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
