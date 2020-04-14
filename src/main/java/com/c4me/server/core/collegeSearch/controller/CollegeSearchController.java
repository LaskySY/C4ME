package com.c4me.server.core.collegeSearch.controller;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.core.admin.domain.CollegeInfo;
import com.c4me.server.core.collegeSearch.domain.CollegeSearchFilter;
import com.c4me.server.core.collegeSearch.service.CollegeSearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 04-06-2020
 */

@RestController
@RequestMapping("/collegeSearch")
public class CollegeSearchController {

    @Autowired
    CollegeSearchServiceImpl collegeSearchService;

    @PostMapping
    @LogAndWrap(log="search for colleges", wrap=true)
    public List<CollegeInfo> searchForColleges(@RequestParam String username, @RequestBody CollegeSearchFilter filter) {
        System.out.println(filter);
        return collegeSearchService.getSearchResults(username, filter);
    }

}
