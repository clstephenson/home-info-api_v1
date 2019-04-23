package com.clstephenson.homeinfo.api_v1.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoredFileList implements Serializable {

    private List<StoredFile> properties;

    public StoredFileList() {
        properties = new ArrayList<>();
    }

    public StoredFileList(List<StoredFile> properties) {
        this.properties = properties;
    }

    public List<StoredFile> getProperties() {
        return properties;
    }

    public void setProperties(List<StoredFile> properties) {
        this.properties = properties;
    }
}
