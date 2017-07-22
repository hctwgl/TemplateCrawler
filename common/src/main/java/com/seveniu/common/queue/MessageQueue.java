package com.seveniu.common.queue;

/**
 * Created by seveniu on 7/2/17.
 * *
 */
public interface MessageQueue {


    void queue(String queueKey, Object message);

    <T> T dequeue(String queueName, Class<T> clazz);

    void publish(String topicName, Object message);

}
