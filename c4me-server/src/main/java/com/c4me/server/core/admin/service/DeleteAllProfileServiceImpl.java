package com.c4me.server.core.admin.service;

import com.c4me.server.core.profile.repository.ProfileRepository;
import com.c4me.server.entities.ProfileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: Yousef Khan
 * @CreateDate: 03-21-2020
 */

@Service
public class DeleteAllProfileServiceImpl {

  @Autowired
  ProfileRepository profileRepository;

  //delete all profiles
  public void deleteAllProfiles() {
    System.out.println("deleted all");
    profileRepository.deleteAll();
  }

  //delete 1 profile
  public void deleteProfile(ProfileEntity profile){
    profileRepository.delete(profile);
  }


}
