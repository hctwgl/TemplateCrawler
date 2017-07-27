package com.seveniu.crawler.spider;

import com.seveniu.crawler.spider.pageProcessor.TemplatePageProcessor;
import com.seveniu.entity.task.Task;
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
public class RunningSpider extends Spider {
    private static AtomicInteger allCount = new AtomicInteger();
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private Task task;
    private Date timeCreated = new Date();
    private Date timeEnded;
    private SpiderCloseListener closeListener;
    private AtomicInteger errorPageCount;

    public RunningSpider(PageProcessor pageProcessor, Task task) {
        super(pageProcessor);
        this.task = task;
    }

    @Override
    protected void initComponent() {
        // 顺序固定
        this.init();
        super.initComponent();
    }

    private void init() {
        this.setUUID("spider-" + allCount.getAndIncrement() + "-task-" + this.task.getId() + "-" + this.timeCreated.getTime())
                .thread(
                        Executors.newFixedThreadPool(task.getThreadNum(), CrawlerThreadPoolFactory.getSpiderThreadFactory(task)),

                        task.getThreadNum())
                .addUrl(task.getStartUrlList())
                .setSpiderListeners(Collections.singletonList(new Listener()))
        ;
        this.errorPageCount = new AtomicInteger();
    }

    @Override
    public void close() {
        super.close();
        if (closeListener != null) {
            closeListener.close();
        }
        this.timeEnded = new Date();
        log.info("task : {} run from {} to {}, get page {}, error page: {}", this.task.getId(), this.timeCreated, this.timeEnded, this.getPageCount(), this.errorPageCount.get());
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
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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
