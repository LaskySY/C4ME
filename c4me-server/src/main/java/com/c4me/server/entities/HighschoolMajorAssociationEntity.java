package com.c4me.server.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Highschool_major_association", schema = "siyoliu")
@IdClass(HighschoolMajorAssociationEntityPK.class)
public class HighschoolMajorAssociationEntity {
    private int highschoolId;
    private String majorName;

    @Id
    @Column(name = "highschool_id")
    public int getHighschoolId() {
        return highschoolId;
    }

    public void setHighschoolId(int highschoolId) {
        this.highschoolId = highschoolId;
    }

    @Id
    @Column(name = "major_name")
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
        HighschoolMajorAssociationEntity that = (HighschoolMajorAssociationEntity) o;
        return highschoolId == that.highschoolId &&
                Objects.equals(majorName, that.majorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(highschoolId, majorName);
    }
}
