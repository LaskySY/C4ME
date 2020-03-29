package com.c4me.server.core.admin.controller;

import com.c4me.server.config.exception.CollegeDoesNotExistException;
import com.c4me.server.config.exception.UserDoesNotExistException;
import com.c4me.server.core.admin.service.QuestionableDecisionsServiceImpl;
import com.c4me.server.core.profile.domain.StudentApplication;
import com.c4me.server.domain.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-24-2020
 */

@RestController
@RequestMapping("/admin/application")
public class QuestionableDecisionsController {

    @Autowired
    QuestionableDecisionsServiceImpl questionableDecisionsService;

    @GetMapping
    public BaseResponse<HashMap<String, List<StudentApplication>>> getQuestionableDecisions() {
        List<StudentApplication> questionableApplications = questionableDecisionsService.getQuestionableDecisions();
        HashMap<String, List<StudentApplication>> map = new HashMap<>();
        map.put("applications", questionableApplications);

        return BaseResponse.<HashMap<String, List<StudentApplication>>>builder().code("success").message("").data(map).build();
    }

    @PostMapping
    public BaseResponse changeQuestionableDecision(@RequestParam String username, @RequestParam Integer collegeId, @RequestParam Byte questionable) throws UserDoesNotExistException, CollegeDoesNotExistException {
        StudentApplication studentApplication = new StudentApplication();
        System.out.println("test");
        System.out.println(studentApplication.getUsername());
        System.out.println(username);
        System.out.println(collegeId);

        studentApplication.setUsername(username);
        studentApplication.setCollegeId(collegeId);
        studentApplication.setQuestionable(questionable);
        questionableDecisionsService.changeQuestionableDecision(studentApplication);
        return BaseResponse.builder().code("success").message("").data(null).build();
    }

}
