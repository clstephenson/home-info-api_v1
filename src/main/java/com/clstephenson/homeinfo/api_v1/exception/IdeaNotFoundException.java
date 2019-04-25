package com.clstephenson.homeinfo.api_v1.exception;

import com.clstephenson.homeinfo.api_v1.logging.MyLogger;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class IdeaNotFoundException extends ResourceNotFoundException {

    private final MyLogger LOGGER = new MyLogger(IdeaNotFoundException.class);

    public IdeaNotFoundException(Long id) {
        super("Idea ID [" + id + "] not found");
        LOGGER.log(getLocalizedMessage());
    }

    public IdeaNotFoundException(String message) {
        super(message);
        LOGGER.log(getLocalizedMessage());
    }

    public IdeaNotFoundException(String message, Throwable cause) {
        super(message, cause);
        LOGGER.log(getLocalizedMessage());
    }
}
