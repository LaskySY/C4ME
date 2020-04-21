package com.c4me.server.core.test.controller;

import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.domain.BaseResponse;
import com.c4me.server.entities.CollegeEntity;
import com.c4me.server.utils.TestingDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.c4me.server.config.constant.Const.Filenames.DATA_DIR;

/**
 * @Description: Controller for methods to create random testing data
 * @Author: Maciej Wlodek
 * @CreateDate: 03-22-2020
 */

@RestController
@RequestMapping("/createTestStudentsCSV")
public class CreateTestStudentsController {

    @Autowired
    CollegeRepository collegeRepository;

    /**
     * Generate a csv of random students
     * @param numStudents {@link Integer} - the number of students to generate
     * @return Empty {@link BaseResponse}
     */
    @PostMapping
    public BaseResponse generateCSV(@RequestParam Integer numStudents) {
        TestingDataUtils.generateStudents(numStudents, DATA_DIR + "students-random.csv");
        return BaseResponse.builder().code("success").message("").data(null).build();
    }

    /**
     * Generate csv of random students and csv of random applications
     * @param numStudents {@link Integer} - the number of students to generate
     * @param numAppsPerStudent {@link Integer} - the average number of applications to generate per student
     * @return Empty {@link BaseResponse}
     */
    @RequestMapping(method = RequestMethod.POST, path = "/createStudentsAndApplicationsCSV")
    public BaseResponse generateStudentsAndAppsCSV(@RequestParam Integer numStudents, @RequestParam Integer numAppsPerStudent) {
        List<CollegeEntity> collegeEntities = collegeRepository.findAll();
        TestingDataUtils.generateStudentsAndApplications(numStudents, numAppsPerStudent, collegeEntities, DATA_DIR + "students-random.csv", DATA_DIR + "applications-random.csv");
        return BaseResponse.builder().code("success").message("").data(null).build();
    }

}
