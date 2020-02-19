package com.c4me.server.domain;

import lombok.*;

import java.io.Serializable;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-15-2020
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BaseResponse<T> implements Serializable {
    @Builder.Default
    private Boolean success = true;
    private String message;
    private T data;
}
