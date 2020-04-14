package com.c4me.server.entities;

import lombok.*;

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

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class CollegeMajorAssociationEntityPK implements Serializable {
    private int college_id;
    private String major_name;

    @Column(name = "college_id", nullable = false)
    @Id
    public int getCollege_id() {
        return college_id;
    }

    public void setCollege_id(int collegeId) {
        this.college_id = collegeId;
    }

    @Column(name = "major_name", nullable = false, length = 45)
    @Id
    public String getMajor_name() {
        return major_name;
    }

    public void setMajor_name(String majorName) {
        this.major_name = majorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollegeMajorAssociationEntityPK that = (CollegeMajorAssociationEntityPK) o;
        return college_id == that.college_id &&
                Objects.equals(major_name, that.major_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(college_id, major_name);
    }
}
