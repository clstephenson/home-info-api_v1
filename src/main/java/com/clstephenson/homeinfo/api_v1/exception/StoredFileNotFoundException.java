package com.clstephenson.homeinfo.api_v1.exception;

import com.clstephenson.homeinfo.api_v1.logging.MyLogger;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

//todo pass throwable cause to each exception in a constructor
public class StoredFileNotFoundException extends ResourceNotFoundException {

    private final MyLogger LOGGER = new MyLogger(StoredFileNotFoundException.class);

    public StoredFileNotFoundException(Long id) {
        super("StoredFile ID [" + id + "] not found");
        LOGGER.log(getLocalizedMessage());
    }

    public StoredFileNotFoundException(String message) {
        super(message);
        LOGGER.log(getLocalizedMessage());
    }

    public StoredFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
        LOGGER.log(getLocalizedMessage());
    }
}
