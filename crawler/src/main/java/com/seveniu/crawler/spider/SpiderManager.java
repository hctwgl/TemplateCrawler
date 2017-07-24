package com.seveniu.crawler.spider;

import com.seveniu.entity.task.Task;

import java.util.Collection;

/**
 * Created by seveniu on 7/22/17.
 * *
 */
public interface SpiderManager {

    RunningSpider runSpider(Task task);

    Collection<RunningSpider> getRunningSpiders();

    Collection<RunningSpider> getRunningSpidersByUserId(Long userId);
}
