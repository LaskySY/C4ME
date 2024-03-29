package com.c4me.server.core.profile.domain;

import com.c4me.server.entities.StudentApplicationEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.c4me.server.config.constant.Const.Status.STATUS_MAP;

/**
 * @Description: Student application object that may be safely passed to frontend
 * @Author: Maciej Wlodek
 * @CreateDate: 03-19-2020
 */

@Setter
@Getter
@NoArgsConstructor
public class StudentApplication {

    private Integer collegeId;
    private String username;
    private CollegeLabel college;
    private String admissionTerm;
    private Integer status;
    private Byte questionable;
    private String statusString;

    public StudentApplication(StudentApplicationEntity sae) {
        this.username = sae.getUserByUsername().getUsername();
        this.college = new CollegeLabel(sae.getCollegeByCollegeId().getId(), sae.getCollegeByCollegeId().getName());
        //this.collegeName = sae.getCollegeByCollegeId().getName();
        this.collegeId = sae.getCollegeByCollegeId().getId();
        this.admissionTerm = sae.getAdmission_term();
        this.status = sae.getStatus();
        this.questionable = sae.getQuestionable();
        this.statusString = sae.getStatus() == null ? "null" : STATUS_MAP.get(sae.getStatus());
    }
}
