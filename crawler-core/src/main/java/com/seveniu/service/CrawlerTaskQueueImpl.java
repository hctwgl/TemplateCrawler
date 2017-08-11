package com.seveniu.service;

import com.seveniu.common.json.Json;
import com.seveniu.entity.CrawlerTask;
import com.seveniu.entity.task.Task;
import com.seveniu.entity.template.Template;
import com.seveniu.queue.DataQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by seveniu on 10/11/16.
 * *
 */
@Service
public class CrawlerTaskQueueImpl implements CrawlerTaskService {
    @Autowired
    DataQueue dataQueue;

    private static final String CRAWLER_TASK_QUEUE_NAME = "crawler-task-queue";


    @Override
    public void add(Task task, Template template) {
        dataQueue.push(CRAWLER_TASK_QUEUE_NAME, Json.toJson(new CrawlerTask(task, template)));
    }

    @Override
    public CrawlerTask take() {
        String data = dataQueue.pop(CRAWLER_TASK_QUEUE_NAME);
        if (data == null) {
            return null;
        }
        return Json.toObject(data, CrawlerTask.class);
    }
}
