package com.c4me.server.config.exception;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-25-2020
 */

public class HighSchoolDoesNotExistException extends Exception {
    public HighSchoolDoesNotExistException(String message) {
        super(message);
    }
}
