package com.seveniu.crawler.spider;

import com.seveniu.crawler.spider.pageProcessor.TemplatePageProcessor;
import com.seveniu.entity.CrawlerTask;
import com.seveniu.service.CrawlerTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Collection;
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

    @Autowired
    public SpiderManagerImpl(@Value("${crawler.thread.max:400}") int sameTimeThreadMaxNum, CrawlerTaskService crawlerTaskService) {
        this.semaphore = new Semaphore(sameTimeThreadMaxNum);
        this.executor = CrawlerThreadPoolFactory.getTaskThreadPool();
        this.checkTaskFromTaskQueue();
        this.crawlerTaskService = crawlerTaskService;
    }

    @Override
    public RunningSpider runSpider(CrawlerTask task) {
        //TODO: 限制一个用户最多使用的线程数, 临时固定为 50 个


        // 获取 template 构造 pageProcess
        PageProcessor pageProcessor = new TemplatePageProcessor(task.getTemplate());

        //TODO: set pageProcessor
        RunningSpider runningSpider = new RunningSpider(pageProcessor, task);

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
