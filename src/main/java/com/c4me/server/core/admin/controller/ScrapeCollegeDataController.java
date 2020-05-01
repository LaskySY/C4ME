package com.c4me.server.core.admin.controller;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.config.exception.NoCollegeTxtException;
import com.c4me.server.core.admin.service.ScrapeCollegeDataServiceImpl;
import com.c4me.server.domain.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

import static com.c4me.server.config.constant.Const.Filenames.*;

/**
 * @Description:
 * @Author: Yousef Khan
 * @CreateDate: 03-22-2020
 */

@RestController
@RequestMapping("/api/v1/admin/college/data")
public class ScrapeCollegeDataController {

  @Autowired
  ScrapeCollegeDataServiceImpl scrapeCollegeDataService;

  @PostMapping
  @LogAndWrap(log="scraping college data")
  public void scrapeCollegeData() throws IOException {
    scrapeCollegeDataService.scrapeCollegeData();
  }




}
