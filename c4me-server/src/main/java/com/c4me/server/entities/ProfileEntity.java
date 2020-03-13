package com.c4me.server.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "Profile", schema = "siyoliu")
public class ProfileEntity {
    private String username;
    private Integer schoolYear;
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
    private Timestamp createTime;
    private Timestamp updateTime;

    @Id
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "school_year")
    public Integer getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(Integer schoolYear) {
        this.schoolYear = schoolYear;
    }

    @Basic
    @Column(name = "num_ap_courses")
    public Integer getNumApCourses() {
        return numApCourses;
    }

    public void setNumApCourses(Integer numApCourses) {
        this.numApCourses = numApCourses;
    }

    @Basic
    @Column(name = "gpa")
    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    @Basic
    @Column(name = "SAT_math")
    public Integer getSatMath() {
        return satMath;
    }

    public void setSatMath(Integer satMath) {
        this.satMath = satMath;
    }

    @Basic
    @Column(name = "SAT_EBRW")
    public Integer getSatEbrw() {
        return satEbrw;
    }

    public void setSatEbrw(Integer satEbrw) {
        this.satEbrw = satEbrw;
    }

    @Basic
    @Column(name = "ACT_math")
    public Integer getActMath() {
        return actMath;
    }

    public void setActMath(Integer actMath) {
        this.actMath = actMath;
    }

    @Basic
    @Column(name = "ACT_english")
    public Integer getActEnglish() {
        return actEnglish;
    }

    public void setActEnglish(Integer actEnglish) {
        this.actEnglish = actEnglish;
    }

    @Basic
    @Column(name = "ACT_reading")
    public Integer getActReading() {
        return actReading;
    }

    public void setActReading(Integer actReading) {
        this.actReading = actReading;
    }

    @Basic
    @Column(name = "ACT_science")
    public Integer getActScience() {
        return actScience;
    }

    public void setActScience(Integer actScience) {
        this.actScience = actScience;
    }

    @Basic
    @Column(name = "ACT_composite")
    public Integer getActComposite() {
        return actComposite;
    }

    public void setActComposite(Integer actComposite) {
        this.actComposite = actComposite;
    }

    @Basic
    @Column(name = "SAT_literature")
    public Integer getSatLiterature() {
        return satLiterature;
    }

    public void setSatLiterature(Integer satLiterature) {
        this.satLiterature = satLiterature;
    }

    @Basic
    @Column(name = "SAT_US_hist")
    public Integer getSatUsHist() {
        return satUsHist;
    }

    public void setSatUsHist(Integer satUsHist) {
        this.satUsHist = satUsHist;
    }

    @Basic
    @Column(name = "SAT_world_hist")
    public Integer getSatWorldHist() {
        return satWorldHist;
    }

    public void setSatWorldHist(Integer satWorldHist) {
        this.satWorldHist = satWorldHist;
    }

    @Basic
    @Column(name = "SAT_math_I")
    public Integer getSatMathI() {
        return satMathI;
    }

    public void setSatMathI(Integer satMathI) {
        this.satMathI = satMathI;
    }

    @Basic
    @Column(name = "SAT_math_II")
    public Integer getSatMathIi() {
        return satMathIi;
    }

    public void setSatMathIi(Integer satMathIi) {
        this.satMathIi = satMathIi;
    }

    @Basic
    @Column(name = "SAT_eco_bio")
    public Integer getSatEcoBio() {
        return satEcoBio;
    }

    public void setSatEcoBio(Integer satEcoBio) {
        this.satEcoBio = satEcoBio;
    }

    @Basic
    @Column(name = "SAT_mol_bio")
    public Integer getSatMolBio() {
        return satMolBio;
    }

    public void setSatMolBio(Integer satMolBio) {
        this.satMolBio = satMolBio;
    }

    @Basic
    @Column(name = "SAT_chemistry")
    public Integer getSatChemistry() {
        return satChemistry;
    }

    public void setSatChemistry(Integer satChemistry) {
        this.satChemistry = satChemistry;
    }

    @Basic
    @Column(name = "SAT_physics")
    public Integer getSatPhysics() {
        return satPhysics;
    }

    public void setSatPhysics(Integer satPhysics) {
        this.satPhysics = satPhysics;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_time")
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
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, schoolYear, numApCourses, gpa, satMath, satEbrw, actMath, actEnglish, actReading, actScience, actComposite, satLiterature, satUsHist, satWorldHist, satMathI, satMathIi, satEcoBio, satMolBio, satChemistry, satPhysics, createTime, updateTime);
    }
}
