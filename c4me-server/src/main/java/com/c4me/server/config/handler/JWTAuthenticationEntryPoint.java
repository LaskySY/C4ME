package com.c4me.server.config.handler;

import com.c4me.server.config.constant.Const;
import com.c4me.server.domain.BaseResponse;
import com.c4me.server.utils.LoggerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-23-2020
 */
public class JWTAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public void commence(HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException) throws IOException {

    LoggerUtils.saveFailLog(request, "JWTAuthenticationEntryPoint", authException.getMessage(),
        Const.Error.MISS_TOKEN);
    logger.error(authException.getMessage());

    response.setCharacterEncoding(Const.Header.CHARACTER_ENCODING);
    response.setContentType(Const.Header.CONTENT_TYPE);
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.getWriter().write(new ObjectMapper().writeValueAsString(
        BaseResponse.builder()
            .code(Const.Error.MISS_TOKEN)
            .message(authException.getMessage())
            .build())
    );
  }
}
