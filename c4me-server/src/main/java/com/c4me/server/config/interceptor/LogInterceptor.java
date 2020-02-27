package com.c4me.server.config.interceptor;

import static com.c4me.server.utils.JwtTokenUtils.TOKEN_PREFIX;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.config.constant.Const;
import com.c4me.server.core.log.repository.LogRepository;
import com.c4me.server.entities.LogEntity;
import com.c4me.server.utils.JwtTokenUtils;
import com.c4me.server.utils.LoggerUtils;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-16-2020
 */
@Component
public class LogInterceptor implements HandlerInterceptor {

  public static LogInterceptor loginterceptor;
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private LogRepository logRepository;

  /**
   * update the log info if error occur
   */
  public synchronized static void logExceptionUnExpect(Throwable e, String errorCode) {
    HttpServletRequest request = ((ServletRequestAttributes) Objects
        .requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    Integer logId = (Integer) request.getAttribute("logId");
    request.removeAttribute("logId");
    LogEntity logEntity = loginterceptor.logRepository.findById(logId).orElse(null);
    if (logEntity != null) {
      logEntity.setType("fail");
      logEntity.setExceptionCode(errorCode);
      logEntity.setExceptionDetail(e.getMessage());
      loginterceptor.logRepository.save(logEntity);
    }
  }

  @PostConstruct
  public void init() {
    loginterceptor = this;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    if (logRepository == null) {
      BeanFactory factory = WebApplicationContextUtils
          .getRequiredWebApplicationContext(request.getServletContext());
      logRepository = (LogRepository) factory.getBean("logRepository");
    }
    try {
      HandlerMethod m = (HandlerMethod) handler;
      String description = m.getMethodAnnotation(LogAndWrap.class) == null ?
          null : Objects.requireNonNull(m.getMethodAnnotation(LogAndWrap.class)).log();
      LogEntity log = new LogEntity();
      log.setRequestIp(LoggerUtils.getCliectIp(request));
      log.setType("success");
      log.setDescription(description);
      log.setService(m.getMethod().getName());

      String token = request.getHeader(JwtTokenUtils.TOKEN_HEADER);
      if (token != null) {
        token = token.replace(TOKEN_PREFIX, "");
        log.setParams(token);
        if (JwtTokenUtils.checkJWT(token)) {
          log.setUserId(JwtTokenUtils.getUserId(token));
          log.setUsername(JwtTokenUtils.getUsername(token));
          log.setUserRole(JwtTokenUtils.getUserRole(token).equals("ROLE_ADMIN") ? 1 : 0);
        }
      }
      logRepository.save(log);

      request.setAttribute("logId", log.getId());
    } catch (Exception e) {
      logger.error("Cannot save current Log." + e.getMessage());
      LoggerUtils.saveFailLog(request, "Log", "Cannot save current Log.", Const.Error.LOG);
    }
    return true;
  }
}
