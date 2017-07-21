package com.jazasoft.mtdb.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.core.Relation;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Relation(collectionRelation = "users",value = "user")
public class UserDto {

    private Long id;

    @Size(min = 3, max = 50)
    private String name;

    @Pattern(regexp="^(?=.*[a-zA-Z])[a-zA-Z0-9_\\-\\.]{3,50}$")
    private String username;

    @Pattern(regexp="^(?=.*[a-zA-Z])[a-zA-Z0-9_\\-@\\.]{5,40}$")
    private String email;

    @JsonIgnore
    private String password;

    @Pattern(regexp="[0-9]{10}")
    private String mobile;

    private String roles;

    private Boolean enabled;

    public UserDto() {
    }

    public UserDto(String name, String username, String email, String mobile) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.mobile = mobile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
