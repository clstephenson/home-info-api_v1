package com.clstephenson.homeinfo.api_v1;

import com.clstephenson.homeinfo.api_v1.configproperty.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class ApiV1Application {

    public static void main(String[] args) {
        SpringApplication.run(ApiV1Application.class, args);
    }

}
