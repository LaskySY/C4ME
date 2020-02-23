package com.c4me.server.config.interceptor;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.core.log.repository.LogRepository;
import com.c4me.server.entities.LogEntity;
import com.c4me.server.utils.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-16-2020
 */
@Component
public class LogInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public static LogInterceptor loginterceptor;

    @Autowired
    LogRepository logRepository;

    @PostConstruct
    public void init() {
        loginterceptor = this;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (logRepository == null) {// cannot inject service bean
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            logRepository = (LogRepository) factory.getBean("logRepository");
        }
        try{
            HandlerMethod m = (HandlerMethod) handler;
            String description = m.getMethodAnnotation(LogAndWrap.class) == null?
                    null: Objects.requireNonNull(m.getMethodAnnotation(LogAndWrap.class)).log();
            LogEntity log = LogEntity.builder()
                .requestIp(LoggerUtils.getCliectIp(request))
                .type("success")
                .description(description)
                .service(m.getMethod().getName())
                .params(null)
                .build();
            logRepository.save(log);
            request.setAttribute("logId", log.getId());
        } catch (Exception e) {
            logger.error("Log Exception, Cannot save current log");
            LogEntity log = LogEntity.builder()
                    .requestIp(LoggerUtils.getCliectIp(request))
                    .type("fail")
                    .description("Log cannot save")
                    .exceptionDetail(e.getLocalizedMessage())
                    .service("Log interceptor")
                    .build();
            logRepository.save(log);
        }
        return true;
    }

    /**
     * update the log info if error occur
     */
    public synchronized static void logExceptionUnExpect(Throwable e){
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        Integer logId = (Integer) request.getAttribute("logId");
        request.removeAttribute("logId");
        LogEntity logEntity = loginterceptor.logRepository.findById(logId).orElse(null);
        if(logEntity!=null){
            logEntity.setType("fail");
            logEntity.setExceptionCode("500");
            logEntity.setExceptionDetail(e.getMessage());
            loginterceptor.logRepository.save(logEntity);
        }
    }
}
