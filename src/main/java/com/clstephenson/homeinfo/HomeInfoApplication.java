package com.clstephenson.homeinfo;

import com.clstephenson.homeinfo.api.configproperty.AwsProperties;
import com.clstephenson.homeinfo.api.configproperty.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class,
        AwsProperties.class
})
public class HomeInfoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeInfoApplication.class, args);
    }

}
