package com.jazasoft.mtdb.assember;

import com.jazasoft.mtdb.entity.UrlInterceptor;
import com.jazasoft.mtdb.restcontroller.InterceptorRestController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class UrlInterceptorAssembler extends ResourceAssemblerSupport<UrlInterceptor, Resource>{

    public UrlInterceptorAssembler(){
        super(InterceptorRestController.class, Resource.class);
    }

    @Override
    public Resource toResource(UrlInterceptor urlInterceptor) {
        return new Resource<>(urlInterceptor, linkTo(methodOn(InterceptorRestController.class).getUrlInterceptor(null,urlInterceptor.getId())).withSelfRel());
    }

    @Override
    public List<Resource> toResources(Iterable<? extends UrlInterceptor> urlInterceptors) {
        List<Resource> resources = new ArrayList<>();
        for(UrlInterceptor urlInterceptor : urlInterceptors) {
            resources.add(toResource(urlInterceptor));
        }
        return resources;
    }

}
