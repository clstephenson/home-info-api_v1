package com.clstephenson.homeinfo.api_v1.exception;

import com.clstephenson.homeinfo.api_v1.logging.MyLogger;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class LocationNotFoundException extends ResourceNotFoundException {

    private final MyLogger LOGGER = new MyLogger(LocationNotFoundException.class);

    public LocationNotFoundException(Long id) {
        super("Location ID [" + id + "] not found");
        LOGGER.log(getLocalizedMessage());
    }

    public LocationNotFoundException(String message) {
        super(message);
        LOGGER.log(getLocalizedMessage());
    }

    public LocationNotFoundException(String message, Throwable cause) {
        super(message, cause);
        LOGGER.log(getLocalizedMessage());
    }
}
