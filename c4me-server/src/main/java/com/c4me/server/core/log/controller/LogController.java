package com.c4me.server.core.log.controller;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.core.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@ResponseBody
public class LogController {

   @Autowired
   LogService logService;

    @GetMapping("/get")
    @LogAndWrap(log="query all log")
    public HashMap findAll(@RequestBody HashMap body){
        List logList = logService.queryAll();
        HashMap<String, String> map = new HashMap(){
            {
                put("string",body.get("string"));
                put("log size",String.valueOf(logList.size()));
            }
        };
        return map;
    }
}