package com.seveniu.crawler.spider;

import com.seveniu.crawler.spider.pageProcessor.parse.PageParseResult;
import com.seveniu.crawler.spider.pageProcessor.parse.TemplatePageParse;
import com.seveniu.entity.CrawlerTask;
import com.seveniu.entity.template.structure.PageTemplate;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * Created by seveniu on 12/20/17.
 * *
 */
public class SinglePageSpider extends TemplateSpider {
    public SinglePageSpider(PageProcessor pageProcessor, CrawlerTask task) {
        super(pageProcessor, task);
    }

    public Html downloadPage(String url) {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        return httpClientDownloader.download(url);
    }

    public PageParseResult parse(String url, PageTemplate pageTemplate) {
        Html html = downloadPage(url);
        return TemplatePageParse.parse(pageTemplate, html, url);
    }
}
