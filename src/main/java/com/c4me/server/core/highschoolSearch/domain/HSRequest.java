package com.c4me.server.core.highschoolSearch.domain;

import lombok.*;

/**
 * @Description: wrapper around high school name
 * @Author: Maciej Wlodek
 * @CreateDate: 04-10-2020
 */

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class HSRequest {
    String highschoolName;
}
