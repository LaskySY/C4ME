package com.c4me.server.core.collegeSearch.controller;

import com.c4me.server.core.admin.domain.ProfileInfo;
import com.c4me.server.core.admin.service.DeleteAllProfileServiceImpl;
import com.c4me.server.core.collegeSearch.domain.MatchingProfileFilter;
import com.c4me.server.core.collegeSearch.service.GetMatchingStudentsServiceImpl;
import com.c4me.server.domain.BaseResponse;
import com.c4me.server.entities.ProfileEntity;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: Yousef Khan
 * @CreateDate: 04-20-2020
 */

@RestController
@RequestMapping("/matchStudents")
public class GetMatchingStudentsController {

  @Autowired
  GetMatchingStudentsServiceImpl getMatchingStudentsService;

  @PostMapping
  public BaseResponse getMatchingProfiles(@RequestBody MatchingProfileFilter filter) throws IOException {

    List<ProfileInfo> profiles = getMatchingStudentsService.getMatchingStudents(filter);

    return BaseResponse.builder()
        .code("success")
        .message("")
        .data(profiles)
        .build();

  }

}
