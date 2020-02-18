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
public class Hello {

   @Autowired
   LogService logService;

    @GetMapping("/get")
    @LogAndWrap(log="query all log")
    public HashMap findAll(@RequestBody String str){
        System.out.println(str);
        List list = logService.queryAll();
        System.out.println(list);
        HashMap<String, String> map = new HashMap<String, String>(){
            {
                put("email","123@123.com");
            }
        };
        return map;
    }
}