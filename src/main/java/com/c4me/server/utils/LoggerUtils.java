package com.c4me.server.utils;

import com.c4me.server.core.log.repository.LogRepository;

import javax.servlet.http.HttpServletRequest;

import com.c4me.server.entities.LogEntity;
import lombok.NonNull;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-16-2020
 */
public class LoggerUtils {


  /**
   * @param request
   * @param Service
   * @param errorMessage
   * @param errorCode
   */
  public static void saveFailLog(@NonNull HttpServletRequest request,
      String Service,
      String errorMessage,
      String errorCode) {

    LogEntity log = new LogEntity();
    log.setType("fail");
    log.setExceptionCode(errorCode);
    log.setExceptionDetail(errorMessage);
    log.setService(Service);
    log.setRequestIp(LoggerUtils.getCliectIp(request));

    String fullToken = request.getHeader(JwtTokenUtils.TOKEN_HEADER);
    if (fullToken != null) {
      log.setParams(fullToken.replace(JwtTokenUtils.TOKEN_PREFIX, ""));
    }
    BeanFactory factory = WebApplicationContextUtils
        .getRequiredWebApplicationContext(request.getServletContext());
    LogRepository logRepository = (LogRepository) factory.getBean("logRepository");
    logRepository.save(log);
  }

  public static void saveSuccessLog(@NonNull HttpServletRequest request,
      String service,
      String description) {

    LogEntity log = new LogEntity();
    log.setType("success");
    log.setDescription(description);
    log.setService(service);
    log.setRequestIp(LoggerUtils.getCliectIp(request));

    String fullToken = request.getHeader(JwtTokenUtils.TOKEN_HEADER);
    if (fullToken != null) {
      log.setParams(fullToken.replace(JwtTokenUtils.TOKEN_PREFIX, ""));
    }
    if (service.equals("successfulAuthentication")) {
      log.setDescription("login");
      log.setParams(description);
      log.setUsername(JwtTokenUtils.getUsername(description));
      log.setUserRole(JwtTokenUtils.getUserRole(description).equals("ROLE_ADMIN") ? 1 : 0);
    }
    BeanFactory factory = WebApplicationContextUtils
        .getRequiredWebApplicationContext(request.getServletContext());
    LogRepository logRepository = (LogRepository) factory.getBean("logRepository");
    logRepository.save(log);
  }

  /**
   * get client ip
   *
   * @param request
   * @return
   */
  public static String getCliectIp(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.trim().equals("") || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.trim().equals("") || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.trim().equals("") || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }

    //  get first IP if there are more than one unknown ip
    final String[] arr = ip.split(",");
    for (final String str : arr) {
      if (!"unknown".equalsIgnoreCase(str)) {
        ip = str;
        break;
      }
    }
    return ip;
  }

  /**
   * is ajax request
   */
  public static boolean isAjaxRequest(HttpServletRequest request) {
    return request.getHeader("accept").contains("application/json")
        || (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With")
        .equals(
            "XMLHttpRequest"));
  }
}
