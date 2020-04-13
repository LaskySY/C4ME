package com.c4me.server.core.profile.service;

import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.core.profile.repository.ProfileRepository;
import com.c4me.server.entities.CollegeEntity;
import com.c4me.server.entities.ProfileEntity;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Yousef Khan
 * @CreateDate: 04-07-2020
 */

@Service
public class CollegeRecommenderServiceImpl {

  @Autowired
  ProfileRepository profileRepository;

  @Autowired
  CollegeRepository collegeRepository;

  public float normalize(Integer attr, Integer attr_min, Integer attr_max){
    return (attr - attr_min)/(attr_max - attr_min);
  }

  public double computeZScore(Integer studentScore, Integer college25, Integer college75) {
    if(studentScore == null || college25 == null || college75 == null) {
      return 0;
    }
    double mean = (college75 - college25)/(2);
    double sigma = (college75 - college25)/(1.348);
    return Math.abs((studentScore - mean)/(sigma));
  }

  public double computeZScoreActComp(Integer studentScore, Integer collegeScore) {
    if(studentScore == null || collegeScore == null) {
      return 0;
    }
    double mean = collegeScore;
    double sigma = (collegeScore)/(1.348);
    return Math.abs((studentScore - mean)/(sigma));
  }




  public Map<String, String> recommendColleges(String username, List<String> colleges){
//    System.out.println(username);

    ProfileEntity thisUser = profileRepository.findByUsername(username);

    //student z score
    double satMath = 0;
    double satEbrw = 0;
    double actMath = 0;
    double actEng = 0;
    double actRead = 0;
    double actSci = 0;
    double actComp = 0;

    if(thisUser.getSatMath() != null) {
      satMath = normalize(thisUser.getSatMath(), 200, 800);
    }

    if(thisUser.getSatEbrw() != null){
      satEbrw = normalize(thisUser.getSatEbrw(), 200, 800);
    }

    if(thisUser.getActMath() != null){
      actMath = normalize(thisUser.getActMath(), 1, 36);
    }

    if(thisUser.getActEnglish() != null){
      actEng = normalize(thisUser.getActEnglish(), 1, 36);
    }

    if(thisUser.getActReading() != null){
      actRead = normalize(thisUser.getActReading(), 1, 36);
    }

    if(thisUser.getActScience() != null){
      actSci = normalize(thisUser.getActScience(), 1, 36);
    }

    if(thisUser.getActComposite() != null){
      actComp = normalize(thisUser.getActComposite(), 1, 36);
    }

    double studentZScore = (satMath + satEbrw + actMath + actEng + actRead + actSci + actComp)/7;

    List<CollegeEntity> collegeEntities = null;
    for(String c: colleges){
      CollegeEntity ce = collegeRepository.findByName(c);
      collegeEntities.add(ce);
    }

    Map<String, String> dataset = null;


    for(CollegeEntity c : collegeEntities){

      double mathZ = computeZScore(thisUser.getSatMath(), c.getSatMath25(), c.getSatMath75());
      double ebrwZ = computeZScore(thisUser.getSatEbrw(), c.getSatEbrw25(), c.getSatEbrw75());
      double actMZ = computeZScore(thisUser.getActMath(), c.getActMath25(), c.getActMath75());
      double actEZ = computeZScore(thisUser.getActEnglish(), c.getActEnglish25(), c.getActEnglish75());
      double actRZ = computeZScore(thisUser.getActReading(), c.getActReading25(), c.getActReading75());
      double actSZ = computeZScore(thisUser.getActScience(), c.getActScience25(), c.getActScience75());
      double actCompZ = computeZScoreActComp(thisUser.getActComposite(), c.getActComposite());

      double collegeZScore = (mathZ + ebrwZ + actMZ + actEZ + actRZ + actSZ + actCompZ)/7;

      dataset.put(c.getName(), String.valueOf(collegeZScore));

    }








    return dataset;


  }



}
