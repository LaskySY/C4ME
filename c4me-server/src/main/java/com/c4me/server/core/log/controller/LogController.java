package com.c4me.server.core.log.controller;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.core.log.service.LogServiceImpl;
import com.c4me.server.entities.LogEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 2-20-2020
 */
@Controller
@ResponseBody
@RequestMapping("/log")
public class LogController {

   @Autowired
   LogServiceImpl logServiceImpl;

    @RequestMapping("/log")
    @LogAndWrap(log="query all log")
    public Map<String, String> findAll(){
        List<LogEntity> logList = logServiceImpl.queryAll();
      return new HashMap<String, String>(){
          {
              put("log size",String.valueOf(logList.size()));
          }
      };
    }

    @RequestMapping("/err")
    @LogAndWrap(log = "test error")
    public void testError() throws Exception {
        throw new Exception("this is a exception");
    }
}