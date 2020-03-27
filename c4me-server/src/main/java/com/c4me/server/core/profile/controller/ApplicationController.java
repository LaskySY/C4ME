package com.c4me.server.core.profile.controller;

import com.c4me.server.config.exception.CollegeDoesNotExistException;
import com.c4me.server.config.exception.UserDoesNotExistException;
import com.c4me.server.core.profile.domain.ProfileRequest;
import com.c4me.server.core.profile.domain.StudentApplication;
import com.c4me.server.core.profile.service.ApplicationServiceImpl;
import com.c4me.server.domain.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-19-2020
 */

@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    ApplicationServiceImpl applicationService;

    @GetMapping
    public BaseResponse<HashMap<String, ArrayList<StudentApplication>>> getStudentApplicationsByProfile(@RequestParam String username) throws UserDoesNotExistException {
        //String user = username.getUsername();
        ArrayList<StudentApplication> applications = applicationService.getStudentApplications(username);

        HashMap<String, ArrayList<StudentApplication>> responseMap = new HashMap<>();
        responseMap.put("applications", applications);

        return BaseResponse.<HashMap<String, ArrayList<StudentApplication>>>builder()
                .code("success")
                .message("")
                .data(responseMap)
                .build();
    }

    @PostMapping
    public BaseResponse updateStudentApplication(@RequestParam String username, @RequestBody StudentApplication studentApplication) throws UserDoesNotExistException, CollegeDoesNotExistException {
        studentApplication.setUsername(username);
        System.out.println("username = " + username + ", college = " + studentApplication.getCollegeId());
        applicationService.putStudentApplication(studentApplication);
        return BaseResponse.builder()
                .code("success")
                .message("")
                .data(null)
                .build();
    }



    @DeleteMapping
    public BaseResponse deleteStudentApplication(@RequestBody StudentApplication studentApplication) throws UserDoesNotExistException, CollegeDoesNotExistException {
        applicationService.deleteStudentApplication(studentApplication);
        return BaseResponse.builder()
                .code("success")
                .message("")
                .data(null)
                .build();
    }

}
