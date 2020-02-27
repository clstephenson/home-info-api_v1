package com.clstephenson.homeinfo.api.exception;

import com.clstephenson.homeinfo.logging.MyLogger;
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
