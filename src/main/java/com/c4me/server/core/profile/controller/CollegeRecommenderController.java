package com.c4me.server.core.profile.controller;

import com.c4me.server.core.profile.service.CollegeRecommenderServiceImpl;
import com.c4me.server.domain.BaseResponse;
import com.c4me.server.entities.CollegeEntity;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

import static com.c4me.server.config.constant.Const.Filenames.*;

/**
 * @Description:
 * @Author: Yousef Khan
 * @CreateDate: 04-07-2020
 */

@RestController
@RequestMapping("/collegeRecommender")
public class CollegeRecommenderController {

  @Autowired
  CollegeRecommenderServiceImpl collegeRecommenderService;

  @PostMapping
  public BaseResponse recommendColleges(@RequestParam String username, List<String> colleges) throws IOException {
    System.out.println("mapping works");

    Map<String, String> recommended = collegeRecommenderService.recommendColleges(username, colleges);


    return BaseResponse.builder()
        .code("success")
        .message("")
        .data(recommended)
        .build();

  }




}
