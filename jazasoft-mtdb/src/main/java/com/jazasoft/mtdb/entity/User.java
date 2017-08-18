package com.jazasoft.mtdb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.jazasoft.mtdb.dto.Permission;
import org.springframework.hateoas.core.Relation;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "users", indexes = @Index(columnList = "name,email,username"))
@Relation(collectionRelation = "users", value = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends BaseEntity implements UserDetails{

    private static final long serialVersionUID = -8053465676093594890L;

    @NotNull @Size(min = 3, max = 50)
    @Column(nullable = false)
    private String name;

    @NotNull @Pattern(regexp="^(?=.*[a-zA-Z])[a-zA-Z0-9_\\-\\.]{3,50}$")
    @Column(nullable = false, unique = true)
    private String username;

    @NotNull @Pattern(regexp="^(?=.*[a-zA-Z])[a-zA-Z0-9_\\-@\\.]{5,50}$")
    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @NotNull @Pattern(regexp="[0-9]{10}")
    private String mobile;

    @JsonIgnore
    private Integer retryCount;

    @JsonIgnore
    private String otp;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    private Date otpSentAt;

    private boolean accountExpired;

    private boolean accountLocked;

    private boolean credentialExpired;

    @Transient
    private Long companyId;

    @Transient
    private String roles;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role_rel",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roleList = new HashSet<>();

    @Transient
    private Set<Permission> permissions = new HashSet<>();

    public User() {
    }

    public User(String name, String username, String email, String password, String mobile,boolean enabled, boolean accountExpired, boolean accountLocked, boolean credentialExpired) {
        super(enabled);
        this.name = name;
        this.username = username;
        this.email = email;
        setPassword(password);
        this.mobile = mobile;
        this.accountExpired = accountExpired;
        this.accountLocked = accountLocked;
        this.credentialExpired = credentialExpired;
    }

    public User(String name, String username, String email, String password, String mobile) {
        this.name = name;
        this.username = username;
        this.email = email;
        setPassword(password);
        this.mobile = mobile;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void addPermission(Permission permission) {
        permissions.add(permission);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> rls = new ArrayList<>();
        for (Role role: roleList) {
            rls.add(new SimpleGrantedAuthority(role.getName()));
        }
        return rls;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialExpired;
    }



    public void addRole(Role role){
        roleList.add(role);
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


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public void setCredentialExpired(boolean credentialExpired) {
        this.credentialExpired = credentialExpired;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Date getOtpSentAt() {
        return otpSentAt;
    }

    public void setOtpSentAt(Date otpSentAt) {
        this.otpSentAt = otpSentAt;
    }

    public Set<Role> getRoleList() {
        return roleList;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", modifiedAt=" + modifiedAt +
                ", name='" + name + '\'' +
                ", enabled=" + enabled +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
