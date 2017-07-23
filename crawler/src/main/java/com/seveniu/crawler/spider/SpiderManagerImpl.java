package com.seveniu.crawler.spider;

import com.seveniu.entity.task.Task;
import com.seveniu.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

/**
 * Created by seveniu on 7/22/17.
 * *
 */
@Service
public class SpiderManagerImpl implements SpiderManager {
    private Logger log = LoggerFactory.getLogger(this.getClass());
    private ExecutorService executor;
    private LinkedBlockingQueue<RunningSpider> allRunningSpider = new LinkedBlockingQueue<>();
    private final Semaphore semaphore;

    public SpiderManagerImpl(@Value("${crawler.thread.max:400}") int sameTimeThreadMaxNum) {
        this.semaphore = new Semaphore(sameTimeThreadMaxNum);
        this.executor = CrawlerThreadPoolFactory.getRunningSpiderThreadPool();
    }

    @Override
    public RunningSpider runSpider(Task task) {
        if (!Objects.equals(SecurityUtil.getCurrentUserId(), task.getCreateUserId())) {
            throw new RuntimeException("user : " + SecurityUtil.getCurrentUserId() + " can't run task " + task.getId() + " because task create by : " + task.getCreateUserId());
        }
        //TODO: 限制一个用户最多使用的线程数


        //TODO: set pageProcesser
        RunningSpider runningSpider = new RunningSpider(null, task);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("cur available thread",semaphore.availablePermits());
                    semaphore.acquire();
                    runningSpider.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }
        });

        //TODO:
        return runningSpider;
    }

    @Override
    public Collection<RunningSpider> getRunningSpiders() {
        return allRunningSpider;
    }

    @Override
    public Collection<RunningSpider> getRunningSpidersByUserId(Long userId) {
        return allRunningSpider.stream().filter(v -> v.getTask().getCreateUserId().equals(userId)).collect(Collectors.toList());
    }
}
