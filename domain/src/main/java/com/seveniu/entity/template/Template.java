package com.seveniu.entity.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.seveniu.common.json.Json;
import com.seveniu.entity.BaseAuditableEntity;
import com.seveniu.entity.template.structure.PageTemplate;
import com.seveniu.entity.website.Website;

import javax.persistence.*;
import java.util.List;

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
    @JsonIgnore
    private String structure;
    private int contentLayer = 1; // 0 表示起始 url 开始就是 内容层级， 1 表示从起始url 的下层url 开始是内容层级

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

    public Website getWebsite() {
        return website;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public int getContentLayer() {
        return contentLayer;
    }

    public void setContentLayer(int contentLayer) {
        this.contentLayer = contentLayer;
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
