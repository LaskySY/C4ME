package com.c4me.server.config.constant;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-26-2020
 */
public final class Const {

  private Const() {
  }

  public final static class Header {

    public static final String TOKEN = "token";
    public static final String CHARACTER_ENCODING = "UTF-8";
    public static final String CONTENT_TYPE = "application/json; charset=utf-8";
  }

  public final static class Error {

    public static final String DUPLICATE_USERNAME = "duplicateUsername";
    public static final String LOG = "512";
    public static final String AUTHENTICATION = "513";
    public static final String MISS_TOKEN = "514";
    public static final String TOKEN_EXPIRED = "515";
    public static final String ACCESS_DENIED = "516";
  }

  public final static class Role {

    public static final String ADMIN = "Admin";
    public static final String STUDENT = "Student";
  }

}
