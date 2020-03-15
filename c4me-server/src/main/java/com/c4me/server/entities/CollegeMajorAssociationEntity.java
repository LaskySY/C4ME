package com.c4me.server.entities;

import javax.persistence.*;
import java.util.Objects;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-15-2020
 */

@Entity
@Table(name = "College_major_association", schema = "siyoliu")
@IdClass(CollegeMajorAssociationEntityPK.class)
public class CollegeMajorAssociationEntity {
    private int collegeId;
    private String majorName;
    private CollegeEntity collegeByCollegeId;
    private MajorEntity majorByMajorName;

    @Id
    @Column(name = "college_id", nullable = false)
    public int getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(int collegeId) {
        this.collegeId = collegeId;
    }

    @Id
    @Column(name = "major_name", nullable = false, length = 45)
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
        CollegeMajorAssociationEntity that = (CollegeMajorAssociationEntity) o;
        return collegeId == that.collegeId &&
                Objects.equals(majorName, that.majorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collegeId, majorName);
    }

    @ManyToOne
    @JoinColumn(name = "college_id", referencedColumnName = "id", nullable = false)
    public CollegeEntity getCollegeByCollegeId() {
        return collegeByCollegeId;
    }

    public void setCollegeByCollegeId(CollegeEntity collegeByCollegeId) {
        this.collegeByCollegeId = collegeByCollegeId;
    }

    @ManyToOne
    @JoinColumn(name = "major_name", referencedColumnName = "name", nullable = false)
    public MajorEntity getMajorByMajorName() {
        return majorByMajorName;
    }

    public void setMajorByMajorName(MajorEntity majorByMajorName) {
        this.majorByMajorName = majorByMajorName;
    }
}
