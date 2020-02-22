package com.c4me.server.config.advice;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.domain.BaseResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Siyong Liu
 * @create 2-15-2020
 **/
@RestControllerAdvice(basePackages = "com.c4me.server")
@Slf4j
public class BasicControllerAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        try{
            return returnType.getMethodAnnotation(LogAndWrap.class).wrap();
        }catch (Exception e){
            return false;
        }
    }

    @Nullable
    @Override
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if(!(body instanceof BaseResponse)&&returnType.getMethodAnnotation(LogAndWrap.class)!=null){
            try {
                BaseResponse<Object> result = BaseResponse.builder()
                        .message(returnType.getMethodAnnotation(LogAndWrap.class).log()+" success")
                        .success(true)
                        .data(body)
                        .build();
                if(body instanceof String){
                    try {
                        return new ObjectMapper().writeValueAsString(result);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return body;
    }
}
