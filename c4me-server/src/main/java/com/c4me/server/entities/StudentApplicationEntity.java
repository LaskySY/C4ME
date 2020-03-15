package com.c4me.server.entities;

import javax.persistence.*;
import java.util.Objects;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-15-2020
 */

@Entity
@Table(name = "Student_application", schema = "siyoliu")
@IdClass(StudentApplicationEntityPK.class)
public class StudentApplicationEntity {
    private String username;
    private int collegeId;
    private String admissionTerm;
    private Integer status;
    private Byte questionable;
    private UserEntity userByUsername;
    private CollegeEntity collegeByCollegeId;

    @Id
    @Column(name = "username", nullable = false, length = 255)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Id
    @Column(name = "college_id", nullable = false)
    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }

    @Basic
    @Column(name = "admission_term", nullable = true, length = 45)
    public String getAdmissionTerm() {
        return admissionTerm;
    }

    public void setAdmissionTerm(String admissionTerm) {
        this.admissionTerm = admissionTerm;
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
        return collegeId == that.collegeId &&
                Objects.equals(username, that.username) &&
                Objects.equals(admissionTerm, that.admissionTerm) &&
                Objects.equals(status, that.status) &&
                Objects.equals(questionable, that.questionable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, collegeId, admissionTerm, status, questionable);
    }

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    public UserEntity getUserByUsername() {
        return userByUsername;
    }

    public void setUserByUsername(UserEntity userByUsername) {
        this.userByUsername = userByUsername;
    }

    @ManyToOne
    @JoinColumn(name = "college_id", referencedColumnName = "id", nullable = false)
    public CollegeEntity getCollegeByCollegeId() {
        return collegeByCollegeId;
    }

    public void setCollegeByCollegeId(CollegeEntity collegeByCollegeId) {
        this.collegeByCollegeId = collegeByCollegeId;
    }
}
