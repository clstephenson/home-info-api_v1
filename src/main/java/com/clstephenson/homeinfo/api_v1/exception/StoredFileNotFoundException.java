package com.clstephenson.homeinfo.api_v1.exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

//todo pass throwable cause to each exception in a constructor
public class StoredFileNotFoundException extends ResourceNotFoundException {

    public StoredFileNotFoundException(Long id) {
        super("File ID not found: " + id);
    }

    public StoredFileNotFoundException(String message) {
        super(message);
    }

    public StoredFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
