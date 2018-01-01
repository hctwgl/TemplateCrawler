package com.seveniu.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(value = {"com.seveniu"})
public class AppCrawler {
    private static final Logger logger = LoggerFactory.getLogger(AppCrawler.class);
    private static ApplicationContext ctx;
    @Autowired
    private Environment environment;

    public static ApplicationContext getCtx() {
        return ctx;
    }


    private void init() {

        logger.debug("profile:{}", environment.getProperty("spring.profiles.active"));
        logger.debug("env:{}", Arrays.toString(environment.getActiveProfiles()));
    }

    public static void main(String[] args) throws Exception {
        ctx = SpringApplication.run(AppCrawler.class, args);
        AppCrawler appCrawl = ctx.getBean(AppCrawler.class);
        appCrawl.init();
    }
}
