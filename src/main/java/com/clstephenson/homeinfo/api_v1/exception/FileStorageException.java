package com.clstephenson.homeinfo.api_v1.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileStorageException extends RuntimeException {

    Logger logger = LoggerFactory.getLogger(FileStorageException.class);

    public FileStorageException(String message) {
        super(message);
        logger.warn(super.getLocalizedMessage());
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
        logger.warn(super.getLocalizedMessage());
    }
}
