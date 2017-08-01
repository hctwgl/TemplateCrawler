package com.seveniu.entity.task;

import com.seveniu.common.date.DateUtil;
import com.seveniu.entity.BaseServiceImpl;
import com.seveniu.entity.EntityStatus;
import com.seveniu.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by seveniu on 6/5/16.
 * TaskService
 */
@Service
public class TaskServiceImpl extends BaseServiceImpl<Task, Long> implements TaskService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final TaskRepository taskRepository;

    @Value("${execDueTask:false}")
    private boolean execDueTask;


    private ScheduledExecutorService getTaskScheduled;

    public Task getById(Long taskId) {
        return taskRepository.findOne(taskId);
    }

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        super(taskRepository);
        this.taskRepository = taskRepository;
    }

    @Override
    public void del(Integer user, Long id) {
        Task task = getById(id);
        task.setStatus(EntityStatus.DELETED);
        update(user, task);
    }

    @Override
    public int filterCountLike(Integer user, String[] fieldArray, Object[] valueArray, String likeField, String likeValue) {
        return 0;
    }

    @Override
    public List filterLimitLike(Integer user, int start, int pageSize, String orderField, String orderType, String[] fieldArray, Object[] valueArray, String likeField, String likeValue) {
        return null;
    }

    @Override
    public int filterCount(Integer user, String[] fieldArray, Object[] valueArray) {
        return 0;
    }

    @Override
    public List filterLimit(Integer user, int start, int pageSize, String orderField, String orderType, String[] fieldArray, Object[] valueArray) {
        return null;
    }

    @Override
    public int count(Integer user) {
        return 0;
    }

    @Override
    public List<Task> limit(Integer user, int i, int pagesize, String orderColumn, String orderType) {
        return null;
    }

    @Override
    public Page<Task> findSelfCreate(Pageable pageable) {
        return taskRepository.findByCreateUserId(SecurityUtil.getCurrentUserId(), pageable);
    }


    public List<Task> getDueTask() {
        return taskRepository.getDueTask(EntityStatus.ACTIVE, TaskRunStatus.TODO);
    }

    private static final int DEFAULT_TASK_PRIORITY = 1;
    private static final int DUE_TASK_PRIORITY = 0;

    /**
     * 定时查询过期任务
     */
    public void execDueTask() {
        if (execDueTask) {
            logger.info("exec due task");
            getTaskScheduled = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "exec-due-task-schedule");
                }
            });
            getTaskScheduled.scheduleWithFixedDelay(() -> {
                List<Task> taskList = getDueTask();
                if (taskList == null) {
                    logger.warn(" get task list is null");
                    return;
                }
                logger.info(" check due task count : {}", taskList.size());
                for (Task task : taskList) {
                    runTask(task.getId(), DUE_TASK_PRIORITY);
                    logger.info("run due task : {}", task);
                }

            }, 0, 1, TimeUnit.MINUTES);
        }
    }

    public TaskRunStatus run(int uid, Long id) {
        logger.info("user : {} run task : {}", uid, id);
        return runTask(id);
    }

    private final Object runLock = new Object();

    private TaskRunStatus runTask(Long id) {
        return runTask(id, DEFAULT_TASK_PRIORITY);
    }

    public String getTaskName(int uid, Long id) {
        return getById(id).getName();
    }


    /**
     * 执行任务
     *
     * @param id
     * @param priority
     * @return
     */
    private TaskRunStatus runTask(Long id, int priority) {
        synchronized (runLock) {
//            Task task = getById(id);
//            if (task == null) {
//                return TaskRunStatus.ERROR;
//            }
//            // 是否已禁用
//            if (task.getStatus() == EntityStatus.DELETED) {
//                return TaskRunStatus.ERROR;
//            }
//            // 是否已运行
//            if (task.getRunStatus() != TaskRunStatus.DONE) {
//                return task.getRunStatus();
//            }
//
//            TaskInfo taskInfo;
//            try {
//                // 转换对象
//                taskInfo = convertTask(task);
//                taskInfo.setPriority(priority);
//            } catch (Exception e) {
//                logger.error("convert task : {} error : {}", task.getId(), e.getMessage());
//                updateStatus(task.getId(), TaskRunStatus.ERROR);
//                return TaskRunStatus.ERROR;
//            }
//            try {
//                // 执行
//                CrawlClient.get().addTask(taskInfo);
//            } catch (Exception e) {
//                logger.error("run task : {} error ", task.getId(), e);
//                updateStatus(task.getId(), TaskRunStatus.ERROR);
//                return TaskRunStatus.ERROR;
//            }
//
//            // 执行结果处理
//            updateStatus(task.getId(), TaskRunStatus.WAIT);
            return TaskRunStatus.WAIT;
        }
    }

    /**
     * 新建任务
     */
    public Long insert(Integer uid, Task pojo) {
        int cycle = pojo.getCycle();
        Date currentDate = new Date();

        //Get the instant of current date in terms of milliseconds
        Instant now = currentDate.toInstant();
        ZoneId currentZone = ZoneId.systemDefault();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(now, currentZone);
        LocalDateTime next = localDateTime.plusSeconds(cycle);
        pojo.setNextRunTime(DateUtil.asUtilDate(next));

        pojo.setCreateUserId(uid.longValue());
        pojo.setStatus(EntityStatus.ACTIVE);
        pojo.setRunStatus(TaskRunStatus.DONE);
        pojo.setCreateUserId(uid.longValue());
        Task task = taskRepository.save(pojo);
        // 任务默认分配给创建任务者
//        taskAssignService.insertEditorId(task.getId(), uid);
        return task.getId();
    }

    @Override
    public void update(Integer accessor, Task task) {

    }

    @Override
    public void del(Integer accessor, List<Integer> idList) {

    }

    /**
     * 重置 所有任务 为未运行
     */
    private void resetAllTaskRunStatus() {
        taskRepository.resetRunningTaskToUnRun();
    }
//
//    private TaskInfo convertTask(Task task) {
//        TaskInfo taskInfo = new TaskInfo();
//        taskInfo.setId(String.valueOf(task.getId()));
//        taskInfo.setName(task.getName());
//        taskInfo.setJavascript(Javascript.findByValue(task.getJavascript()));
//        taskInfo.setProxy(Proxy.findByValue(task.getProxy()));
//        taskInfo.setTemplateId(String.valueOf(task.getTemplateId()));
//
//        Template template;
//        try {
//            template = templateClient.getTemplateById(task.getTemplateId());
//        } catch (IOException e) {
//            logger.warn("get template from server is error, template id : {} error:{}", task.getTemplateId(), e.getMessage());
//            throw new NullPointerException("template : " + task.getTemplateId() + "  is not exist");
//        }
//        if (template == null) {
//            logger.warn("get template from server is null, template id : {} ", task.getTemplateId());
//            throw new NullPointerException("template : " + task.getTemplateId() + "  is not exist");
//        }
//        taskInfo.setTemplateType(TemplateType.findByValue(template.getType()));
//        taskInfo.setTemplate(template.getPages());
//        String[] urls = task.getUrl().split("\n");
//        if (urls.length == 0) {
//            throw new IllegalArgumentException("task {} url is empty" + task.getId());
//        }
//        List<String> urlList = Arrays.asList(urls);
//        taskInfo.setUrls(urlList);
//        taskInfo.setThreadNum(task.getThreadNum());
//        return taskInfo;
//    }

    public void updateStatus(Long id, TaskRunStatus status) {
        Task task = getById(id);
        if (status == TaskRunStatus.DONE) {
            updateNextRuntimeAndRunStatus(task, TaskRunStatus.DONE);
        } else {
            task.setRunStatus(status);
            taskRepository.save(task);
        }

    }


    public void enable(Long id) {
        Task task = taskRepository.findOne(id);
        task.setStatus(EntityStatus.ACTIVE);
        taskRepository.save(task);
    }

    public void disable(Long id) {
        Task task = taskRepository.findOne(id);
        task.setStatus(EntityStatus.DELETED);
        taskRepository.save(task);
    }

    public List<Map<String, Object>> getNamesById(List<Integer> idList) {
//        return taskDao.getNameByIds(idList);
        return null;
    }

    public void updateNextRuntimeAndRunStatus(Task task, TaskRunStatus status) {
        Date date = new Date(System.currentTimeMillis() + (long) task.getCycle() * 1000);
        task.setNextRunTime(date);
        task.setRunStatus(status);
        taskRepository.save(task);
    }


}
