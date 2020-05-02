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
 * @Description: Controller for the getAllCollegeInfo service
 * @Author: Maciej Wlodek
 * @CreateDate: 03-20-2020
 */


@RestController
@RequestMapping("/api/v1/admin/college")
public class GetCollegeController {

    @Autowired
    GetCollegeServiceImpl getCollegeService;

    /**
     * Controller for the getAllCollegeInfo service
     * @return HashMap with a single element containing the list of {@link CollegeInfo}'s
     */
    @GetMapping
    @LogAndWrap(log = "get all college info", wrap = true)
    public HashMap<String, ArrayList<CollegeInfo>> getCollegeInfo() {
        ArrayList<CollegeInfo> colleges = getCollegeService.getColleges();

        HashMap<String, ArrayList<CollegeInfo>> responseMap = new HashMap<>();
        responseMap.put("collegeInfo", colleges);

        return responseMap;
    }

}
