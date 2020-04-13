package com.c4me.server.core.admin.service;



import static com.c4me.server.config.constant.Const.StudentProfileHeaders.*;
import static com.c4me.server.config.constant.Const.ApplicationFileHeaders.*;
import static com.c4me.server.config.constant.Const.Status.*;


import com.c4me.server.config.exception.*;
import com.c4me.server.core.credential.domain.RegisterUser;
import com.c4me.server.core.credential.repository.HighschoolRepository;
import com.c4me.server.core.credential.repository.UserRepository;
import com.c4me.server.core.credential.service.userDetailsServiceImpl;
import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.core.profile.repository.MajorRepository;
import com.c4me.server.core.profile.repository.ProfileRepository;
import com.c4me.server.core.profile.repository.StudentApplicationRepository;
import com.c4me.server.entities.*;

import java.io.*;
import java.util.*;

import com.c4me.server.core.profile.service.MajorAliasTable;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.c4me.server.core.profile.service.HighSchoolScraperServiceImpl;

/**
 * @Description:
 * @Author: Yousef Khan
 * @CreateDate: 03-25-2020
 */

@Service
public class ImportStudentProfileServiceImpl {

  @Autowired
  ProfileRepository profileRepository;
  @Autowired
  UserRepository userRepository;
  @Autowired
  HighschoolRepository highschoolRepository;
  @Autowired
  CollegeRepository collegeRepository;
  @Autowired
  MajorRepository majorRepository;
  @Autowired
  HighSchoolScraperServiceImpl highSchoolScraperService;
  @Autowired
  userDetailsServiceImpl userDetailsService;
  @Autowired
  StudentApplicationRepository studentApplicationRepository;

  boolean debug = true;
  private void debug(String arg) { if(debug) System.out.println(arg); }

  public Integer parseInt(String s) {
    try { return Integer.parseInt(s); }
    catch (NumberFormatException e) { return null; }
  }
  public Double parseDouble(String s) {
    try { return Double.parseDouble(s); }
    catch (NumberFormatException e) { return null; }
  }

  public ProfileEntity recordToEntity(CSVRecord record) {
    Map<String, String> recordMap = record.toMap();
    Set<String> keysWeCareAbout = new HashSet<String>(Arrays.asList(HEADERS));
    if(!recordMap.keySet().containsAll(keysWeCareAbout)) {
      return null;
    }
    else {
      return ProfileEntity.builder()
          .username(record.get(USER_ID))
          .gpa(parseDouble(record.get(GPA)))
          .major1(record.get(MAJOR1))
          .major2(record.get(MAJOR2))
          .satMath(parseInt(record.get(SAT_MATH)))
          .satEbrw(parseInt(record.get(SAT_EBRW)))
          .actEnglish(parseInt(record.get(ACT_ENG)))
          .actMath(parseInt(record.get(ACT_MATH)))
          .actReading(parseInt(record.get(ACT_READING)))
          .actScience(parseInt(record.get(ACT_SCI)))
          .actComposite(parseInt(record.get(ACT_COMP)))
          .satLiterature(parseInt(record.get(SAT_LIT)))
          .satUsHist(parseInt(record.get(SAT_US_HIST)))
          .satWorldHist(parseInt(record.get(SAT_WORLD_HIST)))
          .satMathI(parseInt(record.get(SAT_MATH1)))
          .satMathIi(parseInt(record.get(SAT_MATH2)))
          .satEcoBio(parseInt(record.get(SAT_ECO_BIO)))
          .satMolBio(parseInt(record.get(SAT_MOL_BIO)))
          .satChemistry(parseInt(record.get(SAT_CHEM)))
          .satPhysics(parseInt(record.get(SAT_PHYS)))
          .numApCourses(parseInt(record.get(NUM_AP_PASSED)))
          .schoolYear(parseInt(record.get(COLLEGE_CLASS)))
          .build();
    }
  }


  public StudentApplicationEntity recordApplicationEntity(CSVRecord record){
    Map<String, String> recordMap = record.toMap();
    Set<String> keysWeCareAbout = new HashSet<String>(Arrays.asList(APP_HEADERS));

    if(!recordMap.keySet().containsAll(keysWeCareAbout)) {
      return null;
    }
    else{
      UserEntity thisUser = userRepository.findByUsername(record.get(APP_USER_ID));
      if(thisUser == null) return null;
      CollegeEntity thisCollege = collegeRepository.findByNameLike("%"+record.get(APP_COLLEGE)+"%"); //TODO: use fuzzy search on colleges.txt to get the correct name
      if(thisCollege == null) return null;
      Integer decisionStatus = 0;
      String status = record.get(APP_STATUS);
      if(status.equalsIgnoreCase("pending")){
        decisionStatus = PENDING;
      }
      else if(status.equalsIgnoreCase("accepted")){
        decisionStatus = ACCEPTED;
      }
      else if(status.equalsIgnoreCase("denied")){
        decisionStatus = DENIED;
      }
      else if(status.equalsIgnoreCase("waitlisted") ||
          recordMap.get(APP_STATUS).equalsIgnoreCase("wait-listed")){
        decisionStatus = WAITLISTED;
      }
      StudentApplicationEntityPK studentApplicationEntityPK = StudentApplicationEntityPK.builder()
              .collegeId(thisCollege.getId())
              .username(thisUser.getUsername())
              .build();

      return StudentApplicationEntity.builder()
          .userByUsername(thisUser)
          .collegeByCollegeId(thisCollege)
          .status(decisionStatus)
          .studentApplicationEntityPK(studentApplicationEntityPK)
          .build();
    }



  }

  public void importStudentProfileCsv(File file)
          throws IOException, NoStudentProfileCSVException, InvalidStudentProfileException, HighSchoolDoesNotExistException, NoStudentApplicationCSVException, DuplicateUsernameException {
    debug("Importing students");

    if(file == null) throw new NoStudentProfileCSVException("student profile file not found");

    Reader in = new FileReader(file);
    Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().withTrim().parse(in);

    for (CSVRecord record : records) {
      debug(record.toMap().toString());
      ProfileEntity profileEntity = recordToEntity(record);
      if(profileEntity == null) throw new InvalidStudentProfileException("invalid student profile file");
      else{
        String major1 = profileEntity.getMajor1();
        String major2 = profileEntity.getMajor2();
        profileEntity.setMajorByMajor1(getMajorEntityIfExists(major1));
        profileEntity.setMajorByMajor2(getMajorEntityIfExists(major2));

        String username = profileEntity.getUsername();
        String password = record.get(PASSWORD);
        String name = username;
        profileEntity.setUserByUsername(getUserEntityIfExists(username, password, name));

        String highschoolName = record.get(HS_NAME);
        String highschoolCity = record.get(HS_CITY);
        String highschoolState = record.get(HS_STATE);

        //if it doesn't exist, scrape its info from niche site
        String hsquery = highschoolName + " " + highschoolCity + " " + highschoolState;
        HighschoolEntity highschoolEntity = highschoolRepository.findByName(hsquery);
        if(highschoolEntity == null) highschoolEntity = highSchoolScraperService.scrapeHighSchool(hsquery, false);
        profileEntity.setHighschoolBySchoolId(highschoolEntity);

        profileRepository.save(profileEntity);
      }
    }
  }

  public void importStudentApplicationsCsv(File file) throws InvalidStudentApplicationException, IOException {
    debug("Importing applications");

    if(file == null) throw new InvalidStudentApplicationException("applications file not found");

    Reader in = new FileReader(file);
    Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().withTrim().parse(in);

    for(CSVRecord record : records) {
      debug(record.get(USER_ID));
      StudentApplicationEntity studentApplicationEntity = recordApplicationEntity(record);
      if(studentApplicationEntity != null) {
        //TODO: compute questionable decision first
        studentApplicationRepository.save(studentApplicationEntity);
      }
    }
  }


  private MajorEntity getMajorEntityIfExists(String major) {
    MajorAliasTable majorAliasTable = new MajorAliasTable();
    return majorAliasTable.parseMajorName(major);
//    MajorEntity majorEntity;
//    if(major == null || major.length() == 0) {
//      majorEntity = null;
//    }
//    else {
//      Optional<MajorEntity> majorEntityOpt = majorRepository.findById(major);
//      majorEntity = majorEntityOpt.orElseGet(() -> {
//        MajorEntity me = MajorEntity.builder().name(major).build();
//        majorRepository.save(me);
//        return me;
//      });
//    }
//    return majorEntity;
  }

  private UserEntity getUserEntityIfExists(String username, String password, String name) {
    UserEntity userEntity;
    if(username == null) {
      userEntity = null;
    }
    else {
      Optional<UserEntity> userEntityOpt = userRepository.findById(username);
      userEntity = userEntityOpt.orElseGet(() -> {
        RegisterUser registerUser = RegisterUser.builder().username(username).password(password).name(name).build();
        try {
          return userDetailsService.register(registerUser);
        } catch (DuplicateUsernameException ignored) {
        }
        return null;
      });
    }
    return userEntity;
  }

}
