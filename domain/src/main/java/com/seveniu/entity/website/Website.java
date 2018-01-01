package com.seveniu.entity.website;


import com.seveniu.entity.base.BaseAuditableEntity;

import javax.persistence.Entity;

/**
 * Created by seveniu on 7/27/17.
 * *
 */
@Entity
public class Website extends BaseAuditableEntity {
    private String name;
    private String domain;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

}
