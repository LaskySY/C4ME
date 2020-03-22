package com.c4me.server.core.profile.domain;

import com.c4me.server.entities.HighschoolEntity;
import com.c4me.server.entities.MajorEntity;
import com.c4me.server.entities.ProfileEntity;
import com.c4me.server.entities.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-15-2020
 */

@Setter
@Getter
@NoArgsConstructor
public class ProfileInfo {
    private String username;
    private String name;
    private Integer schoolYear;
    private String schoolName;
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

    public ProfileInfo(ProfileEntity pe) {
        this.username = pe.getUsername();
        this.name = pe.getUserByUsername().getName();
        this.schoolYear = pe.getSchoolYear();
        if(pe.getHighschoolBySchoolId() == null) {
            this.schoolName = null;
        }
        else {
            this.schoolName = pe.getHighschoolBySchoolId().getName();
        }
        this.numApCourses = pe.getNumApCourses();
        this.gpa = pe.getGpa();
        this.satMath = pe.getSatMath();
        this.satEbrw = pe.getSatEbrw();
        this.actMath = pe.getActMath();
        this.actEnglish = pe.getActEnglish();
        this.actReading = pe.getActReading();
        this.actScience = pe.getActScience();
        this.actComposite = pe.getActComposite();
        this.satLiterature = pe.getSatLiterature();
        this.satUsHist = pe.getSatUsHist();
        this.satWorldHist = pe.getSatWorldHist();
        this.satMathI = pe.getSatMathI();
        this.satMathIi = pe.getSatMathIi();
        this.satEcoBio = pe.getSatEcoBio();
        this.satMolBio = pe.getSatMolBio();
        this.satChemistry = pe.getSatChemistry();
        this.satPhysics = pe.getSatPhysics();
        this.major1 = pe.getMajor1();
        this.major2 = pe.getMajor2();
    }
}
