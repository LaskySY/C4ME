package com.c4me.server.core.credential.domain;

import lombok.*;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-21-2020
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GitHubUserInfoEntity {
    private String name;
    private String email;
}