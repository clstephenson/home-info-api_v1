package com.clstephenson.homeinfo.api_v1.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

//todo pass throwable cause to each exception in a constructor
public class StoredFileNotFoundException extends ResourceNotFoundException {

    Logger logger = LoggerFactory.getLogger(StoredFileNotFoundException.class);

    public StoredFileNotFoundException(Long id) {
        super("StoredFile ID [" + id + "] not found");
        logger.warn(getLocalizedMessage());
    }

    public StoredFileNotFoundException(String message) {
        super(message);
        logger.warn(getLocalizedMessage());
    }

    public StoredFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
        logger.warn(getLocalizedMessage(), cause);
    }
}
