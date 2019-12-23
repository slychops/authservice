package com.sandbox.playground.blank_spring_projects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(Config.class)
public class BlankSpringProjectsApplication {

    private Config config;

    @Autowired
    public BlankSpringProjectsApplication(Config config) {
        this.config = config;
    }

    public static void main(String[] args) {
        SpringApplication.run(BlankSpringProjectsApplication.class, args);
    }
}
