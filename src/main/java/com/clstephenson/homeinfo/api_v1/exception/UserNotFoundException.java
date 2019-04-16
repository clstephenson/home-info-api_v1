package com.clstephenson.homeinfo.api_v1.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {

    Logger logger = LoggerFactory.getLogger(UserNotFoundException.class);

    public UserNotFoundException(Long id) {
        super("User ID [" + id + "] not found");
        logger.warn(getLocalizedMessage());
    }

    public UserNotFoundException(String message) {
        super(message);
        logger.warn(getLocalizedMessage());
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
        logger.warn(getLocalizedMessage(), cause);
    }
}
