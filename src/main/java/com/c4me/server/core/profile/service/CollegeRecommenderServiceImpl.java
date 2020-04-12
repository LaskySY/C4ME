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




  public Map<String, String> recommendColleges(String username, List<String> colleges){
//    System.out.println(username);

    ProfileEntity thisUser = profileRepository.findByUsername(username);

    //student z score
    float satMath = 0;
    float satEbrw = 0;
    float actMath = 0;
    float actEng = 0;
    float actRead = 0;
    float actSci = 0;
    float actComp = 0;

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

    float studentZScore = (satMath + satEbrw + actMath + actEng + actRead + actSci + actComp)/7;

    List<CollegeEntity> collegeEntities = null;
    for(String c: colleges){
      CollegeEntity ce = collegeRepository.findByName(c);
      collegeEntities.add(ce);
    }

    Map<String, String> dataset = null;


    for(CollegeEntity c : collegeEntities){

      dataset.put(c.getName(), String.valueOf(Math.random()));

    }








    return dataset;


  }



}
