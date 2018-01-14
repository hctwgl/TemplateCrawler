package com.seveniu.crawler.spider;

import com.seveniu.crawler.spider.pageProcessor.HasContextPageProcessor;
import com.seveniu.crawler.spider.pipeline.ConsolePipeline;
import com.seveniu.entity.CrawlerTask;
import com.seveniu.entity.task.TaskRunStatus;
import com.seveniu.entity.task.TaskService;
import com.seveniu.entity.task.TaskStatistic;
import com.seveniu.entity.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.Scheduler;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by seveniu on 7/22/17.
 * *
 */
@Service
public class SpiderManagerImpl implements SpiderManager {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ExecutorService executor;
    private LinkedBlockingQueue<TemplateSpider> allTemplateSpider = new LinkedBlockingQueue<>();
    private final Semaphore semaphore;
    private final TaskService taskService;
    private List<Pipeline> pipelines;

    @Autowired
    public SpiderManagerImpl(@Value("${crawler.thread.max:400}") int sameTimeThreadMaxNum, @Autowired TaskService taskService, @Autowired(required = false) List<Pipeline> pipelines) {
        this.semaphore = new Semaphore(sameTimeThreadMaxNum);
        this.executor = CrawlerThreadPoolFactory.getTaskThreadPool();
        this.taskService = taskService;
        this.pipelines = pipelines;
    }

    @Override
    public TemplateSpider runSpider(CrawlerTask task) {
        //TODO: 限制一个用户最多使用的线程数, 临时固定为 50 个


        Scheduler scheduler;
        // 获取 template 构造 pageProcess
        Template template = task.getTemplate();
//        if (template.getContentStartLayer() + 1 == template.getPageStructure().size()) {
//            scheduler = new NormalScheduler();
//        } else {
//            scheduler = new QueueScheduler();
//        }
        PageProcessor pageProcessor = new HasContextPageProcessor(task.getTemplate());

        TemplateSpider templateSpider = new TemplateSpider(pageProcessor, task);
//        templateSpider.setScheduler(scheduler);
        if (pipelines == null || pipelines.size() == 0) {
            templateSpider.addPipeline(new ConsolePipeline());
        } else {
            templateSpider.setPipelines(pipelines);
        }
        TaskStatistic taskStatistic = taskService.createTaskStatistic(task.getTask());
        task.setTaskStatistic(taskStatistic);
        templateSpider.setCloseListener(() -> {
            taskService.saveTaskStatistic(taskStatistic);
            task.getTask().setRunStatus(TaskRunStatus.DONE);
            taskService.save(task.getTask());
        });

        executor.execute(() -> {
            try {
                logger.info("cur available thread", semaphore.availablePermits());
                semaphore.acquire();
                templateSpider.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        });

        return templateSpider;
    }

    @Override
    public Collection<TemplateSpider> getRunningSpiders() {
        return allTemplateSpider;
    }

    @Override
    public Collection<TemplateSpider> getRunningSpidersByUserId(Long userId) {
        return allTemplateSpider.stream().filter(v -> v.getCrawlerTask().getTask().getCreateUserId().equals(userId)).collect(Collectors.toList());
    }

    private ScheduledExecutorService monitorScheduled;

    @PostConstruct
    private void checkTaskFromTaskQueue() {

        logger.info("exec due task");
        monitorScheduled = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "check-task-schedule");
            }
        });
        monitorScheduled.scheduleWithFixedDelay(() -> {
            while (true) {
                try {
                    CrawlerTask crawlerTask = taskService.getOneDueTaskAndRun();
                    if (crawlerTask == null) {
                        break;
                    }
                    this.runSpider(crawlerTask);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            logger.info("no task ... ");
        }, 0, 30, TimeUnit.SECONDS);
    }
}
