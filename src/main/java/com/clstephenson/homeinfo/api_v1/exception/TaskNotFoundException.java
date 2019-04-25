package com.clstephenson.homeinfo.api_v1.exception;

import com.clstephenson.homeinfo.api_v1.logging.MyLogger;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class TaskNotFoundException extends ResourceNotFoundException {

    private final MyLogger LOGGER = new MyLogger(TaskNotFoundException.class);

    public TaskNotFoundException(Long id) {
        super("Task ID [" + id + "] not found");
        LOGGER.log(getLocalizedMessage());
    }

    public TaskNotFoundException(String message) {
        super(message);
        LOGGER.log(getLocalizedMessage());
    }

    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
        LOGGER.log(getLocalizedMessage());
    }
}
