package com.c4me.server.core.profile.domain;

import com.c4me.server.entities.HighschoolEntity;
import com.c4me.server.entities.MajorEntity;
import com.c4me.server.entities.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-15-2020
 */

@Setter
@Getter
public class ProfileInfo {
    private String username;
    private String name;
    private Integer numApCourses;
    private Double gpa;
    private Integer satMath;
}
