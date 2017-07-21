package com.jazasoft.mtdb.assember;

import com.jazasoft.mtdb.entity.User;
import com.jazasoft.mtdb.restcontroller.UserRestController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class UserAssembler extends ResourceAssemblerSupport<User, Resource>{

    public UserAssembler(){
        super(UserRestController.class, Resource.class);
    }

    @Override
    public Resource toResource(User userDto) {
        return new Resource<>(userDto, linkTo(methodOn(UserRestController.class).getUser(null,userDto.getId())).withSelfRel());
    }

    @Override
    public List<Resource> toResources(Iterable<? extends User> users) {
        List<Resource> resources = new ArrayList<>();
        for(User user : users) {
            resources.add(toResource(user));
        }
        return resources;
    }

}
