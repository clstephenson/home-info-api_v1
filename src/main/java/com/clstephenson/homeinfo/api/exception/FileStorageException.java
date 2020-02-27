package com.clstephenson.homeinfo.api.exception;

import com.clstephenson.homeinfo.logging.MyLogger;

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
