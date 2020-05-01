package com.c4me.server.core.credential.controller;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.config.exception.DuplicateUsernameException;
import com.c4me.server.core.credential.domain.RegisterUser;
import com.c4me.server.core.credential.service.userDetailsServiceImpl;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-21-2020
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    userDetailsServiceImpl authService;

    @RequestMapping("/register")
    @LogAndWrap(log = "register account")
    public Map<String, String> register(@RequestBody RegisterUser registerUser) throws DuplicateUsernameException {
        authService.register(registerUser);
        return null;
    }
}
