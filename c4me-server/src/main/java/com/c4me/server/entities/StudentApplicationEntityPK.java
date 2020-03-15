package com.c4me.server.entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-15-2020
 */

public class StudentApplicationEntityPK implements Serializable {
    private String username;
    private int collegeId;

    @Column(name = "username", nullable = false, length = 255)
    @Id
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "college_id", nullable = false)
    @Id
    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentApplicationEntityPK that = (StudentApplicationEntityPK) o;
        return collegeId == that.collegeId &&
                Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, collegeId);
    }
}
