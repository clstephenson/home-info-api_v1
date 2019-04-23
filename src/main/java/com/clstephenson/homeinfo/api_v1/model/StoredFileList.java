package com.clstephenson.homeinfo.api_v1.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StoredFileList implements Serializable {

    private List<StoredFile> storedFiles;

    public StoredFileList() {
        storedFiles = new ArrayList<>();
    }

    public StoredFileList(List<StoredFile> storedFiles) {
        this.storedFiles = storedFiles;
    }

    public List<StoredFile> getStoredFiles() {
        return storedFiles;
    }

    public void setStoredFiles(List<StoredFile> storedFiles) {
        this.storedFiles = storedFiles;
    }
}
