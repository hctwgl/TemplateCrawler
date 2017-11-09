package com.seveniu.crawler.spider.pageProcessor.parse;

import com.seveniu.entity.data.PageContent;

import java.util.Collections;
import java.util.List;

/**
 * Created by seveniu on 7/24/17.
 * *
 */
public class PageParseResult {
    private List<String> nextLevelUrls; // 下一层 url
    private List<String> pageUrls; // 同级页面 url
    private PageContent pageContent; // 页面内容

    public PageParseResult(String url) {
        this.pageContent = new PageContent();
        this.pageContent.setUrl(url);
    }

    public List<String> getNextLevelUrls() {
        if (nextLevelUrls == null) {
            return Collections.emptyList();
        }
        return nextLevelUrls;
    }

    public void setNextLevelUrls(List<String> nextLevelUrls) {
        this.nextLevelUrls = nextLevelUrls;
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
