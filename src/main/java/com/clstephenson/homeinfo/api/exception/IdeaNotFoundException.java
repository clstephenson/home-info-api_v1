package com.clstephenson.homeinfo.api.exception;

import com.clstephenson.homeinfo.logging.MyLogger;
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
