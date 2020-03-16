package com.c4me.server.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-15-2020
 */

@Entity
@Table(name = "Profile", schema = "siyoliu")
public class ProfileEntity {
    private String username;
    private Integer schoolYear;
    private Integer schoolId;
    private Integer numApCourses;
    private Double gpa;
    private Integer satMath;
    private Integer satEbrw;
    private Integer actMath;
    private Integer actEnglish;
    private Integer actReading;
    private Integer actScience;
    private Integer actComposite;
    private Integer satLiterature;
    private Integer satUsHist;
    private Integer satWorldHist;
    private Integer satMathI;
    private Integer satMathIi;
    private Integer satEcoBio;
    private Integer satMolBio;
    private Integer satChemistry;
    private Integer satPhysics;
    private String major1;
    private String major2;
    private Timestamp createTime;
    private Timestamp updateTime;
    private UserEntity userByUsername;
    private HighschoolEntity highschoolBySchoolId;
    private MajorEntity majorByMajor1;
    private MajorEntity majorByMajor2;

    @Id
    @Column(name = "username", nullable = false, length = 255)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "school_year", nullable = true)
    public Integer getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(Integer schoolYear) {
        this.schoolYear = schoolYear;
    }

    @Basic
    @Column(name = "school_id", nullable = true, insertable = false, updatable = false)
    public Integer getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Integer schoolId) {
        this.schoolId = schoolId;
    }

    @Basic
    @Column(name = "num_ap_courses", nullable = true)
    public Integer getNumApCourses() {
        return numApCourses;
    }

    public void setNumApCourses(Integer numApCourses) {
        this.numApCourses = numApCourses;
    }

    @Basic
    @Column(name = "gpa", nullable = true, precision = 0)
    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
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
    @Column(name = "SAT_literature", nullable = true)
    public Integer getSatLiterature() {
        return satLiterature;
    }

    public void setSatLiterature(Integer satLiterature) {
        this.satLiterature = satLiterature;
    }

    @Basic
    @Column(name = "SAT_US_hist", nullable = true)
    public Integer getSatUsHist() {
        return satUsHist;
    }

    public void setSatUsHist(Integer satUsHist) {
        this.satUsHist = satUsHist;
    }

    @Basic
    @Column(name = "SAT_world_hist", nullable = true)
    public Integer getSatWorldHist() {
        return satWorldHist;
    }

    public void setSatWorldHist(Integer satWorldHist) {
        this.satWorldHist = satWorldHist;
    }

    @Basic
    @Column(name = "SAT_math_I", nullable = true)
    public Integer getSatMathI() {
        return satMathI;
    }

    public void setSatMathI(Integer satMathI) {
        this.satMathI = satMathI;
    }

    @Basic
    @Column(name = "SAT_math_II", nullable = true)
    public Integer getSatMathIi() {
        return satMathIi;
    }

    public void setSatMathIi(Integer satMathIi) {
        this.satMathIi = satMathIi;
    }

    @Basic
    @Column(name = "SAT_eco_bio", nullable = true)
    public Integer getSatEcoBio() {
        return satEcoBio;
    }

    public void setSatEcoBio(Integer satEcoBio) {
        this.satEcoBio = satEcoBio;
    }

    @Basic
    @Column(name = "SAT_mol_bio", nullable = true)
    public Integer getSatMolBio() {
        return satMolBio;
    }

    public void setSatMolBio(Integer satMolBio) {
        this.satMolBio = satMolBio;
    }

    @Basic
    @Column(name = "SAT_chemistry", nullable = true)
    public Integer getSatChemistry() {
        return satChemistry;
    }

    public void setSatChemistry(Integer satChemistry) {
        this.satChemistry = satChemistry;
    }

    @Basic
    @Column(name = "SAT_physics", nullable = true)
    public Integer getSatPhysics() {
        return satPhysics;
    }

    public void setSatPhysics(Integer satPhysics) {
        this.satPhysics = satPhysics;
    }

    @Basic
    @Column(name = "major1", nullable = true, length = 45, insertable = false, updatable = false)
    public String getMajor1() {
        return major1;
    }

    public void setMajor1(String major1) {
        this.major1 = major1;
    }

    @Basic
    @Column(name = "major2", nullable = true, length = 45, insertable = false, updatable = false)
    public String getMajor2() {
        return major2;
    }

    public void setMajor2(String major2) {
        this.major2 = major2;
    }

    @Basic
    @Column(name = "create_time", nullable = true)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_time", nullable = true)
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileEntity that = (ProfileEntity) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(schoolYear, that.schoolYear) &&
                Objects.equals(schoolId, that.schoolId) &&
                Objects.equals(numApCourses, that.numApCourses) &&
                Objects.equals(gpa, that.gpa) &&
                Objects.equals(satMath, that.satMath) &&
                Objects.equals(satEbrw, that.satEbrw) &&
                Objects.equals(actMath, that.actMath) &&
                Objects.equals(actEnglish, that.actEnglish) &&
                Objects.equals(actReading, that.actReading) &&
                Objects.equals(actScience, that.actScience) &&
                Objects.equals(actComposite, that.actComposite) &&
                Objects.equals(satLiterature, that.satLiterature) &&
                Objects.equals(satUsHist, that.satUsHist) &&
                Objects.equals(satWorldHist, that.satWorldHist) &&
                Objects.equals(satMathI, that.satMathI) &&
                Objects.equals(satMathIi, that.satMathIi) &&
                Objects.equals(satEcoBio, that.satEcoBio) &&
                Objects.equals(satMolBio, that.satMolBio) &&
                Objects.equals(satChemistry, that.satChemistry) &&
                Objects.equals(satPhysics, that.satPhysics) &&
                Objects.equals(major1, that.major1) &&
                Objects.equals(major2, that.major2) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, schoolYear, schoolId, numApCourses, gpa, satMath, satEbrw, actMath, actEnglish, actReading, actScience, actComposite, satLiterature, satUsHist, satWorldHist, satMathI, satMathIi, satEcoBio, satMolBio, satChemistry, satPhysics, major1, major2, createTime, updateTime);
    }

    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username", nullable = false)
    public UserEntity getUserByUsername() {
        return userByUsername;
    }

    public void setUserByUsername(UserEntity userByUsername) {
        this.userByUsername = userByUsername;
    }

    @ManyToOne
    @JoinColumn(name = "school_id", referencedColumnName = "school_id")
    public HighschoolEntity getHighschoolBySchoolId() {
        return highschoolBySchoolId;
    }

    public void setHighschoolBySchoolId(HighschoolEntity highschoolBySchoolId) {
        this.highschoolBySchoolId = highschoolBySchoolId;
    }

    @ManyToOne
    @JoinColumn(name = "major1", referencedColumnName = "name")
    public MajorEntity getMajorByMajor1() {
        return majorByMajor1;
    }

    public void setMajorByMajor1(MajorEntity majorByMajor1) {
        this.majorByMajor1 = majorByMajor1;
    }

    @ManyToOne
    @JoinColumn(name = "major2", referencedColumnName = "name")
    public MajorEntity getMajorByMajor2() {
        return majorByMajor2;
    }

    public void setMajorByMajor2(MajorEntity majorByMajor2) {
        this.majorByMajor2 = majorByMajor2;
    }
}
