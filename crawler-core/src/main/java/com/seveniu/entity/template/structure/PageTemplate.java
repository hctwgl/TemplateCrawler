package com.seveniu.entity.template.structure;

import com.seveniu.entity.template.structure.field.Field;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by seveniu on 5/12/16.
 * PageTemplate
 * 单网页模板
 */
public class PageTemplate {
    private String name;
    private String url;
    private List<Field> fields;
    private String urlPattern; // url 匹配正则

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }

    private Pattern pattern;

    public boolean urlMatch(String url) {
        if (pattern == null) {
            pattern = Pattern.compile(urlPattern);
        }
        return pattern.matcher(url).find();
    }

    @Override
    public String toString() {
        return "Template{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", fields=" + fields +
                '}';
    }
}
