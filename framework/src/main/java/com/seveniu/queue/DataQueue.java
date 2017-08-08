package com.seveniu.queue;

import org.springframework.stereotype.Service;

/**
 * Created by seveniu on 8/8/17.
 * *
 */
@Service
public interface DataQueue {

    void push(String key, String data);

    String pop(String key);
}
