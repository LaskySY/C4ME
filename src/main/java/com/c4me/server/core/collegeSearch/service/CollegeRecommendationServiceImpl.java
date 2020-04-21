package com.c4me.server.core.collegeSearch.service;

import com.c4me.server.core.admin.domain.CollegeInfo;
import com.c4me.server.core.highschoolSearch.service.SimilarHighSchoolServiceImpl;
import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.core.profile.repository.ProfileRepository;
import com.c4me.server.entities.*;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.c4me.server.config.constant.Const.CollegeRecommendationConst.*;
import static com.c4me.server.config.constant.Const.Ranges.*;
import static com.c4me.server.config.constant.Const.Ranges.MAX_ACT;

/**
 * @Description: Implementation for the collegeRecommender service
 * @Author: Maciej Wlodek
 * @CreateDate: 04-12-2020
 */

@Service
public class CollegeRecommendationServiceImpl {

    @Autowired
    ProfileRepository profileRepository;
    @Autowired
    CollegeRepository collegeRepository;
    @Autowired
    SimilarHighSchoolServiceImpl similarHighSchoolService;

    final boolean debug = false;
//    static long tMillis = 0;
//    static long sMillis = 0;

    private void debug(String arg) {
        if(debug) System.out.println(arg);
    }

    /**
     * Find the home state of a student making the request (use for {{@link #computeCollegeRecommendationScore}})
     * @param username {@link String} the username of the student performing the request
     * @return {@link String} the home state (2-letter code) of the student's home state
     */
    //use the home state if sorting by cost (for in-state vs out of state)
    private String getHomeState(String username) {
        if(username == null) return "";
        String homeState = "";
        ProfileEntity profileEntity = profileRepository.findByUsername(username);
        if(profileEntity != null) {
            HighschoolEntity highschoolEntity = profileEntity.getHighschoolBySchoolId();
            if(highschoolEntity != null) {
                homeState = highschoolEntity.getState();
            }
        }
        return homeState;
    }

    /**
     * compute a list of collegeRecommendation scores
     * @param username {@link String} username of the student performing the request
     * @param collegeNames {@link List} of {@link String} college names
     * @return {@link List} of {@link CollegeInfo}'s with recommendation scores
     */
    public List<CollegeInfo> computeCollegeRecommendationScores(String username, List<String> collegeNames) {
        String homeState = getHomeState(username);

        ProfileEntity profileEntity = profileRepository.findByUsername(username);
        if(profileEntity == null) return null;

        List<CollegeInfo> recommendedColleges = new ArrayList<>();
        for(String collegeName : collegeNames) {
            CollegeEntity collegeEntity = collegeRepository.findByName(collegeName);
            if(collegeEntity == null) continue;
            else {
                Double score = computeCollegeRecommendationScore(profileEntity, collegeEntity);
                CollegeInfo recommendationCollege = new CollegeInfo(collegeEntity, homeState);
                recommendationCollege.setRecommendationScore(score);
                recommendedColleges.add(recommendationCollege);
            }
        }
        recommendedColleges.sort(Comparator.comparingDouble(CollegeInfo::getRecommendationScore).reversed());
        return recommendedColleges;
    }

    /**
     * Compute a single recommendation for a given profile and college
     * @param profileEntity {@link ProfileEntity}
     * @param collegeEntity {@link CollegeEntity}
     * @return {@link Double} recommendation score
     */
    private Double computeCollegeRecommendationScore(ProfileEntity profileEntity, CollegeEntity collegeEntity) {
        Double testSimilarityFactor = testScoreSimilarityFactor(profileEntity, collegeEntity);
        Double schoolSimilarityFactor = schoolSimilarityFactor(profileEntity, collegeEntity);
        return testSimilarityFactor * TEST_FACTOR_WEIGHT + schoolSimilarityFactor * SCHOOL_FACTOR_WEIGHT;
    }

    /**
     * Compute the test score similarity factor for a given profile and college
     * @param profileEntity {@link ProfileEntity}
     * @param collegeEntity {@link CollegeEntity}
     * @return {@link Double} test factor
     */
    private Double testScoreSimilarityFactor(ProfileEntity profileEntity, CollegeEntity collegeEntity) {
        BeanWrapperImpl profileWrapper = new BeanWrapperImpl(profileEntity);
        BeanWrapperImpl collegeWrapper = new BeanWrapperImpl(collegeEntity);

        Double sum = 0.0;
        int numTests = 0;
        for(String test : TEST_MAP.keySet()) {
            Integer studentScore = (Integer) profileWrapper.getPropertyValue(test);
            String[] collegeTestPropertyNames = TEST_MAP.get(test);
            Integer college25Percentile = (Integer) collegeWrapper.getPropertyValue(collegeTestPropertyNames[0]);
            Integer college50Percentile = (Integer) collegeWrapper.getPropertyValue(collegeTestPropertyNames[1]);
            Integer college75Percentile = (Integer) collegeWrapper.getPropertyValue(collegeTestPropertyNames[2]);
            Double normalizedStudentScore = normalizeTestScore(studentScore, test);
            Double normalizedCollege25Percentile = normalizeTestScore(college25Percentile, test);
            Double normalizedCollege50Percentile = normalizeTestScore(college50Percentile, test);
            Double normalizedCollege75Percentile = normalizeTestScore(college75Percentile, test);
            Double zScore = computeZScore(normalizedStudentScore, normalizedCollege25Percentile, normalizedCollege50Percentile, normalizedCollege75Percentile);
            if(zScore == null) continue;
            Double normalizedZScore = computeWeightedScore(zScore);
            sum += normalizedZScore;
            numTests++;
        }
        if(numTests == 0) return 0.0;
        else return sum / numTests;
    }

    /**
     * Linearly normalize a test score to the range [0,1]
     * @param score {@link Integer} score for either and SAT or ACT test
     * @param testType {@link String} the name of the test, e.g. satMath
     * @return {@link Double} the normalized test score, or null if the {@code testType} is invalid
     */
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

    /**
     * Compute the student's z-score with respect to a fitted normal distribution according to the college's 25th, 50th, and 75th percentile scores
     * @param studentScore {@link Double} the student's normalized test score
     * @param college25 {@link Double} the college's normalized 25th percentile score
     * @param college50 {@link Double} the college's normalized 50th percentile score
     * @param college75 {@link Double} the college's normalized 75th percentile score
     * @return {@link Double} the student's z-score with respect to the college, or null
     */
    private Double computeZScore(Double studentScore, Double college25, Double college50, Double college75) {
        if(studentScore == null || college25 == null || college75 == null) {
            return null;
        }
        Double mean = (college50 == null)? (college75-college25)/2 : college50;
        Double sigma = (college75-college25)/1.35;
        return (studentScore - mean)/(sigma);
    }

    /**
     * Pass the zScore through an activation function
     * @param zScore {@link Double} the student's zScore
     * @return {@link Double} the weighted score in the range [0,1]
     */
    private Double computeWeightedScore(Double zScore) {
        return 1/(1+zScore*zScore);
    }


    /**
     * Compute the school similarity factor of a given student and college (i.e. how popular is the college at the high school attended by the student)
     * @param profileEntity {@link ProfileEntity}
     * @param collegeEntity {@link CollegeEntity}
     * @return {@link Double} the school similarity factor
     */
    private Double schoolSimilarityFactor(ProfileEntity profileEntity, CollegeEntity collegeEntity) {
        HighschoolEntity highschoolEntity = profileEntity.getHighschoolBySchoolId();
        List<HighschoolEntity> similarHighSchools;
        try {
            similarHighSchools = similarHighSchoolService.getSimilarHighSchools(highschoolEntity.getName());
            if(similarHighSchools == null || similarHighSchools.size() == 0) return 0.0;
        } catch (Exception e) {
            similarHighSchools = new ArrayList<>();
            similarHighSchools.add(highschoolEntity);
        }
        int counter = 0;
        for(HighschoolEntity he : similarHighSchools) {
            Collection<CollegeHighschoolAssociationEntity> highschoolAssociationEntityCollection = highschoolEntity.getCollegeHighschoolAssociationsBySchoolId();
            Collection<CollegeEntity> popularColleges = highschoolAssociationEntityCollection.stream().map(CollegeHighschoolAssociationEntity::getCollegeByCollegeId).collect(Collectors.toList());
            if (popularColleges.contains(collegeEntity)) {
                counter++;
            }
        }
        return ((double) counter) / ((double) similarHighSchools.size());
    }
}
