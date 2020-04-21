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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: Controller for the findSimilarHighSchool request
 * @Author: Maciej Wlodek
 * @CreateDate: 04-10-2020
 */

@RestController
@RequestMapping("/findSimilarHighSchool")
public class SimilarHighSchoolController {

    @Autowired
    SimilarHighSchoolServiceImpl similarHighSchoolService;

    /**
     * Get the list of similar high schools to a given high school
     * @param username {@link String} the username of the student making the request
     * @param highschoolName {@link HSRequest} the name of the highschool to search for (must be wrapped as it is in the request body)
     * @return {@link List} of {@link HighschoolInfo2} for the N most similar high schools, sorted by similarity score
     * @throws IOException
     * @throws HighSchoolDoesNotExistException
     */
    @PostMapping
    @LogAndWrap(log = "getting similar high schools")
    public List<HighschoolInfo2> getSimilarHighSchools(@RequestParam String username, @RequestBody HSRequest highschoolName) throws IOException, HighSchoolDoesNotExistException {
        List<HighschoolEntity> similarHighschools = similarHighSchoolService.getSimilarHighSchools(highschoolName.getHighschoolName());
        if(similarHighschools == null || similarHighschools.size() == 0) return new ArrayList<HighschoolInfo2>();
        return similarHighschools.stream().map(HighschoolInfo2::new).collect(Collectors.toList());
    }
}
