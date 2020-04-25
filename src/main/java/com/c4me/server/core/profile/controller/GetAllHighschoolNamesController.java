package com.c4me.server.core.profile.controller;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.core.profile.service.GetAllHighschoolNamesServiceImpl;
import com.c4me.server.domain.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: Controller for the getHighSchoolNames service (for selecting a high school in frontend)
 * @Author: Maciej Wlodek
 * @CreateDate: 03-26-2020
 */

@RestController
@RequestMapping("/profile/highSchool")

public class GetAllHighschoolNamesController {

    @Autowired
    GetAllHighschoolNamesServiceImpl getAllHighschoolNamesService;

    /**
     * Controller for getting the list of all high school names available on Niche.com
     * @return {@link HashMap} with a single key containing the list of high school names
     * @throws IOException
     */
    @GetMapping
    @LogAndWrap(log="get high school names", wrap=true)
    public HashMap<String, List<String>> getAllHighschoolNames() throws IOException {
        List<String> highschools = getAllHighschoolNamesService.getAllHighschoolNames();
        HashMap<String, List<String>> map = new HashMap<>();
        map.put("highSchools", highschools);
        return map;
    }
}
