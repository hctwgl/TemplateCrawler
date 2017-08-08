package com.seveniu.crawler.spider;


import com.seveniu.entity.CrawlerTask;

import java.util.Collection;

/**
 * Created by seveniu on 7/22/17.
 * *
 */
public interface SpiderManager {

    RunningSpider runSpider(CrawlerTask task);

    Collection<RunningSpider> getRunningSpiders();

    Collection<RunningSpider> getRunningSpidersByUserId(Long userId);
}
