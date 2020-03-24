package com.c4me.server.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollegeHighschoolAssociationEntityPK implements Serializable {
    private int college_id;
    private int highschool_id;

    @Column(name = "college_id", nullable = false)
    @Id
    public int getCollege_id() {
        return college_id;
    }

    public void setCollege_id(int collegeId) {
        this.college_id = collegeId;
    }

    @Column(name = "highschool_id", nullable = false)
    @Id
    public int getHighschool_id() {
        return highschool_id;
    }

    public void setHighschool_id(int highschoolId) {
        this.highschool_id = highschoolId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollegeHighschoolAssociationEntityPK that = (CollegeHighschoolAssociationEntityPK) o;
        return college_id == that.college_id &&
                highschool_id == that.highschool_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(college_id, highschool_id);
    }
}
