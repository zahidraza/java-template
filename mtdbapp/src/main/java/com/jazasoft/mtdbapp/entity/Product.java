package com.jazasoft.mtdbapp.entity;

import com.jazasoft.mtdb.entity.BaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Created by mdzahidraza on 26/06/17.
 */
@Entity
public class Product extends BaseEntity implements Serializable {

    @Audited
    private String name;

    @Audited
    private String description;

    public Product() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
