package com.c4me.server.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-15-2020
 */

@Embeddable
public class CollegeMajorAssociationEntityPK implements Serializable {
    private int collegeId;
    private String majorName;

    @Column(name = "college_id", nullable = false)
    @Id
    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }

    @Column(name = "major_name", nullable = false, length = 45)
    @Id
    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollegeMajorAssociationEntityPK that = (CollegeMajorAssociationEntityPK) o;
        return collegeId == that.collegeId &&
                Objects.equals(majorName, that.majorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collegeId, majorName);
    }
}
