package com.jazasoft.mtdb.entity;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by mdzahidraza on 01/07/17.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    protected Date modifiedAt;

    protected boolean enabled;

    public BaseEntity() {
    }

    public BaseEntity(boolean enabled) {
        this.enabled = enabled;
    }

    public BaseEntity(Long id) {
        this.id = id;
    }

    public BaseEntity(Long id, boolean enabled) {
        this.id = id;
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
