package com.c4me.server.core.profile.controller;

import com.c4me.server.config.exception.HighSchoolDoesNotExistException;
import com.c4me.server.core.profile.service.HighSchoolScraperServiceImpl;
import com.c4me.server.domain.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:  For testing purposes only
 * @Author: Maciej Wlodek
 * @CreateDate: 03-22-2020
 */

@RestController
@RequestMapping
public class HighSchoolScraperServiceController {

    @Autowired
    HighSchoolScraperServiceImpl highSchoolScraperService;

    // this request is for testing purposes only
    @PostMapping("/testHighSchoolScrape")
    public BaseResponse scrapeHighSchool(@RequestParam String hsQuery) throws IOException, HighSchoolDoesNotExistException {
        if(hsQuery == null || hsQuery.equals("")) {
            return BaseResponse.builder().code("failure").message("empty query").data(null).build();
        }
        highSchoolScraperService.scrapeHighSchool(hsQuery);
        return BaseResponse.builder()
                .code("success")
                .message("")
                .data(null)
                .build();
    }

    //this request is for testing purposes only
    @PostMapping("/testFindHighSchoolURL")
    public BaseResponse<List<String>> findHighSchoolUrls(@RequestParam String hsQuery) throws IOException {
        if(hsQuery == null || hsQuery.equals("")) {
            return BaseResponse.<List<String>>builder().code("failure").message("empty query").data(null).build();
        }
        List<String> matches = highSchoolScraperService.findMatches(hsQuery);
        return BaseResponse.<List<String>>builder()
                .code("success")
                .message("")
                .data(matches).build();
    }
}