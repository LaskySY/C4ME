package com.c4me.server.core.admin.controller;

import com.c4me.server.config.annotation.LogAndWrap;
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
 * @Description: Controller for the questionableDecisions service
 * @Author: Maciej Wlodek
 * @CreateDate: 03-24-2020
 */

@RestController
@RequestMapping("/api/v1/admin/application")
public class QuestionableDecisionsController {

    @Autowired
    QuestionableDecisionsServiceImpl questionableDecisionsService;

    /**
     * Controller for the getQuestionableDecisions service
     * @return HashMap with a single element containing the {@link StudentApplication}'s
     */
    @GetMapping
    @LogAndWrap(log="get questionable decision", wrap=true)
    public HashMap<String, List<StudentApplication>> getQuestionableDecisions() {
        List<StudentApplication> questionableApplications = questionableDecisionsService.getQuestionableDecisions();
        HashMap<String, List<StudentApplication>> map = new HashMap<>();
        map.put("applications", questionableApplications);

        return map;
    }

    /**
     * Controller for the changeQuestionableDecision service
     * @param username the username of the student whose application is to be marked
     * @param studentApplication the updated student application info
     * @return Empty {@link BaseResponse}
     * @throws UserDoesNotExistException
     * @throws CollegeDoesNotExistException
     */
    @PostMapping
    public BaseResponse changeQuestionableDecision(@RequestParam String username,  @RequestBody StudentApplication studentApplication) throws UserDoesNotExistException, CollegeDoesNotExistException {
        System.out.println("test");
        System.out.println(studentApplication.getUsername());

        studentApplication.setUsername(username);
        questionableDecisionsService.changeQuestionableDecision(studentApplication);
        return BaseResponse.builder().code("success").message("").data(null).build();
    }

}
