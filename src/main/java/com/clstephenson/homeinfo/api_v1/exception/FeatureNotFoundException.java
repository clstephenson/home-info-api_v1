package com.clstephenson.homeinfo.api_v1.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class FeatureNotFoundException extends ResourceNotFoundException {

    Logger logger = LoggerFactory.getLogger(FeatureNotFoundException.class);

    public FeatureNotFoundException(Long id) {
        super("Feature ID [" + id + "] not found");
        logger.warn(getLocalizedMessage());
    }

    public FeatureNotFoundException(String message) {
        super(message);
        logger.warn(getLocalizedMessage());
    }

    public FeatureNotFoundException(String message, Throwable cause) {
        super(message, cause);
        logger.warn(getLocalizedMessage(), cause);
    }
}
