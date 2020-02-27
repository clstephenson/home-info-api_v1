package com.clstephenson.homeinfo.api.configproperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {

    private String uploadDirectory;
    private String localUploadDirectory;

    public String getUploadDirectory() {
        return uploadDirectory;
    }

    public void setUploadDirectory(String uploadDirectory) {
        this.uploadDirectory = uploadDirectory;
    }

    public String getLocalUploadDirectory() {
        return localUploadDirectory;
    }

    public void setLocalUploadDirectory(String localUploadDirectory) {
        this.localUploadDirectory = localUploadDirectory;
    }
}
