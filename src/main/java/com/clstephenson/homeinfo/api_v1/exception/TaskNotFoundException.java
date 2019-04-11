package com.clstephenson.homeinfo.api_v1.exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class TaskNotFoundException extends ResourceNotFoundException {

    public TaskNotFoundException(Long id) {
        super("Task ID not found: " + id);
    }
}
