package com.c4me.server.core.admin.controller;

import com.c4me.server.core.admin.service.DeleteAllProfileServiceImpl;
import com.c4me.server.domain.BaseResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

/**
 * @Description:
 * @Author: Yousef Khan
 * @CreateDate: 03-28-2020
 */

@RestController
@RequestMapping("/admin/profile/delete")
public class DeleteAllProfileController {

  @Autowired
  DeleteAllProfileServiceImpl deleteAllProfileService;

  @PostMapping
  public BaseResponse DeleteAllProfiles() throws IOException {
    System.out.println("mapping works");

    deleteAllProfileService.deleteAllProfiles();

    return BaseResponse.builder()
        .code("success")
        .message("")
        .data(null).build();

  }

}
