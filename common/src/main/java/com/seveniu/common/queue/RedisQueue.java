package com.seveniu.common.queue;

import com.seveniu.common.json.Json;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class RedisQueue implements MessageQueue {

    private JedisPool jedisPool;

    RedisQueue(String host, int port) {
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();

        this.jedisPool = new JedisPool(genericObjectPoolConfig, host, port);
    }

    @Override
    public void queue(String queueName, Object message) {
        String queueKey = getQueueKey(queueName);
        String stringMsg = Json.toJson(message);

        try (Jedis jedis = jedisPool.getResource()) {
            jedis.rpush(queueKey, stringMsg);
        }
    }


    @Override
    public <T> T dequeue(String queueName, Class<T> clazz) {
        T message;
        String queueKey = getQueueKey(queueName);
        try (Jedis jedis = jedisPool.getResource()) {
            String stringMsg = jedis.lpop(queueKey);

            if (String.class.equals(clazz)) {
                message = Json.toObject(stringMsg, clazz);
            } else {
                message = Json.toObject(stringMsg, clazz);
            }
            return message;
        }
    }

    @Override
    public void publish(String topicName, Object message) {
        String stringMsg;
        if (message instanceof String) {
            stringMsg = (String) message;
        } else {
            stringMsg = Json.toJson(message);
        }
        jedisPool.getResource().publish(topicName, stringMsg);
    }


    public String getQueueKey(String queueName) {
        String queueKey = queueName;
        if (!queueName.startsWith(BaseMessagingService.QUEUE_NAME_PREFIX)) {
            queueKey = BaseMessagingService.QUEUE_NAME_PREFIX + queueName;
        }
        return queueKey;
    }

}
