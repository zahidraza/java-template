package com.jazasoft.mtdb.dto;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by mdzahidraza on 06/08/17.
 */
public class Permission implements Serializable{

    private String resource;
    private Set<String> scopes;

    public Permission() {
    }

    public Permission(String resource, Set<String> scopes) {
        this.resource = resource;
        this.scopes = scopes;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }
}
