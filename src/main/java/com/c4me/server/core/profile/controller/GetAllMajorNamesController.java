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
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 04-13-2020
 */

@RestController
@RequestMapping("/getAllMajorNames")
public class GetAllMajorNamesController {

    @Autowired
    MajorRepository majorRepository;

    @GetMapping
    @LogAndWrap(log="get all major names")
    public List<String> majorNames() {
       return majorRepository.findAll().stream().map(MajorEntity::getName).collect(Collectors.toList());
    }
}
