package com.seveniu.queue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by seveniu on 8/8/17.
 * *
 */
@Service
@ConditionalOnExpression("${queue.redis.enable}")
public class RedisQueue implements DataQueue {

    private JedisPool pool;

    public RedisQueue(@Value("${queue.redis.enable:false}") boolean enable, @Value("${queue.redis.host:}") String host, @Value("${queue.redis.port:0}") int port) {
        if (enable) {
            this.pool = new JedisPool(host, port);
        }
    }

    @Override
    public void push(String key, String data) {
        try (Jedis jedis = this.pool.getResource()) {
            jedis.rpush(key, data);
        }
    }

    @Override
    public String pop(String key) {
        try (Jedis jedis = this.pool.getResource()) {
            return jedis.lpop(key);
        }
    }

}
