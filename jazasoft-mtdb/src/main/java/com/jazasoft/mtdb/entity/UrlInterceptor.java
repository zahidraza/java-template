package com.jazasoft.mtdb.entity;

import org.springframework.hateoas.core.Relation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by mdzahidraza on 28/06/17.
 */
@Entity
@Relation(value = "interceptor", collectionRelation = "interceptors")
public class UrlInterceptor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String url;

    @NotNull
    private String httpMethod;

    @NotNull
    private String access;  // User Role

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Transient
    private Long companyId;

    public UrlInterceptor() {
    }

    public UrlInterceptor(String url, String httpMethod) {
        this.url = url;
        this.httpMethod = httpMethod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UrlInterceptor)) return false;

        UrlInterceptor that = (UrlInterceptor) o;

        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
