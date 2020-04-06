package com.c4me.server.core.profile.controller;

import com.c4me.server.core.profile.service.GetAllHighschoolNamesServiceImpl;
import com.c4me.server.domain.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-26-2020
 */

@RestController
@RequestMapping("/profile/highSchool")

public class GetAllHighschoolNamesController {

    @Autowired
    GetAllHighschoolNamesServiceImpl getAllHighschoolNamesService;

    @GetMapping
    public BaseResponse<HashMap<String, List<String>>> getAllHighschoolNames() throws IOException {
        List<String> highschools = getAllHighschoolNamesService.getAllHighschoolNames();
        HashMap<String, List<String>> map = new HashMap<>();
        map.put("highSchools", highschools);
        return BaseResponse.<HashMap<String, List<String>>>builder()
                .code("success")
                .message("")
                .data(map).build();
    }
}
