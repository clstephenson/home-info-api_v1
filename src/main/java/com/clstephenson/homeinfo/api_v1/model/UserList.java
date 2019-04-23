package com.clstephenson.homeinfo.api_v1.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserList implements Serializable {

    private List<User> properties;

    public UserList() {
        properties = new ArrayList<>();
    }

    public UserList(List<User> properties) {
        this.properties = properties;
    }

    public List<User> getProperties() {
        return properties;
    }

    public void setProperties(List<User> properties) {
        this.properties = properties;
    }
}
