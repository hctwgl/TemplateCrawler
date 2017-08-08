package com.seveniu.entity.task;


import com.seveniu.entity.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by seveniu on 7/15/17.
 * *
 */
public interface TaskService extends BaseService<Task, Long> {
    void startMonitorDueTaskAndRun();

    void updateStatus(Long id, TaskRunStatus status);

    Task getById(Long taskId);

    TaskRunStatus run(int accessor, Long id);

    List<Map<String, Object>> getNamesById(List<Integer> idList);

    String getTaskName(int accessor, Long taskId);

    void disable(Long id);

    void enable(Long id);

    Long insert(Integer accessor, Task task);

    void update(Integer accessor, Task task);

    void del(Integer accessor, List<Integer> idList);

    void del(Integer accessor, Long id);

    int filterCountLike(Integer user, String[] fieldArray, Object[] valueArray, String likeField, String likeValue);

    List filterLimitLike(Integer user, int start, int pageSize, String orderField, String orderType, String[] fieldArray, Object[] valueArray, String likeField, String likeValue);

    int filterCount(Integer user, String[] fieldArray, Object[] valueArray);

    List filterLimit(Integer user, int start, int pageSize, String orderField, String orderType, String[] fieldArray, Object[] valueArray);

    int count(Integer user);

    List<Task> limit(Integer user, int i, int pagesize, String orderColumn, String orderType);

    Page<Task> findSelfCreate(Pageable pageable);

}
