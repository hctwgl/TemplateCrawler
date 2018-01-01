package com.seveniu;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by seveniu on 1/1/18.
 * *
 */
@Service
public class TestService {
    @PostConstruct
    public String get() {
        return "hhhh";
    }
}
