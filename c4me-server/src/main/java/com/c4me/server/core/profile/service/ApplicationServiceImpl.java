package com.c4me.server.core.profile.service;

import com.c4me.server.config.exception.CollegeDoesNotExistException;
import com.c4me.server.config.exception.UserDoesNotExistException;
import com.c4me.server.core.credential.repository.UserRepository;
import com.c4me.server.core.profile.domain.StudentApplication;
import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.core.profile.repository.StudentApplicationRepository;
import com.c4me.server.entities.CollegeEntity;
import com.c4me.server.entities.StudentApplicationEntity;
import com.c4me.server.entities.StudentApplicationEntityPK;
import com.c4me.server.entities.UserEntity;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-19-2020
 */

@Service
@Transactional
public class ApplicationServiceImpl {

    @Autowired
    StudentApplicationRepository studentApplicationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CollegeRepository collegeRepository;


    public ArrayList<StudentApplication> getStudentApplications(String username) throws UserDoesNotExistException {
        UserEntity ue = userRepository.findByUsername(username);
        if(ue == null) throw new UserDoesNotExistException("user does not exist");

        List<StudentApplicationEntity> studentApplicationEntities = studentApplicationRepository.findAllByUserByUsername(ue);
        ArrayList<StudentApplication> applications = new ArrayList<>();
        for(StudentApplicationEntity studentApplicationEntity : studentApplicationEntities) {
            StudentApplication application = new StudentApplication(studentApplicationEntity);
            applications.add(application);
        }
        return applications;
    }

    public void putStudentApplication(StudentApplication studentApplication) throws UserDoesNotExistException, CollegeDoesNotExistException {
        UserEntity ue = userRepository.findByUsername(studentApplication.getUsername());
        CollegeEntity ce = collegeRepository.findByName(studentApplication.getCollegeName());

        if(ue == null) throw new UserDoesNotExistException("user does not exist");
        if(ce == null) throw new CollegeDoesNotExistException("college does not exist");

        //TODO: Compute questionable if status is either accepted or denied

        StudentApplicationEntityPK studentApplicationEntityPK = StudentApplicationEntityPK.builder()
                .collegeId(ce.getId())
                .username(ue.getUsername()).build();
        StudentApplicationEntity studentApplicationEntity = StudentApplicationEntity.builder()
                .admission_term(studentApplication.getAdmissionTerm())
                .status(studentApplication.getStatus())
                .collegeByCollegeId(ce)
                .userByUsername(ue)
                .studentApplicationEntityPK(studentApplicationEntityPK)
                .build();

        studentApplicationRepository.save(studentApplicationEntity);
    }

    public void deleteStudentApplication(StudentApplication studentApplication) throws UserDoesNotExistException, CollegeDoesNotExistException {
        UserEntity ue = userRepository.findByUsername(studentApplication.getUsername());
        CollegeEntity ce = collegeRepository.findByName(studentApplication.getCollegeName());

        if(ue == null) throw new UserDoesNotExistException("user does not exist");
        if(ce == null) throw new CollegeDoesNotExistException("college does not exist");

        StudentApplicationEntityPK studentApplicationEntityPK = StudentApplicationEntityPK.builder()
                .username(ue.getUsername())
                .collegeId(ce.getId()).build();
        studentApplicationRepository.deleteByStudentApplicationEntityPK(studentApplicationEntityPK);
    }

}
