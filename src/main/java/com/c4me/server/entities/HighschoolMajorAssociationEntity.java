package com.c4me.server.entities;

import javax.persistence.*;
import java.util.Objects;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-15-2020
 */

@Entity
@Table(name = "Highschool_major_association", schema = "siyoliu")
//@IdClass(HighschoolMajorAssociationEntityPK.class)
public class HighschoolMajorAssociationEntity {
//    private int highschoolId;
//    private String majorName;
    @ManyToOne
    @JoinColumn(name = "highschool_id", referencedColumnName = "school_id", nullable = false, insertable = false, updatable = false)
    private HighschoolEntity highschoolByHighschoolId;
    @ManyToOne
    @JoinColumn(name = "major_name", referencedColumnName = "name", nullable = false, insertable = false, updatable = false)
    private MajorEntity majorByMajorName;
    @EmbeddedId
    private HighschoolMajorAssociationEntityPK highschoolMajorAssociationEntityPK;

//    @Id
//    @Column(name = "highschool_id", nullable = false)
//    public int getHighschoolId() {
//        return highschoolId;
//    }
//
//    public void setHighschoolId(int highschoolId) {
//        this.highschoolId = highschoolId;
//    }
//
//    @Id
//    @Column(name = "major_name", nullable = false, length = 45)
//    public String getMajorName() {
//        return majorName;
//    }
//
//    public void setMajorName(String majorName) {
//        this.majorName = majorName;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HighschoolMajorAssociationEntity that = (HighschoolMajorAssociationEntity) o;
        return
                Objects.equals(highschoolMajorAssociationEntityPK, that.highschoolMajorAssociationEntityPK);
    }

    @Override
    public int hashCode() {
        return Objects.hash(highschoolMajorAssociationEntityPK);
    }


    public HighschoolEntity getHighschoolByHighschoolId() {
        return highschoolByHighschoolId;
    }

    public void setHighschoolByHighschoolId(HighschoolEntity highschoolByHighschoolId) {
        this.highschoolByHighschoolId = highschoolByHighschoolId;
    }


    public MajorEntity getMajorByMajorName() {
        return majorByMajorName;
    }

    public void setMajorByMajorName(MajorEntity majorByMajorName) {
        this.majorByMajorName = majorByMajorName;
    }
}
