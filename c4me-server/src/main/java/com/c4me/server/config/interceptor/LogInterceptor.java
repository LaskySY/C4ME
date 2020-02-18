//package com.c4me.server.config.interceptor;
//
//import com.c4me.server.core.log.repository.LogRepository;
//import com.c4me.server.entities.LogEntity;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.annotation.PostConstruct;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.UUID;
//
///**
// * @Description:
// * @Author: Siyong Liu
// * @CreateDate: 02-16-2020
// */
//@Component
//public class LogInterceptor implements HandlerInterceptor {
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//    public static LogInterceptor loginterceptor;
//
//    @Autowired
//    LogRepository logService;
//
//    @PostConstruct
//    public void init() {
//        loginterceptor = this;
//    }
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        logService.save( LogEntity.builder()
//            .id(UUID.randomUUID())
//            .type("success")
//            .build()
//        );
//        return true;
//    }
//
////    /**
////     * 如果发生了异常直接出发进行当条日志的更新
////     */
////    public synchronized static void logExceptionUnExpect(Throwable e){
////        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
////        String log = (String) request.getAttribute("logg");
////        request.removeAttribute("logg");
////        LogEntity logEntity = userRepository.findById(log);
////        sysLog.setType("fail");
////        sysLog.setExceptionCode("500");
////        sysLog.setExceptionDetail(e.getMessage());
////        interceptorLogger.sysLogService.updateById(sysLog);
////    }
//}
