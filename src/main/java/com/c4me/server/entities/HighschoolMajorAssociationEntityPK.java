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
public class HighschoolMajorAssociationEntityPK implements Serializable {
    private int highschool_id;
    private String major_name;

    @Column(name = "highschool_id", nullable = false)
    @Id
    public int getHighschool_id() {
        return highschool_id;
    }

    public void setHighschool_id(int highschoolId) {
        this.highschool_id = highschoolId;
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
        HighschoolMajorAssociationEntityPK that = (HighschoolMajorAssociationEntityPK) o;
        return highschool_id == that.highschool_id &&
                Objects.equals(major_name, that.major_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(highschool_id, major_name);
    }
}
