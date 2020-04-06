package com.c4me.server.config.exception;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-16-2020
 */

public class UserDoesNotExistException extends Exception {
    public UserDoesNotExistException() {
    }

    public UserDoesNotExistException(String message) {
        super(message);
    }
}
