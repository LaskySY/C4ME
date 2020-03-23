package com.c4me.server.core.test.controller;

import com.c4me.server.domain.BaseResponse;
import com.c4me.server.utils.TestingDataUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
