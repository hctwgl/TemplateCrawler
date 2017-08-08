package com.seveniu.service;

import com.seveniu.entity.CrawlerTask;
import com.seveniu.entity.task.Task;
import com.seveniu.entity.template.Template;

/**
 * Created by seveniu on 8/8/17.
 * *
 */
public interface CrawlerTaskService {
    void add(Task task, Template template);

    CrawlerTask take();
}
