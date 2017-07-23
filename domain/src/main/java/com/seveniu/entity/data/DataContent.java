package com.seveniu.entity.data;

import java.util.List;

/**
 * Created by seveniu on 7/22/17.
 * *
 */
public class DataContent {
    private String url;
    private List<PageContent> pageContents;
    private List<DataContent> children;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<PageContent> getPageContents() {
        return pageContents;
    }

    public void setPageContents(List<PageContent> pageContents) {
        this.pageContents = pageContents;
    }

    public List<DataContent> getChildren() {
        return children;
    }

    public void setChildren(List<DataContent> children) {
        this.children = children;
    }
}
