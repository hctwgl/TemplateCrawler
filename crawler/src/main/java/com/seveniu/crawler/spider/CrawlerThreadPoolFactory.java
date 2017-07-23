package com.seveniu.crawler.spider;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by seveniu on 7/23/17.
 * *
 */
public class CrawlerThreadPoolFactory {
//    public static <T> ThreadPoolExecutor getThreadPool(int threadNum) {
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadNum,threadNum,0L, TimeUnit.MILLISECONDS,
//                new SynchronousQueue<T>(), new ThreadFactory() {
//        AtomicInteger count = new AtomicInteger();
//        @Override
//        public Thread newThread(Runnable r) {
//            Thread thread = new Thread(r);
//            thread.setName("crawler-thread-" + count.getAndIncrement());
//            return thread;
//        }
//    }, )
//    }

    public static ThreadPoolExecutor getRunningSpiderThreadPool() {

        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new ThreadFactory() {
                    AtomicInteger count = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("crawler-thread-" + count.getAndIncrement());
                        return thread;
                    }
                }
        );
    }
}
