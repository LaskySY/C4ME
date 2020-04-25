package com.c4me.server.core.admin.service;

import com.c4me.server.core.admin.domain.CollegeInfo;
import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.entities.CollegeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: Implementation for the getCollegeInfo service
 * @Author: Maciej Wlodek
 * @CreateDate: 03-20-2020
 */

@Service
public class GetCollegeServiceImpl {

    @Autowired
    CollegeRepository collegeRepository;

    /**
     * Get all college info objects from the database
     * @return {@link ArrayList} of all {@link CollegeInfo} objects found in the database
     */
    public ArrayList<CollegeInfo> getColleges() {
        List<CollegeEntity> colleges = collegeRepository.findAll();
        ArrayList<CollegeInfo> collegeInfos = (ArrayList<CollegeInfo>) colleges.stream().map(CollegeInfo::new).collect(Collectors.toList());
        return collegeInfos;
    }

}
