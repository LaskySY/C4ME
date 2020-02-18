package com.c4me.server.config.annotation;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 2-15-2020
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface LogAndWrap {
    String log() default "" ;//description save in log
    boolean wrap() default true ;//wrap data
}
