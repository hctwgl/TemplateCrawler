package com.seveniu.entity.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.seveniu.common.json.Json;
import com.seveniu.entity.base.BaseAuditableEntity;
import com.seveniu.entity.template.structure.PageTemplate;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;
import java.util.List;

/**
 * Created by seveniu on 7/22/17.
 * *
 */
@Entity
public class Template extends BaseAuditableEntity {
    private String name;
    private Long websiteId;

    @Lob
    @JsonIgnore
    private String structure;
    private int contentStartLayer = 1; // 0 表示起始 url 开始就是 内容层级， 1 表示从起始url 的下层url 开始是内容层级

    @Transient
    private List<PageTemplate> pageStructure;


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

    public Long getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(Long websiteId) {
        this.websiteId = websiteId;
    }

    public int getContentStartLayer() {
        return contentStartLayer;
    }

    public void setContentStartLayer(int contentStartLayer) {
        this.contentStartLayer = contentStartLayer;
    }

    public List<PageTemplate> getPageStructure() {
        if (pageStructure == null) {
            if (this.structure != null) {
                this.pageStructure = Json.toObject(this.structure, new TypeReference<List<PageTemplate>>() {
                });
            }
        }
        return pageStructure;
    }

    public void setPageStructure(List<PageTemplate> pageStructure) {
        this.pageStructure = pageStructure;
        this.structure = Json.toJson(this.pageStructure);
    }
}
