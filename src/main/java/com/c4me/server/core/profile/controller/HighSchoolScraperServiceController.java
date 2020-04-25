package com.c4me.server.core.profile.controller;

import com.c4me.server.config.exception.HighSchoolDoesNotExistException;
import com.c4me.server.core.profile.service.HighSchoolScraperServiceImpl;
import com.c4me.server.domain.BaseResponse;
import com.c4me.server.utils.SearchHSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Description:  Test controller for the high school scraper service
 * @Author: Maciej Wlodek
 * @CreateDate: 03-22-2020
 */

@RestController
@RequestMapping
public class HighSchoolScraperServiceController {

    @Autowired
    HighSchoolScraperServiceImpl highSchoolScraperService;

    /**
     * Test method to scrape a high school given a query (which may or may not be a valid high school name)
     * @param hsQuery {@link String}
     * @return Empty {@link BaseResponse}
     * @throws IOException
     * @throws HighSchoolDoesNotExistException
     */
    @PostMapping("/testHighSchoolScrape")
    public BaseResponse scrapeHighSchool(@RequestParam String hsQuery) throws IOException, HighSchoolDoesNotExistException {
        if(hsQuery == null || hsQuery.equals("")) {
            return BaseResponse.builder().code("failure").message("empty query").data(null).build();
        }
        highSchoolScraperService.scrapeHighSchool(hsQuery, true);
        return BaseResponse.builder()
                .code("success")
                .message("")
                .data(null)
                .build();
    }

    /**
     * Test controller to find a high school url corresponding to a given high school query (may or may not be a valid hs name)
     * @param hsQuery {@link String}
     * @return {@link BaseResponse}
     * @throws IOException
     */
    //this request is for testing purposes only
    @PostMapping("/testFindHighSchoolURL")
    public BaseResponse<String> findHighSchoolUrls(@RequestParam String hsQuery) throws IOException {
        if(hsQuery == null || hsQuery.equals("")) {
            return BaseResponse.<String>builder().code("failure").message("empty query").data(null).build();
        }
//        List<String> matches = highSchoolScraperService.findMatches(hsQuery);
        String match = SearchHSUtils.searchForNicheUrl(hsQuery);
        return BaseResponse.<String>builder()
                .code("success")
                .message("")
                .data(match).build();
    }
}
