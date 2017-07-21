package com.jazasoft.mtdb.assember;


import com.jazasoft.mtdb.entity.Company;
import com.jazasoft.mtdb.restcontroller.CompanyRestController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by mdzahidraza on 16/07/17.
 */
@Component
public class CompanyAssembler extends ResourceAssemblerSupport<Company, Resource>{

    public CompanyAssembler(){
        super(CompanyRestController.class, Resource.class);
    }

    @Override
    public Resource toResource(Company company) {
        return new Resource<>(company, linkTo(methodOn(CompanyRestController.class).getCompany(company.getId())).withSelfRel());
    }

    @Override
    public List<Resource> toResources(Iterable<? extends Company> companys) {
        List<Resource> resources = new ArrayList<>();
        for(Company company : companys) {
            resources.add(toResource(company));
        }
        return resources;
    }

}
