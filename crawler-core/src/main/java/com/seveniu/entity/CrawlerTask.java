package com.seveniu.entity;

import com.seveniu.entity.task.Task;
import com.seveniu.entity.template.Template;

/**
 * Created by seveniu on 8/8/17.
 * *
 */
public class CrawlerTask {
    private Task task;
    private Template template;

    public CrawlerTask(Task task, Template template) {
        this.task = task;
        this.template = template;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }
}
