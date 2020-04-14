package com.c4me.server.core.collegeSearch.controller;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.core.admin.domain.CollegeInfo;
import com.c4me.server.core.collegeSearch.service.CollegeSearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 04-13-2020
 */

@RestController
@RequestMapping("/college")
public class GetCollegeInfoController {

    @Autowired
    CollegeSearchServiceImpl collegeSearchService;

    @GetMapping
    @LogAndWrap(log="get college info", wrap=true)
    public CollegeInfo getCollegeInfo(@RequestParam String name) {
        return collegeSearchService.getCollegeInfo(name);
    }
}
