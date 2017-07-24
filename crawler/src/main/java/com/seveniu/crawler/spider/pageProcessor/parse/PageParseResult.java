package com.seveniu.crawler.spider.pageProcessor.parse;

import com.seveniu.entity.data.PageContent;

import java.util.Collections;
import java.util.List;

/**
 * Created by seveniu on 7/24/17.
 * *
 */
public class PageParseResult {
    private List<String> nextUrls; // 下一层 url
    private List<String> pageUrls; // 同级页面 url
    private PageContent pageContent; // 页面内容

    public List<String> getNextUrls() {
        if (nextUrls == null) {
            return Collections.emptyList();
        }
        return nextUrls;
    }

    public void setNextUrls(List<String> nextUrls) {
        this.nextUrls = nextUrls;
    }

    public List<String> getPageUrls() {
        if (pageUrls == null) {
            return Collections.emptyList();
        }
        return pageUrls;
    }

    public void setPageUrls(List<String> pageUrls) {
        this.pageUrls = pageUrls;
    }

    public PageContent getPageContent() {
        return pageContent;
    }

    public void setPageContent(PageContent pageContent) {
        this.pageContent = pageContent;
    }
}
