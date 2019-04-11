package com.clstephenson.homeinfo.api_v1.exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class IdeaNotFoundException extends ResourceNotFoundException {

    public IdeaNotFoundException(Long id) {
        super("Idea ID not found: " + id);
    }
}
