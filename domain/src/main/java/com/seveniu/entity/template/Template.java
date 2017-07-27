package com.seveniu.entity.template;

import com.seveniu.entity.BaseAuditableEntity;
import com.seveniu.entity.template.structure.TemplateStructure;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;

/**
 * Created by seveniu on 7/22/17.
 * *
 */
@Entity
public class Template extends BaseAuditableEntity {

    @Lob
    private String structure;

    @Transient
    private TemplateStructure templateStructure;

    /**
     * 不能主动调用
     *
     * @return
     */
    public String getStructure() {
        this.structure = templateStructure.toJson();
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
        this.templateStructure = templateStructure;
    }
}
