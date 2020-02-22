package com.c4me.server.core.credential.domain;

import lombok.*;

import java.io.Serializable;

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
public class GoogleUserInfoEntity implements Serializable {
    private String iss;
    private String azp;
    private String aud;
    private String sub;
    private String hd;
    private String email;
    private String email_verified;
    private String at_hash;
    private String name;
    private String picture;
    private String given_name;
    private String family_name;
    private String locale;
    private String iat;
    private String exp;
    private String alg;
    private String kid;
    private String typ;
}
