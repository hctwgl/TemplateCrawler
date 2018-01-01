package com.seveniu.entity.template;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.seveniu.common.json.Json;
import com.seveniu.entity.base.BaseAuditableEntity;
import com.seveniu.entity.template.structure.PageTemplate;
import com.seveniu.entity.template.structure.field.Field;
import com.seveniu.entity.template.structure.field.FieldType;

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
    public static final int PREVENT_CYCLE_LAYER = 8;
    private String name;
    private Long websiteId;

    @Lob
    @JsonIgnore
    private String structure;
    // 0 表示起始 url 开始就是 内容层级
    // 1 表示从起始url 的下层url 开始是内容层级
    // -1 表示模板为非层级模板，都是内容页，需要通过Page url 匹配
    private Integer contentStartLayer = 1;

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
        if (contentStartLayer != null) {
            return contentStartLayer;
        }
        int i = 0;
        for (PageTemplate pageTemplate : pageStructure) {
            for (Field field : pageTemplate.getFields()) {
                if (field.getType() == FieldType.HTML_TEXT || field.getType() == FieldType.PURE_TEXT || field.getType() == FieldType.TEXT_LINK) {
                    this.contentStartLayer = i;
                    return i;
                }
            }
            i++;
        }
        throw new IllegalArgumentException("no content layer");
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
