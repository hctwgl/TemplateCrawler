package com.seveniu.common.concurrent;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by seveniu on 7/13/16.
 * CountDownExecutor
 */
public class CountDownExecutor<T extends Runnable> {
    private Executor threadPoolExecutor;
    private CountDownLatch countDownLatch;
    private List<T> tasks;

    public CountDownExecutor(Executor threadPoolExecutor, List<T> tasks) {
        this.threadPoolExecutor = threadPoolExecutor;
        this.tasks = tasks;
        this.countDownLatch = new CountDownLatch(tasks.size());
    }

    public void execute(long time, TimeUnit timeUnit) throws InterruptedException {
        for (Runnable task : tasks) {
            execute(task);
        }
        countDownLatch.await(time, timeUnit);
    }

    private void execute(Runnable runnable) {
        this.threadPoolExecutor.execute(new CountDownRunnable(runnable, countDownLatch));
    }

    private class CountDownRunnable implements Runnable {
        private Runnable runnable;
        private CountDownLatch countDownLatch;

        CountDownRunnable(Runnable runnable, CountDownLatch countDownLatch) {
            this.runnable = runnable;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            runnable.run();
            countDownLatch.countDown();
        }
    }
}
