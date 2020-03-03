package com.c4me.server.core.credential.domain;

import lombok.*;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-22-2020
 */
@Setter
@Getter
public class LoginUser {
    private String username;
    private String password;
    private Boolean rememberMe;
}
