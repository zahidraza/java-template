package com.jazasoft.mtdbapp.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by mdzahidraza on 22/07/17.
 */
@Entity
public class TUser {

    @Id
    private Long id;

    public TUser() {
    }

    public TUser(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
