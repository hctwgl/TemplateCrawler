package com.seveniu.crawler.spider;

import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.Scheduler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by seveniu on 8/20/17.
 * *
 */
public class NormalScheduler implements Scheduler {

    private BlockingQueue<Request> queue = new LinkedBlockingQueue<>();

    @Override
    public void push(Request request, Task task) {
        queue.add(request);
    }

    @Override
    public Request poll(Task task) {
        return queue.poll();
    }
}
