package com.c4me.server.core.admin.controller;

import com.c4me.server.core.admin.service.GetQuestionableDecisionsServiceImpl;
import com.c4me.server.core.profile.domain.StudentApplication;
import com.c4me.server.domain.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-24-2020
 */

@RestController
@RequestMapping("/admin/application")
public class GetQuestionableDecisionsController {

    @Autowired
    GetQuestionableDecisionsServiceImpl getQuestionableDecisionsService;

    @GetMapping
    public BaseResponse<List<StudentApplication>> getQuestionableDecisions() {
        List<StudentApplication> questionableApplications = getQuestionableDecisionsService.getQuestionableDecisions();
        return BaseResponse.<List<StudentApplication>>builder().code("success").message("").data(questionableApplications).build();
    }

}
