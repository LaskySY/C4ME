package com.c4me.server.core.highschoolSearch.service;

import com.c4me.server.config.constant.Const;
import com.c4me.server.config.exception.HighSchoolDoesNotExistException;
import com.c4me.server.core.credential.repository.HighschoolRepository;
import com.c4me.server.core.highschoolSearch.specifications.AcceptanceSpecification;
import com.c4me.server.core.profile.repository.StudentApplicationRepository;
import com.c4me.server.core.profile.service.HighSchoolScraperServiceImpl;
import com.c4me.server.entities.*;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.c4me.server.config.constant.Const.AcademicQuality.LETTER_TO_NUMBER_GRADE;
import static com.c4me.server.config.constant.Const.Ranges.*;
import static com.c4me.server.config.constant.Const.SimilarHS.*;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 04-10-2020
 */

@Service
public class SimilarHighSchoolServiceImpl {

    @Autowired
    HighschoolRepository highschoolRepository;
    @Autowired
    StudentApplicationRepository studentApplicationRepository;
    @Autowired
    HighSchoolScraperServiceImpl highSchoolScraperService;

    boolean debug = false;

    private void debug(String arg) { if(debug) System.out.println(arg); }

    public List<HighschoolEntity> getSimilarHighSchools(String highschoolName) throws IOException, HighSchoolDoesNotExistException {
        HighschoolEntity highschoolEntity = highschoolRepository.findByName(highschoolName);
        if(highschoolEntity == null) { //if it's not in the database, try to scrape it
            highschoolEntity = highSchoolScraperService.scrapeHighSchool(highschoolName, false);
            if(highschoolEntity == null) return null;
        }

        List<HighschoolEntity> allHighschools = highschoolRepository.findAll();
        Map<HighschoolEntity, Double> distances = new HashMap<>();
        for(HighschoolEntity highschoolEntity1 : allHighschools) {
            debug("dist between " + highschoolEntity.getName() + ", and " + highschoolEntity1.getName());
            distances.put(highschoolEntity1, computeSimilarityScore(highschoolEntity, highschoolEntity1));
        }
        List<Map.Entry<HighschoolEntity, Double>> sortedDistances = new ArrayList<>(distances.entrySet());
        sortedDistances.sort(Map.Entry.comparingByValue());
        if(sortedDistances.size() > MAX_SCHOOLS) sortedDistances = sortedDistances.subList(0, MAX_SCHOOLS);
        return sortedDistances.stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    private Double computeSimilarityScore(HighschoolEntity h1, HighschoolEntity h2) {
        Double testDistance = computeTestSimilarityDistance(h1, h2);
        //Double studentDistance = computeStudentSimilarityDistance(h1, h2);
        Double studentDistance = null;   // the student distance metric is too time-consuming; esp since used for college recommendation
        Double academicDistance = computeAcademicQualityDistance(h1, h2);
        debug("test = " + testDistance);
        debug("student = " + studentDistance);
        debug("academic = " + academicDistance);
        Double score = 0.0;
        score += (testDistance == null)? MISSING_TEST_PENALTY * TEST_FACTOR_WEIGHT : testDistance * TEST_FACTOR_WEIGHT;
        score += (studentDistance == null)? MISSING_STUDENT_PENALTY * STUDENT_FACTOR_WEIGHT : studentDistance * STUDENT_FACTOR_WEIGHT;
        score += (academicDistance == null)? MISSING_ACADEMIC_PENALTY * ACADEMIC_FACTOR_WEIGHT : academicDistance * ACADEMIC_FACTOR_WEIGHT;
        return score;
    }

    private Double computeAcademicQualityDistance(HighschoolEntity h1, HighschoolEntity h2) {
        if(h1.getAcademicQuality() == null || h2.getAcademicQuality() == null) return null;
        Integer qualityH1 = LETTER_TO_NUMBER_GRADE.get(h1.getAcademicQuality());
        Integer qualityH2 = LETTER_TO_NUMBER_GRADE.get(h2.getAcademicQuality());
        return ((double) Math.abs(qualityH1 - qualityH2)) / ((double) LETTER_TO_NUMBER_GRADE.get("A+"));
    }

    private Double computeStudentSimilarityDistance(HighschoolEntity h1, HighschoolEntity h2) {
        List<ProfileEntity> school1Students = (List<ProfileEntity>) h1.getProfilesBySchoolId();
        List<ProfileEntity> school2Students = (List<ProfileEntity>) h2.getProfilesBySchoolId();
        Double average1 = getAverageTopAcceptance(school1Students);
        Double average2 = getAverageTopAcceptance(school2Students);
        if(average1 == null || average2 == null) {
            System.out.println("one of the schools has no acceptances");
            return null;
        }
        return Math.abs(average1 - average2) / 800.0;
    }

    private Double getAverageTopAcceptance(List<ProfileEntity> profileEntities) {
        Integer sum = 0;
        Integer count = 0;
//        for(ProfileEntity pe : profileEntities) {
//            List<StudentApplicationEntity> studentApplicationEntities = (List<StudentApplicationEntity>) pe.getUserByUsername().getStudentApplicationsByUsername();
//        }
        List<Integer> maxAcceptances = profileEntities.stream().map(e -> getMaxAcceptance(e)).collect(Collectors.toList());
        for(Integer acceptance : maxAcceptances) {
            if(acceptance != null) {
                sum += acceptance;
                count++;
            }
        }
        if(count == 0) return null;
        return ((double) sum) / ((double) count);
    }

    private Integer getMaxAcceptance(ProfileEntity profileEntity) {
        UserEntity userEntity = profileEntity.getUserByUsername();
        AcceptanceSpecification acceptanceSpecification = new AcceptanceSpecification(userEntity);
        List<StudentApplicationEntity> apps = studentApplicationRepository.findAll(acceptanceSpecification);
        if(apps.size() == 0) return null;
        else {
            debug("ranking = " + apps.get(0).getCollegeByCollegeId().getRanking());
            return apps.get(0).getCollegeByCollegeId().getRanking();
        }
    }

    private Double computeTestSimilarityDistance(HighschoolEntity h1, HighschoolEntity h2) {
        BeanWrapperImpl h1Wrapper = new BeanWrapperImpl(h1);
        BeanWrapperImpl h2Wrapper = new BeanWrapperImpl(h2);

        double weightSum = 0;
        double score = 0;
        for(String test : TEST_WEIGHTS.keySet()) {
            Integer score1 = (Integer) h1Wrapper.getPropertyValue(test);
            Integer score2 = (Integer) h2Wrapper.getPropertyValue(test);
            Double normalizedScore1 = normalizeTestScore(score1, test);
            Double normalizedScore2 = normalizeTestScore(score2, test);
            if(normalizedScore1 != null && normalizedScore2 != null) {
                weightSum += TEST_WEIGHTS.get(test);
                score += (normalizedScore1 - normalizedScore2)*(normalizedScore1 - normalizedScore2);
            }
        }
        if(weightSum == 0) {
            return 1.0;
        }
        else {
            return Math.sqrt(score / weightSum);
        }
    }

    private Double normalizeTestScore(Integer score, String testType) {
        if(score == null) return null;
        Integer min, max;
        if (testType.equals(HighschoolEntity_.SAT_OVERALL)) {
            min = MIN_SAT_OVERALL;
            max = MAX_SAT_OVERALL;
        }
        else if (testType.contains("sat")) {
            min = MIN_SAT;
            max = MAX_SAT;
        }
        else if (testType.contains("act")) {
            min = MIN_ACT;
            max = MAX_ACT;
        }
        else {
            return null;
        }
        return ((double) (score - min))/((double) (max - min));
    }

}
