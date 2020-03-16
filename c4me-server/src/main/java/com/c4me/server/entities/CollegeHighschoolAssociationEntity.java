package com.c4me.server.entities;

import javax.persistence.*;
import java.util.Objects;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-15-2020
 */

@Entity
@Table(name = "College_highschool_association", schema = "siyoliu")
//@IdClass(CollegeHighschoolAssociationEntityPK.class)
public class CollegeHighschoolAssociationEntity {
//    private int collegeId;
//    private int highschoolId;
    @ManyToOne
    @JoinColumn(name = "college_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private CollegeEntity collegeByCollegeId;
    @ManyToOne
    @JoinColumn(name = "highschool_id", referencedColumnName = "school_id", nullable = false, insertable = false, updatable = false)
    private HighschoolEntity highschoolByHighschoolId;
    @EmbeddedId
    private CollegeHighschoolAssociationEntityPK collegeHighschoolAssociationEntityPK;

//    @Id
//    @Column(name = "college_id", nullable = false, insertable = false, updatable = false)
//    public int getCollegeId() {
//        return collegeId;
//    }
//
//    public void setCollegeId(int collegeId) {
//        this.collegeId = collegeId;
//    }
//
//    @Id
//    @Column(name = "highschool_id", nullable = false, insertable = false, updatable = false)
//    public int getHighschoolId() {
//        return highschoolId;
//    }
//
//    public void setHighschoolId(int highschoolId) {
//        this.highschoolId = highschoolId;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollegeHighschoolAssociationEntity that = (CollegeHighschoolAssociationEntity) o;
        return Objects.equals(collegeHighschoolAssociationEntityPK, that.collegeHighschoolAssociationEntityPK);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collegeHighschoolAssociationEntityPK);
    }


    public CollegeEntity getCollegeByCollegeId() {
        return collegeByCollegeId;
    }

    public void setCollegeByCollegeId(CollegeEntity collegeByCollegeId) {
        this.collegeByCollegeId = collegeByCollegeId;
    }


    public HighschoolEntity getHighschoolByHighschoolId() {
        return highschoolByHighschoolId;
    }

    public void setHighschoolByHighschoolId(HighschoolEntity highschoolByHighschoolId) {
        this.highschoolByHighschoolId = highschoolByHighschoolId;
    }
}
