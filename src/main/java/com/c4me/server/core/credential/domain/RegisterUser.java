package com.c4me.server.core.credential.domain;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Description: check valid in register @RequestBody
 * @Author: Siyong Liu
 * @CreateDate: 02-25-2020
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUser {
  private String username;
  @NotNull(message = "password cannot be null")
  private String password;
  private String name;
}
