package com.clstephenson.homeinfo.api_v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ApiV1Application {

    public static void main(String[] args) {
        SpringApplication.run(ApiV1Application.class, args);
    }

}