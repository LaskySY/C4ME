package com.c4me.server.core.admin.controller;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.core.admin.domain.CollegeInfo;
import com.c4me.server.core.admin.service.GetCollegeServiceImpl;
import com.c4me.server.domain.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-20-2020
 */


@RestController
@RequestMapping("/admin/college")
public class GetCollegeController {

    @Autowired
    GetCollegeServiceImpl getCollegeService;

    @GetMapping
    @LogAndWrap(log = "get all college info", wrap = false)
    public BaseResponse<HashMap<String, ArrayList<CollegeInfo>>> getCollegeInfo() {
        ArrayList<CollegeInfo> colleges = getCollegeService.getColleges();

        HashMap<String, ArrayList<CollegeInfo>> responseMap = new HashMap<>();
        responseMap.put("collegeInfo", colleges);

        return BaseResponse.<HashMap<String, ArrayList<CollegeInfo>>>builder()
                .code("success")
                .message("")
                .data(responseMap).build();
    }

}
