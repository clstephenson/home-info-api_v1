package com.clstephenson.homeinfo.api_v1.exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class VendorNotFoundException extends ResourceNotFoundException {

    public VendorNotFoundException(Long id) {
        super("Vendor ID not found: " + id);
    }
}
