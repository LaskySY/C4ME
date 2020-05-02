package com.c4me.server.core.admin.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.c4me.server.config.constant.Const.Questionable.OK;
import static com.c4me.server.config.constant.Const.Questionable.QUESTIONABLE;

/**
 * @Description: Implementation of the questionableDecisions service
 * @Author: Maciej Wlodek
 * @CreateDate: 03-24-2020
 */

@Service
public class QuestionableDecisionsServiceImpl {

    @Autowired
    StudentApplicationRepository studentApplicationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CollegeRepository collegeRepository;

    /**
     * @return {@link List} of all {@link StudentApplication}'s found in the database
     */
    public List<StudentApplication> getQuestionableDecisions() {
        List<StudentApplicationEntity> applicationEntities = studentApplicationRepository.findAllByQuestionable(QUESTIONABLE);
        List<StudentApplication> applications = applicationEntities.stream().map(StudentApplication::new).collect(Collectors.toList());
        return applications;
    }

    /**
     * Change a questionable decision to either {@literal OK} or {@literal DISHONEST}
     * @param studentApplication the {@link StudentApplication} object to update in the database
     * @throws UserDoesNotExistException
     * @throws CollegeDoesNotExistException
     */
    public void changeQuestionableDecision(StudentApplication studentApplication) throws UserDoesNotExistException, CollegeDoesNotExistException {
        UserEntity ue = userRepository.findByUsername(studentApplication.getUsername());
        Optional<CollegeEntity> ce_opt = collegeRepository.findById(studentApplication.getCollegeId());

        if(ue == null) throw new UserDoesNotExistException("user does not exist");
        if(!ce_opt.isPresent()) throw new CollegeDoesNotExistException("college does not exist");

        CollegeEntity ce = ce_opt.get();

        StudentApplicationEntityPK studentApplicationEntityPK = StudentApplicationEntityPK.builder()
                .collegeId(ce.getId())
                .username(ue.getUsername()).build();

        Optional<StudentApplicationEntity> sae = studentApplicationRepository.findById(studentApplicationEntityPK);
        if(!sae.isPresent()) throw new UserDoesNotExistException("application does not exist");
        StudentApplicationEntity studentApplicationEntity = sae.get();
        studentApplicationEntity.setQuestionable(studentApplication.getQuestionable());

//        StudentApplicationEntity studentApplicationEntity = StudentApplicationEntity.builder()
//                .admission_term(studentApplication.getAdmissionTerm())
//                .status(studentApplication.getStatus())
//                .collegeByCollegeId(ce)
//                .userByUsername(ue)
//                .studentApplicationEntityPK(studentApplicationEntityPK)
//                //.questionable(questionable? new Byte((byte) 1) : new Byte((byte) 0))
//                .questionable(studentApplication.getQuestionable())
//                .build();

        studentApplicationRepository.save(studentApplicationEntity);
    }
}
