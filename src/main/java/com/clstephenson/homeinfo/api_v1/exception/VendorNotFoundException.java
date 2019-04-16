package com.clstephenson.homeinfo.api_v1.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class VendorNotFoundException extends ResourceNotFoundException {

    Logger logger = LoggerFactory.getLogger(VendorNotFoundException.class);

    public VendorNotFoundException(Long id) {
        super("Vendor ID [" + id + "] not found");
        logger.warn(getLocalizedMessage());
    }

    public VendorNotFoundException(String message) {
        super(message);
        logger.warn(getLocalizedMessage());
    }

    public VendorNotFoundException(String message, Throwable cause) {
        super(message, cause);
        logger.warn(getLocalizedMessage(), cause);
    }
}
