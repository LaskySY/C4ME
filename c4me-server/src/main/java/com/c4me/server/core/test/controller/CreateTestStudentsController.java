package com.c4me.server.core.test.controller;

import com.c4me.server.domain.BaseResponse;
import com.c4me.server.utils.TestingDataUtils;
import org.springframework.web.bind.annotation.*;

import static com.c4me.server.config.constant.Const.Filenames.DATA_DIR;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-22-2020
 */

@RestController
@RequestMapping("/createTestStudentsCSV")
public class CreateTestStudentsController {

    @PostMapping
    public BaseResponse generateCSV(@RequestParam Integer numStudents) {
        TestingDataUtils.generateStudents(numStudents, DATA_DIR + "students-random.csv");
        return BaseResponse.builder().code("success").message("").data(null).build();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/createStudentsAndApplicationsCSV")
    public BaseResponse generateStudentsAndAppsCSV(@RequestParam Integer numStudents, @RequestParam Integer numAppsPerStudent) {
        TestingDataUtils.generateStudentsAndApplications(numStudents, numAppsPerStudent, DATA_DIR + "students-random.csv", DATA_DIR + "applications-random.csv");
        return BaseResponse.builder().code("success").message("").data(null).build();
    }

}
