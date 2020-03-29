package com.c4me.server.core.admin.service;


import static com.c4me.server.config.constant.Const.Filenames.STUDENT_APPLICATIONS_FILE;
import static com.c4me.server.config.constant.Const.Filenames.STUDENT_PROFILES_FILE;
import static com.c4me.server.config.constant.Const.StudentProfileHeaders.*;
import static com.c4me.server.config.constant.Const.ApplicationFileHeaders.*;


import com.c4me.server.config.exception.HighSchoolDoesNotExistException;
import com.c4me.server.config.exception.InvalidCollegeScorecardException;
import com.c4me.server.config.exception.InvalidStudentApplicationException;
import com.c4me.server.config.exception.InvalidStudentProfileException;
import com.c4me.server.config.exception.NoCollegeScorecardException;
import com.c4me.server.config.exception.NoStudentApplicationCSVException;
import com.c4me.server.config.exception.NoStudentProfileCSVException;
import com.c4me.server.core.credential.repository.HighschoolRepository;
import com.c4me.server.core.credential.repository.UserRepository;
import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.core.profile.repository.ProfileRepository;
import com.c4me.server.entities.CollegeEntity;
import com.c4me.server.entities.HighschoolEntity;
import com.c4me.server.entities.ProfileEntity;
import com.c4me.server.entities.StudentApplicationEntity;
import com.c4me.server.entities.UserEntity;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.c4me.server.core.profile.service.HighSchoolScraperServiceImpl;
import static com.c4me.server.config.constant.Const.Filenames.STUDENT_APPLICATIONS_FILE;

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
  HighSchoolScraperServiceImpl highSchoolScraperService;


  public Integer parseInt(String s) {
    try { return Integer.parseInt(s); }
    catch (NumberFormatException e) { return null; }
  }
  public Double parseDouble(String s) {
    try { return Double.parseDouble(s); }
    catch (NumberFormatException e) { return null; }
  }


  public ProfileEntity recordToEntity(CSVRecord record) { //TODO: should only check header for the first record
    Map<String, String> recordMap = record.toMap();
//        System.out.println(recordMap.toString());
//        System.out.println(recordMap.keySet().toString());
    Set<String> keysWeCareAbout = new HashSet<String>(Arrays.asList(HEADERS));
    //trim whitespaces

    //this gives concurrent modification error
//    for(String s: recordMap.keySet()){
//      System.out.println(s);
//      String keyvalString = recordMap.remove(s);
//      s = s.trim();
//      recordMap.put(s, keyvalString);
//    }

    //this only changes the values not the headers
//    for(Iterator<Entry<String, String>> it = recordMap.entrySet().iterator(); it.hasNext();)
//    {
//
//      Map.Entry<String, String> entry = it.next();
//      String s = entry.getValue().trim();
//      System.out.println("key is"+s);
//        entry.setValue(s);
//    }

    if(!recordMap.keySet().containsAll(keysWeCareAbout)) {
      System.out.println("missing headers -- throw an error");
      for(String key : keysWeCareAbout) {
        if(!recordMap.containsKey(key)) {
          System.out.println("missing header key " + key);
        }
      }
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
          .satPhysics(parseInt(record.get(SAT_PHYS)))
          .numApCourses(parseInt(record.get(NUM_AP_PASSED)))
//          .createTime(Timestamp.from(Instant.now()))
          .updateTime(Timestamp.from(Instant.now()))
          .build();
    }

  }


  public StudentApplicationEntity recordApplicationEntity(CSVRecord record){
    Map<String, String> recordMap = record.toMap();
    Set<String> keysWeCareAbout = new HashSet<String>(Arrays.asList(APP_HEADERS));

    if(!recordMap.keySet().containsAll(keysWeCareAbout)) {
      System.out.println("missing headers -- throw an error");
      for(String key : keysWeCareAbout) {
        if(!recordMap.containsKey(key)) {
          System.out.println("missing header key " + key);
        }
      }
      return null;
    }
    else{
      UserEntity thisUser = userRepository.findByUsername(APP_USER_ID);
      CollegeEntity thisCollege = collegeRepository.findByName(APP_COLLEGE);
      Integer decisionStatus = 0;
      if(recordMap.get(APP_COLLEGE).equalsIgnoreCase("pending")){
        decisionStatus = 1;
      }
      else if(recordMap.get(APP_COLLEGE).equalsIgnoreCase("accepted")){
        decisionStatus = 2;
      }
      else if(recordMap.get(APP_COLLEGE).equalsIgnoreCase("denied")){
        decisionStatus = 3;
      }
      else if(recordMap.get(APP_COLLEGE).equalsIgnoreCase("waitlisted") ||
          recordMap.get(APP_COLLEGE).equalsIgnoreCase("wait-listed")){
        decisionStatus = 4;
      }

      return StudentApplicationEntity.builder()
          .userByUsername(thisUser)
          .collegeByCollegeId(thisCollege)
          .status(decisionStatus)
          .build();
    }



  }



  public void importStudentProfileCsv(String filename)
      throws IOException, NoStudentProfileCSVException, InvalidStudentProfileException, HighSchoolDoesNotExistException, InvalidStudentApplicationException, NoStudentApplicationCSVException {
    System.out.println("Importing");

    if(filename.equals("")) throw new NoStudentProfileCSVException("student profile file not found");

    Reader in = new FileReader(filename);
    Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);

    List<UserEntity> currentUsers = userRepository.findAll();
    List<HighschoolEntity> currentHighSchools = highschoolRepository.findAll();

    for (CSVRecord record : records) {

      Boolean userExists = false;
      Boolean highschoolExists = false;
      UserEntity newUser = null;
      HighschoolEntity newHS = null;
      UserEntity existingUser = null;

      ProfileEntity profileEntity = recordToEntity(record);
      if(profileEntity == null) throw new InvalidStudentProfileException("invalid student profile file");
      else{

        String username = record.get(USER_ID);
        String highschoolname = record.get(HS_NAME);
        username = username.trim();
        highschoolname = highschoolname.trim();
        //check if user with that username exists
        for(UserEntity user : currentUsers){
          if(user.getUsername().equals(username)){
            userExists = true;
            existingUser = user;
          }
        }

        //if it doesn't exist create it
        if(!userExists){
          newUser = UserEntity.builder()
              .username(username)
              .name(username)
              .password(record.get(PASSWORD))
              .role(0)
              .profileByUsername(profileEntity)
              .createTime(Timestamp.from(Instant.now()))
              .updateTime(Timestamp.from(Instant.now()))
              .build();
        }

        //check if highschool exists
        for(HighschoolEntity hs : currentHighSchools){
          if(hs.getName().equals(highschoolname)){
            highschoolExists = true;
          }
        }

        //if it doesn't exist create it and scrape it's info from niche site
        if(!highschoolExists){
          newHS = highSchoolScraperService.scrapeHighSchool(highschoolname);
        }

        //check applications csv and if it contains any applications from that user then we add them to his profile

        //get the applications file
        File topDir = new File(System.getProperty("user.dir"));
        Iterator<File> files = FileUtils.iterateFiles(topDir, new String[] {"csv"}, true);

        String studentApplicationsFilename = "";
        boolean b1 = false, b2 = false;
        while(files.hasNext()) {
          File f = files.next();
          String extension = FilenameUtils.getExtension(f.getAbsolutePath());
          if (f.getName().equals(STUDENT_APPLICATIONS_FILE) && extension.equals("csv")) {
            studentApplicationsFilename = f.getAbsolutePath();
            b1 = true;
            if (b2)
              break;
          }
        }

        if(studentApplicationsFilename.equals("")) throw new NoStudentApplicationCSVException("Student applications file you requested was not found");

        Reader applicationReader = new FileReader(studentApplicationsFilename);
        Iterable<CSVRecord> appRecords = CSVFormat.EXCEL.withHeader().parse(in);
        for(CSVRecord appRecord : appRecords){

          StudentApplicationEntity applicationEntity = recordApplicationEntity(appRecord);
          if(applicationEntity == null) throw new InvalidStudentApplicationException("invalid student applications file");
          else{
            if(existingUser.equals(applicationEntity.getUserByUsername())){
              //then add the application to theirs
              existingUser.getStudentApplicationsByUsername().add(applicationEntity);
            }
          }

        }

        applicationReader.close();

        //save everything

        //save new user if we made one
        if(!userExists){
          userRepository.save(newUser);
        }
        else{ // else save the existing one with the updated changes to their applications field
          userRepository.save(existingUser);
        }

        if(!highschoolExists){
          highschoolRepository.save(newHS);
        }


        profileRepository.save(profileEntity);
      }

      in.close();


    }








  }

}
