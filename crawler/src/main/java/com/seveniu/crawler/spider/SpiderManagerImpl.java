package com.seveniu.crawler.spider;

import com.seveniu.crawler.spider.pageProcessor.TemplatePageProcessor;
import com.seveniu.crawler.spider.pipeline.ConsolePipeline;
import com.seveniu.entity.CrawlerTask;
import com.seveniu.entity.template.Template;
import com.seveniu.service.CrawlerTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;

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
    private LinkedBlockingQueue<RunningSpider> allRunningSpider = new LinkedBlockingQueue<>();
    private final Semaphore semaphore;
    private final CrawlerTaskService crawlerTaskService;
    private List<Pipeline> pipelines;

    @Autowired
    public SpiderManagerImpl(@Value("${crawler.thread.max:400}") int sameTimeThreadMaxNum, CrawlerTaskService crawlerTaskService, @Autowired(required = false) List<Pipeline> pipelines) {
        this.semaphore = new Semaphore(sameTimeThreadMaxNum);
        this.executor = CrawlerThreadPoolFactory.getTaskThreadPool();
        this.checkTaskFromTaskQueue();
        this.crawlerTaskService = crawlerTaskService;
        this.pipelines = pipelines;
    }

    @Override
    public RunningSpider runSpider(CrawlerTask task) {
        //TODO: 限制一个用户最多使用的线程数, 临时固定为 50 个


        Scheduler scheduler;
        // 获取 template 构造 pageProcess
        Template template = task.getTemplate();
        if (template.getContentStartLayer() + 1 == template.getPageStructure().size()) {
            scheduler = new NormalScheduler();
        } else {
            scheduler = new QueueScheduler();
        }
        PageProcessor pageProcessor = new TemplatePageProcessor(task.getTemplate());

        RunningSpider runningSpider = new RunningSpider(pageProcessor, task);
        runningSpider.setScheduler(scheduler);
        if (pipelines == null || pipelines.size() == 0) {
            runningSpider.addPipeline(new ConsolePipeline());
        }

        executor.execute(() -> {
            try {
                logger.info("cur available thread", semaphore.availablePermits());
                semaphore.acquire();
                runningSpider.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        });

        return runningSpider;
    }

    @Override
    public Collection<RunningSpider> getRunningSpiders() {
        return allRunningSpider;
    }

    @Override
    public Collection<RunningSpider> getRunningSpidersByUserId(Long userId) {
        return allRunningSpider.stream().filter(v -> v.getTask().getTask().getCreateUserId().equals(userId)).collect(Collectors.toList());
    }

    private ScheduledExecutorService monitorScheduled;

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
                    CrawlerTask crawlerTask = crawlerTaskService.take();
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
