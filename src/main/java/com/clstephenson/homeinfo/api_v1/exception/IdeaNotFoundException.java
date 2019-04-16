package com.clstephenson.homeinfo.api_v1.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class IdeaNotFoundException extends ResourceNotFoundException {

    Logger logger = LoggerFactory.getLogger(IdeaNotFoundException.class);

    public IdeaNotFoundException(Long id) {
        super("Idea ID [" + id + "] not found");
        logger.warn(getLocalizedMessage());
    }

    public IdeaNotFoundException(String message) {
        super(message);
        logger.warn(getLocalizedMessage());
    }

    public IdeaNotFoundException(String message, Throwable cause) {
        super(message, cause);
        logger.warn(getLocalizedMessage(), cause);
    }
}
