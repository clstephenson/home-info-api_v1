package com.clstephenson.homeinfo.api_v1.configproperty;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cloud.aws")
public class AwsProperties {

    private String credentialsAccessKey;
    private String credentialsSecretKey;
    private String region;
    private String bucketName;

    public String getCredentialsAccessKey() {
        return credentialsAccessKey;
    }

    public void setCredentialsAccessKey(String credentialsAccessKey) {
        this.credentialsAccessKey = credentialsAccessKey;
    }

    public String getCredentialsSecretKey() {
        return credentialsSecretKey;
    }

    public void setCredentialsSecretKey(String credentialsSecretKey) {
        this.credentialsSecretKey = credentialsSecretKey;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
