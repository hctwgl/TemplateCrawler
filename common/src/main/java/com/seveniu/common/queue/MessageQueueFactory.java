package com.seveniu.common.queue;

/**
 * Created by seveniu on 7/3/17.
 * *
 */
public class MessageQueueFactory {
    public static MessageQueue redisQueue(String host, int port) {
        return new RedisQueue(host, port);
    }

    public static MessageQueue mysqlQueue(String url, String username, String password) {
        return new MysqlQueue(url, username, password);
    }

}
