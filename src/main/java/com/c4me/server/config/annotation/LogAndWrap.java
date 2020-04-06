package com.c4me.server.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 2-15-2020
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface LogAndWrap {

  String log() default "";//description save in log

  boolean wrap() default true;//wrap data
}
