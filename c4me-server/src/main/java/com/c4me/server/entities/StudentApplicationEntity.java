package com.c4me.server.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-15-2020
 */

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Student_application", schema = "siyoliu")
//@IdClass(StudentApplicationEntityPK.class)
public class StudentApplicationEntity {
//    private String username;
//    private int collegeId;
    private String admission_term;
    private Integer status;
    private Byte questionable;
    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false, insertable = false, updatable = false)
    private UserEntity userByUsername;
    @ManyToOne
    @JoinColumn(name = "college_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private CollegeEntity collegeByCollegeId;
    @EmbeddedId
    private StudentApplicationEntityPK studentApplicationEntityPK;

//    @Id
//    @Column(name = "username", nullable = false, length = 255)
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    @Id
//    @Column(name = "college_id", nullable = false, insertable = "false", update = "false")
//    public int getCollegeId() {
//        return collegeId;
//    }
//
//    public void setCollegeId(int collegeId) {
//        this.collegeId = collegeId;
//    }

    @Basic
    @Column(name = "admission_term", nullable = true, length = 45)
    public String getAdmission_term() {
        return admission_term;
    }

    public void setAdmission_term(String admissionTerm) {
        this.admission_term = admissionTerm;
    }

    @Basic
    @Column(name = "status", nullable = true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "questionable", nullable = true)
    public Byte getQuestionable() {
        return questionable;
    }

    public void setQuestionable(Byte questionable) {
        this.questionable = questionable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentApplicationEntity that = (StudentApplicationEntity) o;
        return
                Objects.equals(studentApplicationEntityPK, that.studentApplicationEntityPK) &&
                Objects.equals(admission_term, that.admission_term) &&
                Objects.equals(status, that.status) &&
                Objects.equals(questionable, that.questionable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentApplicationEntityPK, admission_term, status, questionable);
    }


    public UserEntity getUserByUsername() {
        return userByUsername;
    }

    public void setUserByUsername(UserEntity userByUsername) {
        this.userByUsername = userByUsername;
    }


    public CollegeEntity getCollegeByCollegeId() {
        return collegeByCollegeId;
    }

    public void setCollegeByCollegeId(CollegeEntity collegeByCollegeId) {
        this.collegeByCollegeId = collegeByCollegeId;
    }
}
