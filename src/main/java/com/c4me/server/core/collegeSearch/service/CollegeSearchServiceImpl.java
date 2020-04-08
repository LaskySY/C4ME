package com.c4me.server.core.collegeSearch.service;

import com.c4me.server.core.admin.domain.CollegeInfo;
import com.c4me.server.core.collegeSearch.domain.CollegeSearchFilter;
import com.c4me.server.core.collegeSearch.specifications.CollegeSearchFilterSpecification;
import com.c4me.server.core.credential.repository.HighschoolRepository;
import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.core.profile.repository.ProfileRepository;
import com.c4me.server.entities.CollegeEntity;
import com.c4me.server.entities.HighschoolEntity;
import com.c4me.server.entities.ProfileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 04-06-2020
 */

@Service
public class CollegeSearchServiceImpl {

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    CollegeRepository collegeRepository;


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
        System.out.println(homeState);

        final CollegeSearchFilterSpecification specification = new CollegeSearchFilterSpecification(filter, homeState);
        List<CollegeEntity> results = collegeRepository.findAll(specification);

        List<CollegeInfo> resultsInfo = results.stream().map(CollegeInfo::new).collect(Collectors.toList());
        return resultsInfo;
    }

}
