package com.c4me.server.core.collegeSearch.service;

import com.c4me.server.core.admin.domain.CollegeInfo;
import com.c4me.server.core.collegeSearch.domain.CollegeSearchFilter;
import com.c4me.server.core.credential.repository.HighschoolRepository;
import com.c4me.server.core.profile.repository.ProfileRepository;
import com.c4me.server.entities.HighschoolEntity;
import com.c4me.server.entities.ProfileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 04-06-2020
 */

@Service
public class CollegeSearchServiceImpl {

    @Autowired
    ProfileRepository profileRepository;


    //use the home state if sorting by cost (for in-state vs out of state)
    private String getHomeState(String username) {
        if(username == null) return null;
        String homeState = null;
        ProfileEntity profileEntity = profileRepository.findByUsername(username);
        if(profileEntity != null) {
            HighschoolEntity highschoolEntity = profileEntity.getHighschoolBySchoolId();
            if(highschoolEntity != null) {
                homeState = highschoolEntity.getState();
            }
        }
        return homeState;
    }
    public List<CollegeInfo> getSearchResults(String username, CollegeSearchFilter filter) {
        String homeState = getHomeState(username);

        /*TODO: to order conditionally, try:
        SELECT ... FROM ... ORDER BY (CASE WHEN (outStateTuition is NULL OR (State == homeState AND Type == PUBLIC)) THEN inStateTuition ELSE outStateTuition END)
         */




        return null;
    }

}
