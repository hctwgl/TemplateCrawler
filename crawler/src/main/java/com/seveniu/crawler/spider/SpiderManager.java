package com.seveniu.crawler.spider;


import com.seveniu.entity.CrawlerTask;

import java.util.Collection;

/**
 * Created by seveniu on 7/22/17.
 * *
 */
public interface SpiderManager {

    TemplateSpider runSpider(CrawlerTask task);

    Collection<TemplateSpider> getRunningSpiders();

    Collection<TemplateSpider> getRunningSpidersByUserId(Long userId);
}
