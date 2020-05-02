package com.c4me.server.core.admin.controller;

import com.c4me.server.core.admin.service.ScrapeCollegeRankingsServiceImpl;
import com.c4me.server.domain.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @Description: Controller for the scrapeCollegeRankings service
 * @Author: Maciej Wlodek
 * @CreateDate: 03-20-2020
 */

@RestController
@RequestMapping("/api/v1/admin/college/rank")
public class ScrapeCollegeRankingsController {

    @Autowired
    ScrapeCollegeRankingsServiceImpl scrapeCollegeRankingsService;

    /**
     * Controller for the scrapeCollegeRankings service
     * @return Empty {@link BaseResponse}
     * @throws IOException
     */
    @PostMapping
    public BaseResponse scrapeCollegeRankings() throws IOException {
        scrapeCollegeRankingsService.scrapeRankings();

        return BaseResponse.builder()
                .code("success")
                .message("")
                .data(null)
                .build();
    }
}
