package com.c4me.server.core.hello.controller;

import com.c4me.server.core.hello.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {

   @Autowired
   UserService userService;

    @RequestMapping("/")
    public String hello(){
        System.out.println(userService.queryAll().size());
        return "Hello World----";
    }
}
