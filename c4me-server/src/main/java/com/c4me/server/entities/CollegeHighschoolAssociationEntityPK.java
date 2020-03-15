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

public class CollegeHighschoolAssociationEntityPK implements Serializable {
    private int collegeId;
    private int highschoolId;

    @Column(name = "college_id", nullable = false)
    @Id
    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }

    @Column(name = "highschool_id", nullable = false)
    @Id
    public int getHighschoolId() {
        return highschoolId;
    }

    public void setHighschoolId(int highschoolId) {
        this.highschoolId = highschoolId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollegeHighschoolAssociationEntityPK that = (CollegeHighschoolAssociationEntityPK) o;
        return collegeId == that.collegeId &&
                highschoolId == that.highschoolId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(collegeId, highschoolId);
    }
}
