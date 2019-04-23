package com.clstephenson.homeinfo.api_v1.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserList implements Serializable {

    private List<User> users;

    public UserList() {
        users = new ArrayList<>();
    }

    public UserList(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
