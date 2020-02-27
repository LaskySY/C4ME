package com.c4me.server.config.aspect;

import java.util.Arrays;
import java.util.List;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-15-2020
 */
@Aspect
@Component
public class LogInfoAspect {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Around(value = "execution(* com.c4me.server.core..controller.*.*(..))")
  public Object printLog(ProceedingJoinPoint pjp) throws Throwable {
    String classname = pjp.getTarget().getClass().getSimpleName();
    String methodName = pjp.getSignature().getName();
    List<Object> args = Arrays.asList(pjp.getArgs());
    logger.info("Class: {}, Method: {}, Param: {}", classname, methodName, args);
    return pjp.proceed();
  }
}
