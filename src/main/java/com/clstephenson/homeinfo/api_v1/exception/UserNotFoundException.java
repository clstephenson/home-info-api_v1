package com.clstephenson.homeinfo.api_v1.exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class UserNotFoundException extends ResourceNotFoundException {
    public UserNotFoundException(Long id) {
        super("User ID not found: " + id);
    }
}
