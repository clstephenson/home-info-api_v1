package com.clstephenson.homeinfo.api_v1.exception;

import com.clstephenson.homeinfo.api_v1.logging.MyLogger;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {

    private final MyLogger LOGGER = new MyLogger(UserNotFoundException.class);

    public UserNotFoundException(Long id) {
        super("User ID [" + id + "] not found");
        LOGGER.log(getLocalizedMessage());
    }

    public UserNotFoundException(String message) {
        super(message);
        LOGGER.log(getLocalizedMessage());
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
        LOGGER.log(getLocalizedMessage());
    }
}
