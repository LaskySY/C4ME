package com.c4me.server.config.advice;

import com.c4me.server.config.interceptor.LogInterceptor;
import com.c4me.server.domain.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-22-2020
 */
@RestControllerAdvice(basePackages = "com.c4me.server")
@Slf4j
public class ErrorAdvice {
    @ExceptionHandler({Exception.class})
    @ResponseBody
    public Object ExceptionException(Exception exception) {
        LogInterceptor.logExceptionUnExpect(exception);
        exception.printStackTrace();
        BaseResponse<Object> result = BaseResponse.builder()
                .message("Error, please contact administrator")
                .success(false)
                .build();
        return result;
    }
}