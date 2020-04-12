package com.c4me.server.core.highschoolSearch.controller;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.config.exception.HighSchoolDoesNotExistException;
import com.c4me.server.core.highschoolSearch.domain.HSRequest;
import com.c4me.server.core.highschoolSearch.domain.HighschoolInfo;
import com.c4me.server.core.highschoolSearch.domain.HighschoolInfo2;
import com.c4me.server.core.highschoolSearch.service.SimilarHighSchoolServiceImpl;
import com.c4me.server.entities.HighschoolEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 04-10-2020
 */

@RestController
@RequestMapping("/findSimilarHighSchool")
public class SimilarHighSchoolController {

    @Autowired
    SimilarHighSchoolServiceImpl similarHighSchoolService;

    @PostMapping
    @LogAndWrap(log = "getting similar high schools")
    public List<HighschoolInfo2> getSimilarHighSchools(@RequestParam String username, @RequestBody HSRequest highschoolName) throws IOException, HighSchoolDoesNotExistException {
        System.out.println("getting similar highschools");
        System.out.println(username);
        System.out.println(highschoolName.getHighschoolName());

        List<HighschoolEntity> similarHighschools = similarHighSchoolService.getSimilarHighSchools(highschoolName.getHighschoolName());
        if(similarHighschools == null) return null;
        return similarHighschools.stream().map(HighschoolInfo2::new).collect(Collectors.toList());
    }
}
