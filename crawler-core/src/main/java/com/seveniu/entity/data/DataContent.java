package com.seveniu.entity.data;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Transient;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by seveniu on 7/22/17.
 * url 对应的 content
 * *
 */
public class DataContent {
    private boolean error; // 标记是否失败

    public DataContent(String url) {
        this.url = url;
    }

    private String url;
    private List<PageContent> pageContents;
    private List<DataContent> children;
    @Transient
    @JsonIgnore
    private DataContent parent;
    private AtomicInteger allChildrenCount = new AtomicInteger(0);
    private AtomicInteger doneChildrenCount = new AtomicInteger(0);

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

    public boolean isError() {
        return error;
    }

    public synchronized void addChild(DataContent child) {
        this.children.add(child);
        child.parent = this;
        this.allChildrenCount.getAndIncrement();
    }

    public DataContent addPage(PageContent pageContent) {
        if (this.pageContents == null) {
            this.pageContents = new LinkedList<>();
        }
        this.pageContents.add(pageContent);
        this.pageContents.sort(Comparator.comparingInt(PageContent::getIndex));
        return this;
    }

    /**
     * 递归向上遍历父节点，判断是否完成. 没有完成 返回 null；直到根节点都完成，则返回 根节点
     */
    public DataContent error() {
        this.error = true;
        if (this.allChildrenCount.get() == this.doneChildrenCount.get()) {
            if (this.parent == null) {
                return this;
            } else {
                return this.parent.error();
            }
        }
        return null;
    }
    /**
     * 递归向上遍历父节点，判断是否完成. 没有完成 返回 null；直到根节点都完成，则返回 根节点
     */
    public DataContent done() {
        if (this.allChildrenCount.get() == this.doneChildrenCount.get()) {
            if (this.parent == null) {
                return this;
            } else {
                return this.parent.done();
            }
        }
        return null;
    }

}
