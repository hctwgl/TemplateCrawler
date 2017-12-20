package com.seveniu.crawler.spider;

import com.seveniu.entity.CrawlerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Collections;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by seveniu on 7/22/17.
 * *
 */
public class TemplateSpider extends Spider {
    private static AtomicInteger allCount = new AtomicInteger();
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private CrawlerTask crawlerTask;
    private Date timeCreated = new Date();
    private Date timeEnded;
    private SpiderCloseListener closeListener;
    private AtomicInteger errorPageCount;

    public TemplateSpider(PageProcessor pageProcessor, CrawlerTask crawlerTask) {
        super(pageProcessor);
        this.crawlerTask = crawlerTask;
        this.setUUID("spider-" + allCount.getAndIncrement() + "-task-" + this.crawlerTask.getTask().getId() + "-" + this.timeCreated.getTime())
                .thread(
                        Executors.newFixedThreadPool(crawlerTask.getTask().getThreadNum(), CrawlerThreadPoolFactory.getSpiderThreadFactory(crawlerTask.getTask().getId())),

                        crawlerTask.getTask().getThreadNum())
                .addUrl(crawlerTask.getTask().getStartUrlList())
                .setSpiderListeners(Collections.singletonList(new Listener()))
        ;
    }

    @Override
    protected void initComponent() {
        // 顺序固定
        this.init();
        super.initComponent();
    }

    private void init() {
        this.errorPageCount = new AtomicInteger();
    }

    @Override
    public void close() {
        super.close();
        if (closeListener != null) {
            closeListener.close();
        }
        this.timeEnded = new Date();
        log.info("task : {} run from {} to {}, get page {}, error page: {}", this.crawlerTask.getTask().getId(), this.timeCreated, this.timeEnded, this.getPageCount(), this.errorPageCount.get());
    }

    private class Listener implements SpiderListener {

        @Override
        public void onSuccess(Request request) {

        }

        @Override
        public void onError(Request request) {

        }
    }

    // get and set

    public CrawlerTask getCrawlerTask() {
        return crawlerTask;
    }

    public void setCrawlerTask(CrawlerTask crawlerTask) {
        this.crawlerTask = crawlerTask;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public Date getTimeEnded() {
        return timeEnded;
    }

    public void setTimeEnded(Date timeEnded) {
        this.timeEnded = timeEnded;
    }
}
