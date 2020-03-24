package com.c4me.server.core.admin.service;

import com.c4me.server.core.profile.domain.StudentApplication;
import com.c4me.server.core.profile.repository.StudentApplicationRepository;
import com.c4me.server.entities.StudentApplicationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.c4me.server.config.constant.Const.Questionable.QUESTIONABLE;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-24-2020
 */

@Service
public class GetQuestionableDecisionsServiceImpl {

    @Autowired
    StudentApplicationRepository studentApplicationRepository;

    public List<StudentApplication> getQuestionableDecisions() {
        List<StudentApplicationEntity> applicationEntities = studentApplicationRepository.findAllByQuestionable(QUESTIONABLE);
        List<StudentApplication> applications = applicationEntities.stream().map(StudentApplication::new).collect(Collectors.toList());
        return applications;
    }
}
