package com.commic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.commic.v1.mapper")
public class CommicApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommicApplication.class, args);
    }
}
