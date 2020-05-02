package com.c4me.server.core.collegeSearch.domain;

import com.c4me.server.entities.ProfileEntity;
import com.c4me.server.entities.StudentApplicationEntity;
import lombok.*;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 04-28-2020
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ApplicationProfileInfo {
    ProfileEntity profileEntity;
    StudentApplicationEntity studentApplicationEntity;
}