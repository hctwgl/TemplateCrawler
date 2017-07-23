package com.seveniu.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan("com.seveniu")
public class AppSpider {
    private static final Logger logger = LoggerFactory.getLogger(AppSpider.class);
    private static ApplicationContext ctx;
    @Autowired
    private Environment environment;

    public static ApplicationContext getCtx() {
        return ctx;
    }

    @Value("${server.port}")
    int webPort;

    private void init() {

        logger.debug("profile:{}", environment.getProperty("spring.profiles.active"));
        logger.debug("env:{}", Arrays.toString(environment.getActiveProfiles()));
        logger.info("start web port : {}", webPort);
    }

    public static void main(String[] args) throws Exception {
        ctx = SpringApplication.run(AppSpider.class, args);
        AppSpider appCrawl = ctx.getBean(AppSpider.class);
        appCrawl.init();

    }
}
