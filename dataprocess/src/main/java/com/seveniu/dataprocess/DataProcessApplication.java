package com.seveniu.dataprocess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by seveniu on 1/15/18.
 * *
 */
@SpringBootApplication
@ComponentScan("com.seveniu")
public class DataProcessApplication {
    public static void main(String[] args) {
        SpringApplication.run(DataProcessApplication.class, args);
    }
}
