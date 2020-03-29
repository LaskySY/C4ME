package com.c4me.server.core.admin.service;


import static com.c4me.server.config.constant.Const.StudentProfileHeaders.*;


import com.c4me.server.config.exception.InvalidCollegeScorecardException;
import com.c4me.server.config.exception.InvalidStudentProfileException;
import com.c4me.server.config.exception.NoCollegeScorecardException;
import com.c4me.server.config.exception.NoStudentProfileCSVException;
import com.c4me.server.core.profile.repository.ProfileRepository;
import com.c4me.server.entities.CollegeEntity;
import com.c4me.server.entities.ProfileEntity;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author: Yousef Khan
 * @CreateDate: 03-25-2020
 */

@Service
public class ImportStudentProfileServiceImpl {

  @Autowired
  ProfileRepository profileRepository;

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



  public void importStudentProfileCsv(String filename) throws IOException, NoStudentProfileCSVException, InvalidStudentProfileException {
    System.out.println("Importing");

    if(filename.equals("")) throw new NoStudentProfileCSVException("student profile file not found");

    Reader in = new FileReader(filename);
    Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);

    for (CSVRecord record : records) {

      ProfileEntity profileEntity = recordToEntity(record);
      if(profileEntity == null) throw new InvalidStudentProfileException("invalid student profile file");
      else{
        profileRepository.save(profileEntity);
      }

      in.close();


    }








  }

}
