package com.seveniu.entity.template;

import com.seveniu.entity.BaseAuditableEntity;
import com.seveniu.entity.template.structure.TemplateStructure;
import com.seveniu.entity.website.Website;

import javax.persistence.*;

/**
 * Created by seveniu on 7/22/17.
 * *
 */
@Entity
public class Template extends BaseAuditableEntity {
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    private Website website;

    @Lob
    private String structure;

    @Transient
    private TemplateStructure templateStructure;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 不能主动调用
     *
     * @return
     */
    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public TemplateStructure getTemplateStructure() {
        if (templateStructure == null) {
            this.templateStructure = TemplateStructure.fromJson(this.structure);
        }
        return templateStructure;
    }

    public void setTemplateStructure(TemplateStructure templateStructure) {
        if (templateStructure == null) {
            this.structure = null;
        } else {
            this.structure = templateStructure.toJson();
        }
        this.templateStructure = templateStructure;
    }

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }
}
