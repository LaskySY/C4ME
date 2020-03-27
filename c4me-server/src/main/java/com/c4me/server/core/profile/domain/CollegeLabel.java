package com.c4me.server.core.profile.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-26-2020
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CollegeLabel {
    private Integer value;
    private String label;
}
