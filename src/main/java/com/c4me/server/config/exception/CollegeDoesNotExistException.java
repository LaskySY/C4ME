package com.c4me.server.config.exception;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-19-2020
 */

public class CollegeDoesNotExistException extends Exception {

    public CollegeDoesNotExistException(String message) {
        super(message);
    }
}
