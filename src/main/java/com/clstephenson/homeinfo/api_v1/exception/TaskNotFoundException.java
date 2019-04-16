package com.clstephenson.homeinfo.api_v1.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class TaskNotFoundException extends ResourceNotFoundException {

    Logger logger = LoggerFactory.getLogger(TaskNotFoundException.class);

    public TaskNotFoundException(Long id) {
        super("Task ID [" + id + "] not found");
        logger.warn(getLocalizedMessage());
    }

    public TaskNotFoundException(String message) {
        super(message);
        logger.warn(getLocalizedMessage());
    }

    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
        logger.warn(getLocalizedMessage(), cause);
    }
}
