package com.seveniu.crawler.spider;

import com.seveniu.security.SecurityUtil;
import com.seveniu.crawler.spider.pageProcessor.TemplatePageProcessor;
import com.seveniu.entity.task.Task;
import com.seveniu.entity.template.Template;
import com.seveniu.entity.template.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.processor.PageProcessor;

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
    @Autowired
    TemplateService templateService;

    public SpiderManagerImpl(@Value("${crawler.thread.max:400}") int sameTimeThreadMaxNum) {
        this.semaphore = new Semaphore(sameTimeThreadMaxNum);
        this.executor = CrawlerThreadPoolFactory.getTaskThreadPool();
    }

    @Override
    public RunningSpider runSpider(Task task) {
        if (!Objects.equals(SecurityUtil.getCurrentUserId(), task.getCreateUserId())) {
            throw new RuntimeException("user : " + SecurityUtil.getCurrentUserId() + " can't run task " + task.getId() + " because task create by : " + task.getCreateUserId());
        }
        //TODO: 限制一个用户最多使用的线程数, 临时固定为 50 个


        // 获取 template 构造 pageProcess
        PageProcessor pageProcessor = generatePageProcessor(task);
        if (pageProcessor == null) {
            throw new RuntimeException("template page processor generate error");
        }

        //TODO: set pageProcessor
        RunningSpider runningSpider = new RunningSpider(pageProcessor, task);

        executor.execute(() -> {
            try {
                log.info("cur available thread", semaphore.availablePermits());
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

    // 获取 template 构造 pageProcess
    private TemplatePageProcessor generatePageProcessor(Task task) {
        Template template = templateService.findOne(task.getTemplateId());
        if (template == null) {
            return null;
        }
        return new TemplatePageProcessor(template.getTemplateStructure());
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
