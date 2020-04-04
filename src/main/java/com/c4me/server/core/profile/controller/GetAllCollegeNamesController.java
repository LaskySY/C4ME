package com.c4me.server.core.profile.controller;

import com.c4me.server.core.profile.domain.CollegeLabel;
import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.domain.BaseResponse;
import com.c4me.server.entities.CollegeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-26-2020
 */

@RestController
@RequestMapping("/profile/college")
public class GetAllCollegeNamesController {

    @Autowired
    CollegeRepository collegeRepository;

    @GetMapping
    public BaseResponse<HashMap<String, List<CollegeLabel>>> getCollegeNames() {
        List<CollegeEntity> colleges = collegeRepository.findAllByOrderByName();
        List<CollegeLabel> collegeLabels = colleges.stream().map(e -> new CollegeLabel(e.getId(), e.getName())).collect(Collectors.toList()); //TODO: update once we have college alias table
        HashMap<String, List<CollegeLabel>> map = new HashMap<>();
        map.put("colleges", collegeLabels);
        return BaseResponse.<HashMap<String, List<CollegeLabel>>>builder()
                .code("success")
                .message("")
                .data(map).build();
    }
}
