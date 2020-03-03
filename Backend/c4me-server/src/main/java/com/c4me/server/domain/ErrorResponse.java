package com.c4me.server.domain;

import lombok.*;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-26-2020
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorResponse {
  @Builder.Default
  private Boolean success = false;
  private String errorCode;
  private String message;
}