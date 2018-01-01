package com.seveniu.service;

import com.seveniu.entity.task.CrawlerUserInfo;
import com.seveniu.entity.task.Task;
import com.seveniu.entity.task.TaskStatisticOld;

import java.util.List;

/**
 * Created by seveniu on 1/8/17.
 * *
 */
public interface CrawlerServer {
    String reg(String name, String host);

    List<TaskStatisticOld> getRunningTasks(String uuid);

    CrawlerUserInfo getResourceInfo(String uuid);

    void addTask(String uuid, Task taskInfo);

}
