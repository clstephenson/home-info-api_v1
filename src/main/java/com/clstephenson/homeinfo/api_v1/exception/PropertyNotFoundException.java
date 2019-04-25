package com.clstephenson.homeinfo.api_v1.exception;

import com.clstephenson.homeinfo.api_v1.logging.MyLogger;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class PropertyNotFoundException extends ResourceNotFoundException {

    private final MyLogger LOGGER = new MyLogger(PropertyNotFoundException.class);

    public PropertyNotFoundException(Long id) {
        super("Property ID [" + id + "] not found");
        LOGGER.log(getLocalizedMessage());
    }

    public PropertyNotFoundException(String message) {
        super(message);
        LOGGER.log(getLocalizedMessage());
    }

    public PropertyNotFoundException(String message, Throwable cause) {
        super(message, cause);
        LOGGER.log(getLocalizedMessage());
    }
}
