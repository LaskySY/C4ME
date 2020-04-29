package com.c4me.server.core.admin.domain;

import com.c4me.server.config.constant.Const;
import com.c4me.server.entities.ProfileEntity;
import com.c4me.server.utils.TestingDataUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.BeanWrapperImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
  private Double weightedAvgPercentileScore;
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
    if(satMath != null && satEbrw != null)
      sat = satMath + satEbrw;

    //computeWeightedAveragePercentileScore();


  }

  public void computeWeightedAveragePercentileScore(Map<String, Map<Integer, Integer>> percentiles) {
//    System.out.println("computing percentiles for " + username);
    if(percentiles == null) return;
//    System.out.println(percentiles);

    BeanWrapperImpl beanWrapper = new BeanWrapperImpl(this);
    double subjectWeightTotal = 0;
    double scoreTotal = 0;
    for(String satSubject : Const.Percentiles.SAT_SUBJECT_TESTS) {
      Object score = beanWrapper.getPropertyValue(satSubject);
      if(score == null || !(score instanceof Integer)) {
        continue;
      }
      else {
        Integer scoreInt = (Integer) score;
        scoreInt = (scoreInt / 10) * 10;
        if(scoreInt >= Const.Ranges.MIN_SAT && scoreInt <= Const.Ranges.MAX_SAT) {
          Integer percentile = percentiles.get(satSubject).get(scoreInt);
//          System.out.println("percentile for " + satSubject + " = " + percentile);
          subjectWeightTotal += Const.Percentiles.SAT_SUBJECT_WEIGHT;
          scoreTotal += percentile * Const.Percentiles.SAT_SUBJECT_WEIGHT;
        }
      }
    }

//    System.out.println("subj = " + subjectWeightTotal);

    double remainingWeight = 1-subjectWeightTotal;
    int numRemainingTests = 0;
    for(String satMain : Const.Percentiles.SAT_MAIN_TESTS) {
      Object score = beanWrapper.getPropertyValue(satMain);
      if(score != null && (score instanceof Integer)) {
        numRemainingTests++;
      }
    }
    for(String act : Const.Percentiles.ACT_TESTS) {
      Object score = beanWrapper.getPropertyValue(act);
      if(score != null && (score instanceof Integer)) {
        numRemainingTests++;
      }
    }

    if (numRemainingTests == 0) {
      if(subjectWeightTotal == 0) weightedAvgPercentileScore = null;
      else weightedAvgPercentileScore = scoreTotal / subjectWeightTotal;
    }
    else {
      double remainingWeightForEach = remainingWeight / numRemainingTests;
      for(String satMain : Const.Percentiles.SAT_MAIN_TESTS) {
        Object score = beanWrapper.getPropertyValue(satMain);
        if(score == null || !(score instanceof Integer)) {
          continue;
        }
        else {
          Integer scoreInt = (Integer) score;
          scoreInt = (scoreInt / 10) * 10;
          if(scoreInt >= Const.Ranges.MIN_SAT && scoreInt <= Const.Ranges.MAX_SAT) {
            Integer percentile = percentiles.get(satMain).get(scoreInt);
//            System.out.println("percentile for " + satMain + " = " + percentile);
            scoreTotal += percentile * remainingWeightForEach;
          }
        }
      }
      for(String act : Const.Percentiles.ACT_TESTS) {
        Object score = beanWrapper.getPropertyValue(act);
        if(score == null || !(score instanceof Integer)) {
          continue;
        }
        else {
          Integer scoreInt = (Integer) score;
          if(scoreInt >= Const.Ranges.MIN_ACT && scoreInt <= Const.Ranges.MAX_ACT) {
            Integer percentile = percentiles.get(act).get(scoreInt);
//            System.out.println("percentile for " + act + " = " + percentile);
            scoreTotal += percentile * remainingWeightForEach;
          }
        }
      }
      weightedAvgPercentileScore = scoreTotal;
    }
  }
}
