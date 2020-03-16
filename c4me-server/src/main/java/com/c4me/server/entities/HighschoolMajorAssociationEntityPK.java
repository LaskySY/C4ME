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
public class HighschoolMajorAssociationEntityPK implements Serializable {
    private int highschoolId;
    private String majorName;

    @Column(name = "highschool_id", nullable = false)
    @Id
    public int getHighschoolId() {
        return highschoolId;
    }

    public void setHighschoolId(int highschoolId) {
        this.highschoolId = highschoolId;
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
        HighschoolMajorAssociationEntityPK that = (HighschoolMajorAssociationEntityPK) o;
        return highschoolId == that.highschoolId &&
                Objects.equals(majorName, that.majorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(highschoolId, majorName);
    }
}
