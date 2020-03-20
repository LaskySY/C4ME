package com.c4me.server.core.profile.domain;

import com.c4me.server.entities.StudentApplicationEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-19-2020
 */

@Setter
@Getter
@NoArgsConstructor
public class StudentApplication {
    private String username;
    private String collegeName;
    private String admissionTerm;
    private Integer status;

    public StudentApplication(StudentApplicationEntity sae) {
        this.username = sae.getUserByUsername().getUsername();
        this.collegeName = sae.getCollegeByCollegeId().getName();
        this.admissionTerm = sae.getAdmission_term();
        this.status = sae.getStatus();
    }
}
