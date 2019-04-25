package com.clstephenson.homeinfo.api_v1.exception;

import com.clstephenson.homeinfo.api_v1.logging.MyLogger;

public class FileStorageException extends RuntimeException {

    private final MyLogger LOGGER = new MyLogger(FileStorageException.class);

    public FileStorageException(String message) {
        super(message);
        LOGGER.log(super.getLocalizedMessage());
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
        LOGGER.log(super.getLocalizedMessage());
    }


}
