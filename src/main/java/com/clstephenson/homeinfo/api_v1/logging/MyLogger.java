package com.clstephenson.homeinfo.api_v1.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;

public class MyLogger {

    private final Logger LOGGER;
    private LogLevel defaultLevel;

    public MyLogger(Class<?> clazz) {
        LOGGER = LoggerFactory.getLogger(clazz);
        this.defaultLevel = LogLevel.WARN;
    }

    public MyLogger(Logger logger) {
        LOGGER = logger;
        this.defaultLevel = LogLevel.WARN;
    }

    public Logger getLogger() {
        return LOGGER;
    }

    public void log(String message) {
        log(message, defaultLevel);
    }

    public void log(String message, LogLevel level) {
        switch (level) {
            case ERROR:
                LOGGER.error(message);
                break;
            case DEBUG:
                LOGGER.debug(message);
                break;
            case WARN:
                LOGGER.warn(message);
                break;
            case TRACE:
                LOGGER.trace(message);
                break;
            case INFO:
                LOGGER.info(message);
                break;
        }
    }

    public void setDefaultLevel(LogLevel defaultLevel) {
        this.defaultLevel = defaultLevel;
    }

    public void info(String message) {
        LOGGER.info(message);
    }

    public void debug(String message) {
        LOGGER.debug(message);
    }

    public void warn(String message) {
        LOGGER.warn(message);
    }

    public void error(String message) {
        LOGGER.error(message);
    }

    public void trace(String message) {
        LOGGER.trace(message);
    }
}
