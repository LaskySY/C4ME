package com.c4me.server.core.profile.controller;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.core.profile.domain.CollegeLabel;
import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.domain.BaseResponse;
import com.c4me.server.entities.CollegeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: Controller for the get all college names service (for selecting a college in the frontend)
 * @Author: Maciej Wlodek
 * @CreateDate: 03-26-2020
 */

@RestController
@RequestMapping("/api/v1/profile/college")
public class GetAllCollegeNamesController {

    @Autowired
    CollegeRepository collegeRepository;

    /**
     * Controller for getting all college names service
     * @return {@link HashMap} with a single key containing the list of {@link CollegeLabel}'s
     */
    @GetMapping
    @LogAndWrap(log = "get all college names", wrap = true)
    public HashMap<String, List<CollegeLabel>> getCollegeNames() {
        List<CollegeEntity> colleges = collegeRepository.findAllByOrderByName();
        List<CollegeLabel> collegeLabels = colleges.stream().map(e -> new CollegeLabel(e.getId(), e.getName())).collect(Collectors.toList());
        HashMap<String, List<CollegeLabel>> map = new HashMap<>();
        map.put("colleges", collegeLabels);
        return map;
    }
}
