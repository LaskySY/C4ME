package com.c4me.server.core.credential.controller;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.config.exception.DuplicateUsernameException;
import com.c4me.server.core.credential.service.userDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-21-2020
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    userDetailsServiceImpl authService;

    @RequestMapping("/register")
    @LogAndWrap(log = "register")
    public Map<String, String> register(@RequestBody Map<String,String> registerUser) throws DuplicateUsernameException {
        authService.register(registerUser);
        return null;
    }
}
