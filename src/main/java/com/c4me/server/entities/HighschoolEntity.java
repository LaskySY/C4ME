package com.c4me.server.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-15-2020
 */

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "Highschool", schema = "siyoliu")
public class HighschoolEntity {
    private int schoolId;
    private String name;
    private String type;
    private Integer ranking;
    private String city;
    private String state;
    private Integer satMath;
    private Integer satEbrw;
    private Integer satOverall;
    private Integer actMath;
    private Integer actEnglish;
    private Integer actReading;
    private Integer actScience;
    private Integer actComposite;
    private String academicQuality;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Collection<CollegeHighschoolAssociationEntity> collegeHighschoolAssociationsBySchoolId;
    private Collection<HighschoolMajorAssociationEntity> highschoolMajorAssociationsBySchoolId;
    private Collection<ProfileEntity> profilesBySchoolId;

//    @Id
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "school_id", nullable = false)
    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 128)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "type", nullable = true, length = 45)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "ranking", nullable = true)
    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    @Basic
    @Column(name = "city", nullable = true, length = 45)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "state", nullable = true, length = 45)
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Basic
    @Column(name = "SAT_math", nullable = true)
    public Integer getSatMath() {
        return satMath;
    }

    public void setSatMath(Integer satMath) {
        this.satMath = satMath;
    }

    @Basic
    @Column(name = "SAT_EBRW", nullable = true)
    public Integer getSatEbrw() {
        return satEbrw;
    }

    public void setSatEbrw(Integer satEbrw) {
        this.satEbrw = satEbrw;
    }

    @Basic
    @Column(name = "SAT_overall", nullable = true)
    public Integer getSatOverall() {
        return satOverall;
    }

    public void setSatOverall(Integer satOverall) {
        this.satOverall = satOverall;
    }

    @Basic
    @Column(name = "ACT_math", nullable = true)
    public Integer getActMath() {
        return actMath;
    }

    public void setActMath(Integer actMath) {
        this.actMath = actMath;
    }

    @Basic
    @Column(name = "ACT_english", nullable = true)
    public Integer getActEnglish() {
        return actEnglish;
    }

    public void setActEnglish(Integer actEnglish) {
        this.actEnglish = actEnglish;
    }

    @Basic
    @Column(name = "ACT_reading", nullable = true)
    public Integer getActReading() {
        return actReading;
    }

    public void setActReading(Integer actReading) {
        this.actReading = actReading;
    }

    @Basic
    @Column(name = "ACT_science", nullable = true)
    public Integer getActScience() {
        return actScience;
    }

    public void setActScience(Integer actScience) {
        this.actScience = actScience;
    }

    @Basic
    @Column(name = "ACT_composite", nullable = true)
    public Integer getActComposite() {
        return actComposite;
    }

    public void setActComposite(Integer actComposite) {
        this.actComposite = actComposite;
    }

    @Basic
    @Column(name = "academic_quality", nullable = true)
    public String getAcademicQuality() {
        return academicQuality;
    }

    public void setAcademicQuality(String academicQuality) {
        this.academicQuality = academicQuality;
    }

    @Basic
    @UpdateTimestamp
    @Column(name = "update_time", nullable = true)
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @CreationTimestamp
    @Column(name = "create_time", nullable = true)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HighschoolEntity that = (HighschoolEntity) o;
        return schoolId == that.schoolId &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Objects.equals(ranking, that.ranking) &&
                Objects.equals(city, that.city) &&
                Objects.equals(state, that.state) &&
                Objects.equals(satMath, that.satMath) &&
                Objects.equals(satEbrw, that.satEbrw) &&
                Objects.equals(satOverall, that.satOverall) &&
                Objects.equals(actMath, that.actMath) &&
                Objects.equals(actEnglish, that.actEnglish) &&
                Objects.equals(actReading, that.actReading) &&
                Objects.equals(actScience, that.actScience) &&
                Objects.equals(actComposite, that.actComposite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schoolId, name, type, ranking, city, state, satMath, satEbrw, satOverall, actMath, actEnglish, actReading, actScience, actComposite);
    }

    @OneToMany(mappedBy = "highschoolByHighschoolId")
    public Collection<CollegeHighschoolAssociationEntity> getCollegeHighschoolAssociationsBySchoolId() {
        return collegeHighschoolAssociationsBySchoolId;
    }

    public void setCollegeHighschoolAssociationsBySchoolId(Collection<CollegeHighschoolAssociationEntity> collegeHighschoolAssociationsBySchoolId) {
        this.collegeHighschoolAssociationsBySchoolId = collegeHighschoolAssociationsBySchoolId;
    }

    @OneToMany(mappedBy = "highschoolByHighschoolId")
    public Collection<HighschoolMajorAssociationEntity> getHighschoolMajorAssociationsBySchoolId() {
        return highschoolMajorAssociationsBySchoolId;
    }

    public void setHighschoolMajorAssociationsBySchoolId(Collection<HighschoolMajorAssociationEntity> highschoolMajorAssociationsBySchoolId) {
        this.highschoolMajorAssociationsBySchoolId = highschoolMajorAssociationsBySchoolId;
    }

    @OneToMany(mappedBy = "highschoolBySchoolId")
    public Collection<ProfileEntity> getProfilesBySchoolId() {
        return profilesBySchoolId;
    }

    public void setProfilesBySchoolId(Collection<ProfileEntity> profilesBySchoolId) {
        this.profilesBySchoolId = profilesBySchoolId;
    }
}
