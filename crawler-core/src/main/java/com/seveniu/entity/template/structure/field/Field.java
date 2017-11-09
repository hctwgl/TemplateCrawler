package com.seveniu.entity.template.structure.field;

import java.util.List;

public class Field {

    private String name; // 必须符合驼峰命名法
    private String label;
    private FieldType type;

    private String defaultValue = "";
    private String xpath;
    private List<String> regex;
    private boolean must = false;
    private boolean multiple = true; // 有多个值

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXpath() {
        return xpath;
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public List<String> getRegex() {
        return regex;
    }

    public void setRegex(List<String> regex) {
        this.regex = regex;
    }


    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isMust() {
        return must;
    }

    public void setMust(boolean must) {
        this.must = must;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    @Override
    public String toString() {
        return "Field{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                ", xpath='" + xpath + '\'' +
                ", regex=" + regex +
                ", must=" + must +
                '}';
    }

}
