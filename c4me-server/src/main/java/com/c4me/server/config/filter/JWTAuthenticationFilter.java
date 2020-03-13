package com.c4me.server.config.filter;

import com.c4me.server.config.constant.Const;
import com.c4me.server.core.credential.domain.JwtUser;
import com.c4me.server.core.credential.domain.LoginUser;
import com.c4me.server.domain.BaseResponse;
import com.c4me.server.utils.JwtTokenUtils;
import com.c4me.server.utils.LoggerUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Collection;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-22-2020
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  private ThreadLocal<Boolean> rememberMe = new ThreadLocal<>();
  private AuthenticationManager authenticationManager;

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
    super.setFilterProcessesUrl("/auth/login");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    try {
      LoginUser loginUser = new ObjectMapper()
          .readValue(request.getInputStream(), LoginUser.class);
      rememberMe.set(loginUser.getRememberMe());
      return authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              loginUser.getUsername(),
              loginUser.getPassword())
      );
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult) {

    JwtUser jwtUser = (JwtUser) authResult.getPrincipal();
    boolean isRemember = rememberMe.get();

    Collection<? extends GrantedAuthority> authorities = jwtUser.getAuthorities();
    String token = JwtTokenUtils.createToken(jwtUser, isRemember);
    response.setHeader(Const.Header.TOKEN, JwtTokenUtils.TOKEN_PREFIX + token);

    logger.info("login success");
    LoggerUtils.saveSuccessLog(request,"successfulAuthentication",token);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed) throws IOException {
    LoggerUtils.saveFailLog(request, "JWTAuthenticationFilter", failed.getMessage(),
        Const.Error.AUTHENTICATION);
    logger.error(failed.getMessage());
    response.getWriter().write(new ObjectMapper().writeValueAsString(
        BaseResponse.builder()
            .code(Const.Error.AUTHENTICATION)
            .message(failed.getMessage())
            .build())
    );
  }
}