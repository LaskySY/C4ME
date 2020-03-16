package com.c4me.server.core.profile.service;

import com.c4me.server.config.exception.UserDoesNotExistException;
import com.c4me.server.core.profile.domain.ProfileInfo;
import com.c4me.server.core.profile.repository.ProfileRepository;
import com.c4me.server.entities.ProfileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-15-2020
 */

@Service
public class ProfileServiceImpl {

    @Autowired
    ProfileRepository profileRepository;

    public ProfileInfo getInfoByUsername(String username) throws UserDoesNotExistException {
        ProfileEntity pe = profileRepository.findByUsername(username);
        if(pe == null) {
            throw new UserDoesNotExistException("cannot find user");
        }
        ProfileInfo pi = new ProfileInfo();
        pi.setName(pe.getUserByUsername().getName());
        pi.setGpa(pe.getGpa());
        pi.setNumApCourses(pe.getNumApCourses());
        pi.setSatMath(pe.getSatMath());
        pi.setUsername(username);
        return pi;
    }
}
