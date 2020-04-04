package com.c4me.server.config.exception;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-23-2020
 */
public class DuplicateUsernameException extends Exception {

  public DuplicateUsernameException() {
  }

  public DuplicateUsernameException(String message) {
    super(message);
  }
}
