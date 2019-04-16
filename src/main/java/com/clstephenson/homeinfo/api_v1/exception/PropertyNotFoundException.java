package com.clstephenson.homeinfo.api_v1.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class PropertyNotFoundException extends ResourceNotFoundException {

    Logger logger = LoggerFactory.getLogger(PropertyNotFoundException.class);

    public PropertyNotFoundException(Long id) {
        super("Property ID [" + id + "] not found");
        logger.warn(getLocalizedMessage());
    }

    public PropertyNotFoundException(String message) {
        super(message);
        logger.warn(getLocalizedMessage());
    }

    public PropertyNotFoundException(String message, Throwable cause) {
        super(message, cause);
        logger.warn(getLocalizedMessage(), cause);
    }
}
