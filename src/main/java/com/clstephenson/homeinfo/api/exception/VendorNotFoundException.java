package com.clstephenson.homeinfo.api.exception;

import com.clstephenson.homeinfo.logging.MyLogger;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

public class VendorNotFoundException extends ResourceNotFoundException {

    private final MyLogger LOGGER = new MyLogger(VendorNotFoundException.class);

    public VendorNotFoundException(Long id) {
        super("Vendor ID [" + id + "] not found");
        LOGGER.log(getLocalizedMessage());
    }

    public VendorNotFoundException(String message) {
        super(message);
        LOGGER.log(getLocalizedMessage());
    }

    public VendorNotFoundException(String message, Throwable cause) {
        super(message, cause);
        LOGGER.log(getLocalizedMessage());
    }
}
