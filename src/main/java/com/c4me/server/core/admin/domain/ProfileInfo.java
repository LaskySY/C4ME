package com.c4me.server.core.admin.domain;

import com.c4me.server.entities.ProfileEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description:
 * @Author: Yousef Khan
 * @CreateDate: 04-27-2020
 */

@Setter
@Getter
@NoArgsConstructor
public class ProfileInfo {

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
  private Integer weightedAvgPercentileScore;
  private Integer sat;
  private String applicationStatus;




  public ProfileInfo(ProfileEntity profileEntity){
    username = profileEntity.getUsername();
    schoolYear = profileEntity.getSchoolYear();
    schoolId = profileEntity.getSchoolId();
    numApCourses = profileEntity.getNumApCourses();
    gpa = profileEntity.getGpa();
    satMath = profileEntity.getSatMath();
    satEbrw = profileEntity.getSatEbrw();
    actMath = profileEntity.getActMath();
    actEnglish = profileEntity.getActEnglish();
    actReading = profileEntity.getActReading();
    actScience = profileEntity.getActScience();
    actComposite = profileEntity.getActComposite();
    satLiterature = profileEntity.getSatLiterature();
    satUsHist = profileEntity.getSatUsHist();
    satWorldHist = profileEntity.getSatWorldHist();
    satMathI = profileEntity.getSatMathI();
    satMathIi = profileEntity.getSatMathIi();
    satEcoBio = profileEntity.getSatEcoBio();
    satMolBio = profileEntity.getSatMolBio();
    satChemistry = profileEntity.getSatChemistry();
    satPhysics = profileEntity.getSatPhysics();
    major1 = profileEntity.getMajor1();
    major2 = profileEntity.getMajor2();
    sat = satMath + satEbrw;


  }

}
