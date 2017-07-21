package com.jazasoft.mtdb.assember;

import com.jazasoft.mtdb.entity.Role;
import com.jazasoft.mtdb.restcontroller.RoleRestController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class RoleAssembler extends ResourceAssemblerSupport<Role, Resource>{

    public RoleAssembler(){
        super(RoleRestController.class, Resource.class);
    }

    @Override
    public Resource toResource(Role role) {
        return new Resource<>(role, linkTo(methodOn(RoleRestController.class).getRole(null,role.getId())).withSelfRel());
    }

    @Override
    public List<Resource> toResources(Iterable<? extends Role> roles) {
        List<Resource> resources = new ArrayList<>();
        for(Role role : roles) {
            resources.add(toResource(role));
        }
        return resources;
    }

}
