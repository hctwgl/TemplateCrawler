package com.seveniu.entity.data;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by seveniu on 7/22/17.
 * *
 */
public class PageContent {
    private int index = 0;
    private String url;
    private List<FieldContent> fieldContents;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<FieldContent> getFieldContents() {
        if (fieldContents == null) {
            this.fieldContents = new LinkedList<>();
        }
        return fieldContents;
    }

    public void setFieldContents(List<FieldContent> fieldContents) {
        this.fieldContents = fieldContents;
    }
}
