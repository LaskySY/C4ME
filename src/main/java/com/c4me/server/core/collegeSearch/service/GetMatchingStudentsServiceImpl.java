package com.c4me.server.core.collegeSearch.service;


import static com.c4me.server.config.constant.Const.Status.STATUS_MAP;

import com.c4me.server.config.constant.Const;
import com.c4me.server.core.admin.domain.ProfileInfo;
import com.c4me.server.core.collegeSearch.domain.ApplicationProfileInfo;
import com.c4me.server.core.collegeSearch.domain.MatchingProfileFilter;
import com.c4me.server.core.collegeSearch.specifications.MatchingApplicationFilterSpecification;
import com.c4me.server.core.collegeSearch.specifications.MatchingProfileFilterSpecification;
import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.core.profile.repository.ProfileRepository;
import com.c4me.server.core.profile.repository.StudentApplicationRepository;
import com.c4me.server.entities.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import com.c4me.server.utils.TestingDataUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

/**
 * @Description:
 * @Author: Yousef Khan
 * @CreateDate: 04-20-2020
 */

@Service
public class GetMatchingStudentsServiceImpl {

  @Autowired
  ProfileRepository profileRepository;
  @Autowired
  StudentApplicationRepository studentApplicationRepository;
  @Autowired
  CollegeRepository collegeRepository;
  @PersistenceContext
  EntityManager entityManager;



  public List<ProfileInfo> getMatchingStudents(MatchingProfileFilter filter){

    if(filter.getApplicationStatus() != null){
      List<Integer> intList = new ArrayList<>();
      for(String appStatus: filter.getApplicationStatus()){
        Integer statusInt;
        if(!STATUS_MAP.containsValue(appStatus)) statusInt = -1;
        else statusInt = STATUS_MAP.entrySet().stream().filter(e -> e.getValue().equals(appStatus)).collect(
            Collectors.toList()).get(0).getKey();
        intList.add(statusInt);
      }
      filter.setApplicationInts(intList);
    }

    MatchingProfileFilterSpecification specification = new MatchingProfileFilterSpecification(filter);
    System.out.println(specification);

    MatchingApplicationFilterSpecification applicationFilterSpecification = new MatchingApplicationFilterSpecification(filter);

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ApplicationProfileInfo> criteriaQuery = criteriaBuilder.createQuery(ApplicationProfileInfo.class);

    Root<ProfileEntity> profileEntityRoot = criteriaQuery.from(ProfileEntity.class);
    Root<StudentApplicationEntity> studentApplicationEntityRoot = criteriaQuery.from(StudentApplicationEntity.class);

    Predicate profilePredicate = specification.toPredicate(profileEntityRoot, criteriaQuery, criteriaBuilder);
    Predicate appPredicate = applicationFilterSpecification.toPredicate(studentApplicationEntityRoot, criteriaQuery, criteriaBuilder);
    Predicate joinPredicate = criteriaBuilder.equal(profileEntityRoot.get(ProfileEntity_.username), studentApplicationEntityRoot.get(StudentApplicationEntity_.userByUsername).get(UserEntity_.username));

//    Predicate predicate = criteriaBuilder.equal(profileEntityRoot.get(ProfileEntity_.username), studentApplicationEntityRoot.get(StudentApplicationEntity_.userByUsername).get(UserEntity_.username));
//    Predicate predicate1 = criteriaBuilder.equal(studentApplicationEntityRoot.get(StudentApplicationEntity_.collegeByCollegeId).get(CollegeEntity_.NAME), filter.getName());
    //Predicate predicate2 = filter.getHighSchools() == null? criteriaBuilder.conjunction() : profileEntityRoot.get(ProfileEntity_.highschoolBySchoolId).get(HighschoolEntity_.name).in(filter.getHighSchools());

//    TypedQuery<MyJoin> myJoinTypedQuery = entityManager.createQuery(criteriaQuery.multiselect(profileEntityRoot, studentApplicationEntityRoot).where(predicate, predicate1, predicate2));//.where(specification.toPredicate(profileEntityRoot, criteriaBuilder.createQuery(ProfileEntity.class), criteriaBuilder)));
//    TypedQuery<MyJoin> myJoinTypedQuery = entityManager.createQuery(criteriaQuery.multiselect(profileEntityRoot, studentApplicationEntityRoot).where(specification, applicationFilterSpecification));//.where(specification.toPredicate(profileEntityRoot, criteriaBuilder.createQuery(ProfileEntity.class), criteriaBuilder)));
    TypedQuery<ApplicationProfileInfo> myJoinTypedQuery = entityManager.createQuery(criteriaQuery.multiselect(profileEntityRoot, studentApplicationEntityRoot).where(profilePredicate, appPredicate, joinPredicate));

    List<ApplicationProfileInfo> resultSet = myJoinTypedQuery.getResultList();

    System.out.println(resultSet);
    System.out.println(resultSet.size());



    List<ProfileInfo> matchingProfileInfo = new ArrayList<>();

    if(resultSet.size() == 0) return matchingProfileInfo;

    Map<String, Map<Integer, Integer>> percentiles = null;
    try {
      percentiles = readPercentilesCsv();
    } catch (IOException e) {
      e.printStackTrace();
    }

    for(ApplicationProfileInfo applicationProfileInfo : resultSet) {
      if(applicationProfileInfo == null || applicationProfileInfo.getProfileEntity() == null || applicationProfileInfo.getStudentApplicationEntity() == null) continue;
      else {
        ProfileInfo profileInfo = new ProfileInfo(applicationProfileInfo.getProfileEntity());
        Integer statusInt = applicationProfileInfo.getStudentApplicationEntity().getStatus();
        String statusStr = null;
        if(statusInt != null && (STATUS_MAP.containsKey(statusInt))) {
          statusStr = STATUS_MAP.get(statusInt);
        }
        profileInfo.setApplicationStatus(statusStr);
        profileInfo.computeWeightedAveragePercentileScore(percentiles);
        matchingProfileInfo.add(profileInfo);
      }
    }

    return matchingProfileInfo;

  }

  private Map<String, Map<Integer, Integer>> readPercentilesCsv() throws IOException {
    File percentilesFile = TestingDataUtils.findFile(Const.Filenames.PERCENTILES_FILE, "csv");
    if(percentilesFile == null) {
      return null;
    }
    FileReader reader = new FileReader(percentilesFile);

    CSVParser parser = CSVFormat.EXCEL.withHeader().withTrim().parse(reader);
    Map<String, Map<Integer, Integer>> percentiles = new HashMap<>();
    for(String header : parser.getHeaderNames()) {
      if(!(header.equals("Score"))) {
        percentiles.put(header, new HashMap<>());
      }
    }

    for (CSVRecord record : parser) {
      Integer score = Integer.parseInt(record.get("Score"));
      for (String header : parser.getHeaderNames()) {
        if (!header.equals("Score")) {
          String percentileStr = record.get(header);
          if (percentileStr != null && percentileStr.length() > 0) {
            Integer percentile = Integer.parseInt(record.get(header));
            percentiles.get(header).put(score, percentile);
          }
        }
      }
    }
    return percentiles;
  }
}
