package com.c4me.server.core.collegeSearch.controller;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.core.admin.domain.CollegeInfo;
import com.c4me.server.core.collegeSearch.domain.CollegeRecommendationRequest;
import com.c4me.server.core.collegeSearch.service.CollegeRecommendationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 04-12-2020
 */

@RestController
@RequestMapping("/collegeRecommender")
public class CollegeRecommendationController {

    @Autowired
    CollegeRecommendationServiceImpl collegeRecommendationService;

    @PostMapping
    @LogAndWrap(log="get college recommendation score")
    public List<CollegeInfo> computeCollegeRecommendationScores(@RequestParam String username, @RequestBody CollegeRecommendationRequest collegeList) {
        return collegeRecommendationService.computeCollegeRecommendationScores(username, collegeList.getCollegeList());
    }

}
