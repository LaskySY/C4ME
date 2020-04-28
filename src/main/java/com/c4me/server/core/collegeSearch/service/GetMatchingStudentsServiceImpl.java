package com.c4me.server.core.collegeSearch.service;


import static com.c4me.server.config.constant.Const.Status.STATUS_MAP;

import com.c4me.server.core.admin.domain.CollegeInfo;
import com.c4me.server.core.admin.domain.ProfileInfo;
import com.c4me.server.core.collegeSearch.domain.MatchingProfileFilter;
import com.c4me.server.core.collegeSearch.specifications.CollegeSearchFilterSpecification;
import com.c4me.server.core.collegeSearch.specifications.MatchingProfileFilterSpecification;
import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.core.profile.repository.ProfileRepository;
import com.c4me.server.core.profile.repository.StudentApplicationRepository;
import com.c4me.server.entities.CollegeEntity;
import com.c4me.server.entities.HighschoolEntity;
import com.c4me.server.entities.ProfileEntity;
import com.c4me.server.entities.StudentApplicationEntity;
import com.c4me.server.entities.UserEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Yousef Khan
 * @CreateDate: 04-20-2020
 */

@Service
public class GetMatchingStudentsServiceImpl {

  @Autowired
  ProfileRepository profileRepository;
  @Autowired
  StudentApplicationRepository studentApplicationRepository;
  @Autowired
  CollegeRepository collegeRepository;




  public List<ProfileInfo> getMatchingStudents(MatchingProfileFilter filter){


    if(filter.getApplicationStatus() != null){
      List<Integer> intList = new ArrayList<>();
      for(String appStatus: filter.getApplicationStatus()){
        Integer statusInt = STATUS_MAP.entrySet().stream().filter(e -> e.getValue().equals(appStatus)).collect(
            Collectors.toList()).get(0).getKey();
        intList.add(statusInt);
      }

      filter.setApplicationInts(intList);
    }

    MatchingProfileFilterSpecification specification = new MatchingProfileFilterSpecification(filter);
    System.out.println(specification);

    List<ProfileEntity> matchingProfiles = profileRepository.findAll(specification);

    List<ProfileInfo> matchingProfileInfo = new ArrayList<>();

    for(ProfileEntity pe : matchingProfiles){
      if(pe == null) continue;
      else {
        ProfileInfo matchedProfile = new ProfileInfo(pe);
//        matchedProfile.setApplicationStatus();
        matchingProfileInfo.add(matchedProfile);
      }
    }

    return matchingProfileInfo;

  }

}
