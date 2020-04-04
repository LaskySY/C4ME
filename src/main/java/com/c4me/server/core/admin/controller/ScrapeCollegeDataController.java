package com.c4me.server.core.admin.controller;

import com.c4me.server.config.exception.NoCollegeTxtException;
import com.c4me.server.core.admin.service.ScrapeCollegeDataServiceImpl;
import com.c4me.server.domain.BaseResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.Iterator;
import java.util.List;

import static com.c4me.server.config.constant.Const.Filenames.*;

/**
 * @Description:
 * @Author: Yousef Khan
 * @CreateDate: 03-22-2020
 */

@RestController
@RequestMapping("/admin/college/data")
public class ScrapeCollegeDataController {

  @Autowired
  ScrapeCollegeDataServiceImpl scrapeCollegeDataService;

  @PostMapping
  public BaseResponse scrapeCollegeData() throws IOException {
    System.out.println("mapping works");

    scrapeCollegeDataService.scrapeCollegeData();


    return BaseResponse.builder()
        .code("success")
        .message("")
        .data(null).build();

  }




}
