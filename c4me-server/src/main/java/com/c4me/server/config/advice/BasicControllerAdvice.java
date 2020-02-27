package com.c4me.server.config.advice;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.domain.BaseResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.annotation.Annotation;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Siyong Liu
 * @CreateDate 02-15-2020
 **/
@RestControllerAdvice(basePackages = "com.c4me.server")
@Slf4j
public class BasicControllerAdvice implements ResponseBodyAdvice<Object> {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public boolean supports( MethodParameter returnType, Class converterType) {
    try {
      Annotation LAW = returnType.getMethodAnnotation(LogAndWrap.class);
      if (LAW == null) {
        return false;
      }
      return Objects.requireNonNull(returnType.getMethodAnnotation(LogAndWrap.class)).wrap();
    } catch (Exception e) {
      logger.error(e.getMessage());
      return false;
    }
  }

  @Nullable
  @Override
  public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType,
      MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request,
      ServerHttpResponse response) {
    if (!(body instanceof BaseResponse)
        && returnType.getMethodAnnotation(LogAndWrap.class) != null) {
      try {
        BaseResponse<Object> result = BaseResponse.builder()
            .message(Objects.requireNonNull(returnType.getMethodAnnotation(LogAndWrap.class)).log())
            .success(true)
            .data(body)
            .build();
        if (body instanceof String) {
          try {
            return new ObjectMapper().writeValueAsString(result);
          } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
          }
        }
        return result;
      } catch (Exception e) {
        logger.error(e.getMessage());
      }
    }
    return body;
  }
}
