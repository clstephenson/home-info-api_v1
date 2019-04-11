package com.clstephenson.homeinfo.api_v1.exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class FeatureNotFoundException extends ResourceNotFoundException {

    public FeatureNotFoundException(Long id) {
        super("Feature ID not found: " + id);
    }
}
