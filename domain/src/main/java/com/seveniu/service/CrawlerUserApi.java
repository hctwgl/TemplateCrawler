package com.seveniu.service;

import com.seveniu.entity.task.CrawlerUserInfo;
import com.seveniu.entity.task.Task;
import com.seveniu.entity.task.TaskStatisticOld;

import java.util.List;

/**
 * Created by seveniu on 1/8/17.
 * *
 */
public interface CrawlerUserApi {

    List<TaskStatisticOld> getRunningTasks();

    CrawlerUserInfo getResourceInfo();

    void addTask(Task taskInfo);

}
