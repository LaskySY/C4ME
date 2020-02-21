package com.c4me.server.core.log.controller;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.core.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 2-20-2020
 */
@Controller
@ResponseBody
public class LogController {

   @Autowired
   LogService logService;

    @GetMapping("/log")
    @LogAndWrap(log="query all log")
    public Map findAll(){
        List logList = logService.queryAll();
        Map<String, String> map = new HashMap(){
            {
                put("log size",String.valueOf(logList.size()));
            }
        };
        return map;
    }
}