package com.c4me.server.core.profile.controller;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.config.exception.CollegeDoesNotExistException;
import com.c4me.server.config.exception.UserDoesNotExistException;
import com.c4me.server.core.profile.domain.StudentApplication;
import com.c4me.server.core.profile.service.ApplicationServiceImpl;
import com.c4me.server.domain.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Description: Controller for the application service
 * @Author: Maciej Wlodek
 * @CreateDate: 03-19-2020
 */

@RestController
@RequestMapping("/application")
public class ApplicationController {

    @Autowired
    ApplicationServiceImpl applicationService;

    /**
     * Controller for getting a list of student applications
     * @param username {@link String}
     * @return {@link HashMap} with a single key containing the list of {@link StudentApplication}'s
     * @throws UserDoesNotExistException
     */
    @GetMapping
    @LogAndWrap(log="get student applications", wrap=true)
    public HashMap<String, ArrayList<StudentApplication>> getStudentApplicationsByProfile(@RequestParam String username) throws UserDoesNotExistException {
        ArrayList<StudentApplication> applications = applicationService.getStudentApplications(username);

        HashMap<String, ArrayList<StudentApplication>> responseMap = new HashMap<>();
        responseMap.put("applications", applications);
        return responseMap;
    }

    /**
     * Controller for updating a student application
     * @param username {@link String}
     * @param studentApplication {@link StudentApplication}
     * @return empty {@link BaseResponse}
     * @throws UserDoesNotExistException
     * @throws CollegeDoesNotExistException
     */
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


    /**
     * Controller for deleting a student application
     * @param username {@link String}
     * @param studentApplication {@link StudentApplication}
     * @return Empty {@link BaseResponse}
     * @throws UserDoesNotExistException
     * @throws CollegeDoesNotExistException
     */
    @PostMapping("/delete")
    public BaseResponse deleteStudentApplication(@RequestParam String username, @RequestBody StudentApplication studentApplication) throws UserDoesNotExistException, CollegeDoesNotExistException {
        studentApplication.setUsername(username);
        applicationService.deleteStudentApplication(studentApplication);
        return BaseResponse.builder()
                .code("success")
                .message("")
                .data(null)
                .build();
    }

}
