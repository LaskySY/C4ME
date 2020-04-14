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
 * @Description:
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

    //use the home state if sorting by cost (for in-state vs out of state)
    private String getHomeState(String username) {
        if(username == null) return null;
        String homeState = null;
        ProfileEntity profileEntity = profileRepository.findByUsername(username);
        if(profileEntity != null) {
            HighschoolEntity highschoolEntity = profileEntity.getHighschoolBySchoolId();
            if(highschoolEntity != null) {
                homeState = highschoolEntity.getState();
            }
        }
        return homeState;
    }

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
//        HashMap<String, Double> recommendationScores = new HashMap<>();
//
//        for(String collegeName : collegeNames) {
//            CollegeEntity collegeEntity = collegeRepository.findByName(collegeName);
//            if(collegeEntity == null) {
//                recommendationScores.put(collegeName, 0.0);
//            }
//            else {
//                recommendationScores.put(collegeName, computeCollegeRecommendationScore(profileEntity, collegeEntity));
//            }
//        }
//        List<Map.Entry<String, Double>> sortedScores = new ArrayList<>(recommendationScores.entrySet());
//        Comparator<Map.Entry<String, Double>> compareValues = (o1, o2) -> {
//            if(o1.getValue().equals(o2.getValue())) return 0;
//            else if (o1.getValue() < o2.getValue()) return -1;
//            else return 1;
//        };
//        sortedScores.sort(compareValues);
//        System.out.println(sortedScores);
//        Map<String, Double> returnMap = new HashMap<>();
//        for(Map.Entry<String, Double> entry : sortedScores) {
//            returnMap.put(entry.getKey(), entry.getValue());
//        }
//        System.out.println(returnMap);
//        return returnMap;
//        return sortedScores.stream().collect(Collectors.toMap(e -> (String) e.getKey(), f -> (Double) f.getValue()));
    }

    private Double computeCollegeRecommendationScore(ProfileEntity profileEntity, CollegeEntity collegeEntity) {
//        long x,y;
//        x = System.currentTimeMillis();
        Double testSimilarityFactor = testScoreSimilarityFactor(profileEntity, collegeEntity);
//        y = System.currentTimeMillis();
//        tMillis += (y-x);
//        x = System.currentTimeMillis();
        Double schoolSimilarityFactor = schoolSimilarityFactor(profileEntity, collegeEntity);
//        y = System.currentTimeMillis();
//        sMillis += (y-x);
//        debug("tmillis = " + tMillis);
//        debug("smillis = " + sMillis);
        return testSimilarityFactor * TEST_FACTOR_WEIGHT + schoolSimilarityFactor * SCHOOL_FACTOR_WEIGHT;
    }

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

    private Double computeZScore(Double studentScore, Double college25, Double college50, Double college75) {
        if(studentScore == null || college25 == null || college75 == null) {
            return null;
        }
        Double mean = (college50 == null)? (college75-college25)/2 : college50;
        Double sigma = (college75-college25)/1.35;
        return (studentScore - mean)/(sigma);
    }

    private Double computeWeightedScore(Double zScore) {
        return 1/(1+zScore*zScore);
    }


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
