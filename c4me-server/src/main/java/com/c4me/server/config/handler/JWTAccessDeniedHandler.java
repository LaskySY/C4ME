package com.c4me.server.config.handler;

import com.c4me.server.config.constant.Const;
import com.c4me.server.config.interceptor.LogInterceptor;
import com.c4me.server.domain.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-23-2020
 */
public class JWTAccessDeniedHandler implements AccessDeniedHandler {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
      AccessDeniedException e) throws IOException {

    LogInterceptor.logExceptionUnExpect(e, Const.Error.ACCESS_DENIED);
    logger.error(e.getMessage());

    httpServletResponse.setCharacterEncoding(Const.Header.CHARACTER_ENCODING);
    httpServletResponse.setContentType(Const.Header.CONTENT_TYPE);
    httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
    httpServletResponse.getWriter().write(new ObjectMapper().writeValueAsString(
        BaseResponse.builder()
            .code(Const.Error.ACCESS_DENIED)
            .message(e.getMessage())
            .build())
    );
  }
}
