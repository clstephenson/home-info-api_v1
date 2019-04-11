package com.clstephenson.homeinfo.api_v1.exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class StoredFileNotFoundException extends ResourceNotFoundException {

    public StoredFileNotFoundException(Long id) {
        super("File ID not found: " + id);
    }
}
