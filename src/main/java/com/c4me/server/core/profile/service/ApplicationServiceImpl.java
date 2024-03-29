package com.c4me.server.core.profile.service;

import com.c4me.server.config.exception.CollegeDoesNotExistException;
import com.c4me.server.config.exception.UserDoesNotExistException;
import com.c4me.server.core.credential.repository.UserRepository;
import com.c4me.server.core.profile.domain.StudentApplication;
import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.core.profile.repository.ProfileRepository;
import com.c4me.server.core.profile.repository.StudentApplicationRepository;
import com.c4me.server.entities.*;
//import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.c4me.server.config.constant.Const.Questionable.*;
import static com.c4me.server.config.constant.Const.Ranges.MIN_STUDENTS_FOR_STDDEV;
import static com.c4me.server.config.constant.Const.Ranges.MIN_ZSCORE_FOR_QUESTIONABLE;
import static com.c4me.server.config.constant.Const.Status.*;

/**
 * @Description: Implementation of the application service
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
    @Autowired
    ProfileRepository profileRepository;


    /**
     * Get the list of all applications of a given student
     * @param username {@link String}
     * @return {@link ArrayList} of {@link StudentApplication}'s
     * @throws UserDoesNotExistException
     */
    public ArrayList<StudentApplication> getStudentApplications(String username) throws UserDoesNotExistException {
        UserEntity ue = userRepository.findByUsername(username);
        if (ue == null) throw new UserDoesNotExistException("user does not exist");

        List<StudentApplicationEntity> studentApplicationEntities = studentApplicationRepository.findAllByUserByUsername(ue);
        ArrayList<StudentApplication> applications = new ArrayList<>();
        for (StudentApplicationEntity studentApplicationEntity : studentApplicationEntities) {
            StudentApplication application = new StudentApplication(studentApplicationEntity);
            applications.add(application);
        }
        return applications;
    }

    /**
     * Compute a z-score of the student according to a fit normal distribution of the college scores.
     * @param studentScore {@link Integer} student score for a certain test
     * @param college25 {@link Integer} college 25th percentile score
     * @param college50 {@link Integer} college 50th percentile score
     * @param college75 {@link Integer} college 75th percentile score
     * @return double - the student's z-score
     */
    public double computeZScore(Integer studentScore, Integer college25, Integer college50, Integer college75) {
        if(studentScore == null || college25 == null || college50 == null || college75 == null) {
            return 0;
        }
        double mean = college50;
        double sigma = (college75 - college25)/(1.348);
        return (studentScore - mean)/(sigma);
    }

    /**
     * Compute whether a given acceptance or rejection decision should be deemed questionable by our system
     * @param ue {@link UserEntity}
     * @param ce {@link CollegeEntity}
     * @param status {@link Integer}
     * @return boolean - whether the decision is questionable or not
     */
    public boolean computeQuestionable(UserEntity ue, CollegeEntity ce, Integer status) {
        if(status == null || (!status.equals(ACCEPTED) && !status.equals(DENIED))) {
            return false; //only acceptances or rejections can be questionable
        }

        ProfileEntity pe = ue.getProfileByUsername();
        //ProfileEntity pe = profileRepository.findByUsername(ue.getUsername());
        if(pe == null) return false;

        boolean questionable = false;
        double mathZ = computeZScore(pe.getSatMath(), ce.getSatMath25(), ce.getSatMath50(), ce.getSatMath75());
        double ebrwZ = computeZScore(pe.getSatEbrw(), ce.getSatEbrw25(), ce.getSatEbrw50(), ce.getSatEbrw75());
        double actMZ = computeZScore(pe.getActMath(), ce.getActMath25(), ce.getActMath50(), ce.getActMath75());
        double actEZ = computeZScore(pe.getActEnglish(), ce.getActEnglish25(), ce.getActEnglish50(), ce.getActEnglish75());
        double actRZ = computeZScore(pe.getActReading(), ce.getActReading25(), ce.getActReading50(), ce.getActReading75());
        double actSZ = computeZScore(pe.getActScience(), ce.getActScience25(), ce.getActScience50(), ce.getActScience75());
        double[] scores = {mathZ, ebrwZ, actMZ, actEZ, actRZ, actSZ};
        for(double score : scores) {
            if(status.equals(ACCEPTED) && score < -MIN_ZSCORE_FOR_QUESTIONABLE) questionable = true;
            else if (status.equals(DENIED) && score > MIN_ZSCORE_FOR_QUESTIONABLE) questionable = true;
        }

        return questionable;
    }

    /**
     * Create or update a student application, checking if the decision as questionable
     * @param studentApplication {@link StudentApplication}
     * @throws UserDoesNotExistException
     * @throws CollegeDoesNotExistException
     */
    public void putStudentApplication(StudentApplication studentApplication) throws UserDoesNotExistException, CollegeDoesNotExistException { //TODO: should we copy bean properties instead?
        UserEntity ue = userRepository.findByUsername(studentApplication.getUsername());
        Optional<CollegeEntity> ce_opt = collegeRepository.findById(studentApplication.getCollegeId());

        if(ue == null) throw new UserDoesNotExistException("user does not exist");
        if(!ce_opt.isPresent()) throw new CollegeDoesNotExistException("college does not exist");

        CollegeEntity ce = ce_opt.get();

        boolean questionable = computeQuestionable(ue, ce, studentApplication.getStatus());

        StudentApplicationEntityPK studentApplicationEntityPK = StudentApplicationEntityPK.builder()
                .collegeId(ce.getId())
                .username(ue.getUsername()).build();
        StudentApplicationEntity studentApplicationEntity = StudentApplicationEntity.builder()
                .admission_term(studentApplication.getAdmissionTerm())
                .status(studentApplication.getStatus())
                .collegeByCollegeId(ce)
                .userByUsername(ue)
                .studentApplicationEntityPK(studentApplicationEntityPK)
                //.questionable(questionable? new Byte((byte) 1) : new Byte((byte) 0))
                .questionable(questionable? QUESTIONABLE : OK)
                .build();

        studentApplicationRepository.save(studentApplicationEntity);
    }

    /**
     * Delete a student application
     * @param studentApplication {@link StudentApplication}
     * @throws UserDoesNotExistException
     * @throws CollegeDoesNotExistException
     */
    public void deleteStudentApplication(StudentApplication studentApplication) throws UserDoesNotExistException, CollegeDoesNotExistException {
        System.out.println("deleting request");

        UserEntity ue = userRepository.findByUsername(studentApplication.getUsername());
        Optional<CollegeEntity> ce_opt = collegeRepository.findById(studentApplication.getCollegeId());

        //System.out.println("ue = " + ue.getName() + ", ce = " + ce.getName());

        if(ue == null) throw new UserDoesNotExistException("user does not exist");
        if(!ce_opt.isPresent()) throw new CollegeDoesNotExistException("college does not exist");

        CollegeEntity ce = ce_opt.get();

        System.out.println("ue = " + ue.getName() + ", ce = " + ce.getName());

        StudentApplicationEntityPK studentApplicationEntityPK = StudentApplicationEntityPK.builder()
                .username(ue.getUsername())
                .collegeId(ce.getId()).build();
        studentApplicationRepository.deleteByStudentApplicationEntityPK(studentApplicationEntityPK);
    }

}
