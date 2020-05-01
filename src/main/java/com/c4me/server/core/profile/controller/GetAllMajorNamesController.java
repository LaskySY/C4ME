package com.c4me.server.core.profile.controller;

import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.core.profile.repository.MajorRepository;
import com.c4me.server.core.profile.service.MajorAliasTable;
import com.c4me.server.entities.MajorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: Controller for the getMajorNames service (for selecting major in frontend)
 * @Author: Maciej Wlodek
 * @CreateDate: 04-13-2020
 */

@RestController
@RequestMapping("/api/v1/collegeSearch/getmajor")
public class GetAllMajorNamesController {

    @Autowired
    MajorRepository majorRepository;

    /**
     * @return {@link List} of college names found in our database
     */
    @GetMapping
    @LogAndWrap(log="get all major names", wrap=true)
    public List<String> majorNames() {
       return majorRepository.findAll().stream().map(MajorEntity::getName).collect(Collectors.toList());
    }
}
