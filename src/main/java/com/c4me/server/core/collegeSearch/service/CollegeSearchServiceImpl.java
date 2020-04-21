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
 * @Description: Implementation of the collegeSearch service
 * @Author: Maciej Wlodek
 * @CreateDate: 04-06-2020
 */

@Service
public class CollegeSearchServiceImpl {

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    CollegeRepository collegeRepository;


    /**
     * Compute the home state of the user making the request
     * @param username {@link String} the username of the student making the request
     * @return {@link String} the home state of the user
     */
    //use the home state if sorting by cost (for in-state vs out of state)
    private String getHomeState(String username) {
        if(username == null) return "";
        String homeState = "";
        ProfileEntity profileEntity = profileRepository.findByUsername(username);
        if(profileEntity != null) {
            HighschoolEntity highschoolEntity = profileEntity.getHighschoolBySchoolId();
            if(highschoolEntity != null) {
                homeState = highschoolEntity.getState();
            }
        }
        return homeState;
    }

    /**
     * Execute the query requested by the student and retrieve the search results
     * @param username {@link String} the username of the student making the search
     * @param filter {@link CollegeSearchFilter} the filter object embodying the query made by the student
     * @return {@link List} of {@link CollegeInfo} containing the sorted search results
     */
    public List<CollegeInfo> getSearchResults(String username, CollegeSearchFilter filter) {
        String homeState = getHomeState(username);

        final CollegeSearchFilterSpecification specification = new CollegeSearchFilterSpecification(filter, homeState);
        List<CollegeEntity> results = collegeRepository.findAll(specification);

        List<CollegeInfo> resultsInfo = results.stream().map(e -> new CollegeInfo(e, homeState)).collect(Collectors.toList());
        return resultsInfo;
    }

    /**
     * Get information about a single college
     * @param name {@link String} college name
     * @return {@link CollegeInfo} or null
     */
    public CollegeInfo getCollegeInfo(String name) {
        CollegeEntity collegeEntity = collegeRepository.findByName(name);
        if(collegeEntity == null) return null;
        else return new CollegeInfo(collegeEntity);
    }

}
