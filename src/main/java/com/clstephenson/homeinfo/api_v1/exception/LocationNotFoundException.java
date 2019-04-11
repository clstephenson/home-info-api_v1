package com.clstephenson.homeinfo.api_v1.exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class LocationNotFoundException extends ResourceNotFoundException {

    public LocationNotFoundException(Long id) {
        super("Location ID not found: " + id);
    }
}
