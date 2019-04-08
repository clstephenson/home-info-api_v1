package com.clstephenson.homeinfo.api_v1.exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class PropertyNotFoundException extends ResourceNotFoundException {

    public PropertyNotFoundException(Long id) {
        super("Property ID not found: " + id);
    }
}
