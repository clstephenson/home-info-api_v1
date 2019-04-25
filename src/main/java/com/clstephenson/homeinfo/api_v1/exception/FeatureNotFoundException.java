package com.clstephenson.homeinfo.api_v1.exception;

import com.clstephenson.homeinfo.api_v1.logging.MyLogger;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class FeatureNotFoundException extends ResourceNotFoundException {

    private final MyLogger LOGGER = new MyLogger(FeatureNotFoundException.class);

    public FeatureNotFoundException(Long id) {
        super("Feature ID [" + id + "] not found");
        LOGGER.log(getLocalizedMessage());
    }

    public FeatureNotFoundException(String message) {
        super(message);
        LOGGER.log(getLocalizedMessage());
    }

    public FeatureNotFoundException(String message, Throwable cause) {
        super(message, cause);
        LOGGER.log(getLocalizedMessage());
    }
}
