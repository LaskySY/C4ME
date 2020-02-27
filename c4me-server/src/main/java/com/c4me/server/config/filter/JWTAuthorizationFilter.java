package com.c4me.server.config.filter;

import com.c4me.server.config.constant.Const;
import com.c4me.server.domain.ErrorResponse;
import com.c4me.server.utils.JwtTokenUtils;
import com.c4me.server.utils.LoggerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-22-2020
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain) throws IOException, ServletException {

    String tokenHeader = request.getHeader(JwtTokenUtils.TOKEN_HEADER);
    if (tokenHeader == null || !tokenHeader.startsWith(JwtTokenUtils.TOKEN_PREFIX)) {
      chain.doFilter(request, response);
      return;
    }
    try {
      SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
    } catch (ExpiredJwtException e) {
      LoggerUtils
          .saveLog(request, "JWTAuthorizationFilter", e.getMessage(), Const.Error.TOKEN_EXPIRED);
      logger.error(e.getMessage());
      response.setCharacterEncoding(Const.Header.CHARACTER_ENCODING);
      response.setContentType(Const.Header.CONTENT_TYPE);
      response.setStatus(HttpServletResponse.SC_FORBIDDEN);
      response.getWriter().write(new ObjectMapper().writeValueAsString(
          ErrorResponse.builder()
              .errorCode(Const.Error.TOKEN_EXPIRED)
              .message(e.getMessage())
              .build())
      );
      response.getWriter().flush();
      return;
    }
    super.doFilterInternal(request, response, chain);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
    String token = tokenHeader.replace(JwtTokenUtils.TOKEN_PREFIX, "");
    String username = JwtTokenUtils.getUsername(token);
    String role = JwtTokenUtils.getUserRole(token);
    if (username != null) {
      return new UsernamePasswordAuthenticationToken(username, null,
          Collections.singleton(new SimpleGrantedAuthority(role)));
    }
    return null;
  }
}