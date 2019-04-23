package com.clstephenson.homeinfo.api_v1.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskList implements Serializable {

    private List<Task> properties;

    public TaskList() {
        properties = new ArrayList<>();
    }

    public TaskList(List<Task> properties) {
        this.properties = properties;
    }

    public List<Task> getProperties() {
        return properties;
    }

    public void setProperties(List<Task> properties) {
        this.properties = properties;
    }
}
