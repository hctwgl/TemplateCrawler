package com.seveniu.crawler.spider;

import com.seveniu.entity.task.Task;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Date;

/**
 * Created by seveniu on 7/22/17.
 * *
 */
public class RunningSpider extends Spider {
    private Task task;
    private Date timeCreated = new Date();

    public RunningSpider(PageProcessor pageProcessor, Task task) {
        super(pageProcessor);
        this.task = task;
    }

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


}
