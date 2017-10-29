package com.seveniu.manage.rest;

import com.seveniu.entity.task.Task;
import com.seveniu.entity.task.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by seveniu on 7/27/17.
 * *
 */
@RestController
@RequestMapping("/api/task")
public class TaskApi extends BaseApi<Task, Long> {
    private final TaskService taskService;

    @Autowired
    public TaskApi(TaskService taskService) {
        super(taskService);
        this.taskService = taskService;
    }

}
