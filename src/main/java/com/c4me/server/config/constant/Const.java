package com.c4me.server.config.constant;

import com.c4me.server.entities.*;
import com.c4me.server.utils.TestingDataUtils;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-26-2020
 */
public final class Const {

  private Const() {
  }

  public final static class Header {

    public static final String TOKEN = "token";
    public static final String CHARACTER_ENCODING = "UTF-8";
    public static final String CONTENT_TYPE = "application/json; charset=utf-8";
  }

  public final static class Error {

    public static final String DUPLICATE_USERNAME = "duplicateUsername";
    public static final String LOG = "512";
    public static final String AUTHENTICATION = "badCredential";
    public static final String MISS_TOKEN = "514";
    public static final String TOKEN_EXPIRED = "515";
    public static final String ACCESS_DENIED = "516";
    public static final String USER_NOT_FOUND = "userNotExist";
    public static final String COLLEGE_NOT_FOUND = "collegeInvalid";
    public static final String COLLEGE_SCORECARD_NOT_FOUND = "fail";
    public static final String COLLEGE_TXT_NOT_FOUND = "collegeTxtNotFound";
    public static final String INVALID_COLLEGE_SCORECARD = "invalidScorecardFile";
    public static final String HIGHSCHOOL_NOT_FOUND = "highSchoolNotFound";
  }

  public final static class Role {
    public static final String ADMIN = "Admin";
    public static final String STUDENT = "Student";
  }

  public final static class Status {
    public static final Integer PENDING = 0;
    public static final Integer ACCEPTED = 1;
    public static final Integer DENIED = 2;
    public static final Integer WAITLISTED = 3;
    public static final Integer DEFERRED = 4;
    public static final Integer WITHDRAWN = 5;

    public static final String PENDING_STR = "Pending";
    public static final String ACCEPTED_STR = "Accepted";
    public static final String DENIED_STR = "Denied";
    public static final String WAITLISTED_STR = "Waitlisted";
    public static final String DEFERRED_STR = "Deferred";
    public static final String WITHDRAWN_STR = "Withdrawn";

    public static final Map<Integer, String> STATUS_MAP = new HashMap<Integer, String>() {
      {
        put(PENDING, PENDING_STR);
        put(ACCEPTED, ACCEPTED_STR);
        put(DENIED, DENIED_STR);
        put(WAITLISTED, WAITLISTED_STR);
        put(DEFERRED, DEFERRED_STR);
        put(WITHDRAWN, WITHDRAWN_STR);
      }
    };

  }

  public final static class Questionable {
    public static final Byte OK = 0;
    public static final Byte QUESTIONABLE = 1;
    public static final Byte DISHONEST = 2;
  }

  public final static class Ranges {
    public static final Double MIN_ZSCORE_FOR_QUESTIONABLE = 2.5;
    public static final Integer MIN_STUDENTS_FOR_STDDEV = 10;
    public static final Integer MIN_SAT = 200;
    public static final Integer MAX_SAT = 800;
    public static final Integer MIN_ACT = 1;
    public static final Integer MAX_ACT = 36;

    public static final Integer MIN_SAT_OVERALL = 400;
    public static final Integer MAX_SAT_OVERALL = 1600;

    public static final Integer MAX_QUERY_SIZE = 128;
  }

  public final static class SimilarHS {
    public static final Integer MAX_SCHOOLS = 10;
    public static final Integer RANGE = 10;

    public static final Map<String, Double> TEST_WEIGHTS = new HashMap<String, Double>(){
      {
        put(HighschoolEntity_.SAT_MATH, 1.0);
        put(HighschoolEntity_.SAT_EBRW, 1.0);
        put(HighschoolEntity_.ACT_ENGLISH, 0.5);   //act tests have half weight since there are twice as many
        put(HighschoolEntity_.ACT_MATH, 0.5);
        put(HighschoolEntity_.ACT_READING, 0.5);
        put(HighschoolEntity_.ACT_SCIENCE, 0.5);
      }
    };

    public static final Double TEST_FACTOR_WEIGHT = 0.85;
    public static final Double STUDENT_FACTOR_WEIGHT = 0.1;
    public static final Double ACADEMIC_FACTOR_WEIGHT = 0.05;

    public static final Double MISSING_TEST_PENALTY = 1.0;
    public static final Double MISSING_STUDENT_PENALTY = 1.0;
    public static final Double MISSING_ACADEMIC_PENALTY = 1.0;
  }

  public final static class AcademicQuality {
    public static final Map<String, Integer> LETTER_TO_NUMBER_GRADE = new HashMap<String, Integer>() {
      {
        put("A+", 12);
        put("A", 11);
        put("A-", 11);
        put("B+", 10);
        put("B", 9);
        put("B-", 8);
        put("C+", 7);
        put("C", 6);
        put("C-", 5);
        put("D+", 4);
        put("D", 3);
        put("D-", 2);
        put("F", 1);
      }
    };
  }

  public final static class CollegeRecommendationConst {
    public static final Map<String, String[]> TEST_MAP = new HashMap<String, String[]>(){
      {
        put(ProfileEntity_.SAT_MATH, new String[] {CollegeEntity_.SAT_MATH25, CollegeEntity_.SAT_MATH50, CollegeEntity_.SAT_MATH75});
        put(ProfileEntity_.SAT_EBRW, new String[] {CollegeEntity_.SAT_EBRW25, CollegeEntity_.SAT_EBRW50, CollegeEntity_.SAT_EBRW75});
        put(ProfileEntity_.ACT_ENGLISH, new String[] {CollegeEntity_.ACT_ENGLISH25, CollegeEntity_.ACT_ENGLISH50, CollegeEntity_.ACT_ENGLISH75});
        put(ProfileEntity_.ACT_MATH, new String[] {CollegeEntity_.ACT_MATH25, CollegeEntity_.ACT_MATH50, CollegeEntity_.ACT_MATH75});
      }
    };

    public static final Double TEST_FACTOR_WEIGHT = 0.8;
    public static final Double SCHOOL_FACTOR_WEIGHT = 0.2;
  }

  public final static class ProfileWeightedPercentileConst {

    public static final Double SUBJECT_TEST_WEIGHT = 0.05;

  }

  public final static class Types {
    public static final Integer PUBLIC = 1;
    public static final Integer PRIVATE = 2;
    public static final Integer PROPRIETARY = 3;
    public static final String PUBLIC_STR = "Public";
    public static final String PRIVATE_STR = "Private";
    public static final String PROPRIETARY_STR = "Proprietary";
    public static final Map<Integer, String> TYPES_MAP = new HashMap<Integer, String>() {
      {
        put(PUBLIC, PUBLIC_STR);
        put(PRIVATE, PRIVATE_STR);
        put(PROPRIETARY, PROPRIETARY_STR);
      }
    };
  }

  public final static class FilterOptions {
    public static final String[] SUPPORTED_RANGE_FILTERS = {
            CollegeEntity_.SAT_MATH50,
            CollegeEntity_.SAT_EBRW50,
            CollegeEntity_.ACT_COMPOSITE,
            CollegeEntity_.ADMISSION_RATE,
            CollegeEntity_.RANKING,
            CollegeEntity_.NUM_STUDENTS_ENROLLED,
    };
    //Special filters must be handled separately since they aren't actual columns
    public static final String[] SPECIAL_RANGE_FILTERS = {
            "costOfAttendance"
    };

    public static final String[] SUPPORTED_MATCH_FILTERS = {
            CollegeEntity_.NAME
            //major1, major2
    };
    public static final Map<String, String> SUPPORTED_IN_FILTERS = new HashMap<String, String>() {
      {
        put(CollegeEntity_.STATE, "states");
      }
    };
    public static final String[] SUPPORTED_SORT_FILTERS = {
            CollegeEntity_.NAME,
            CollegeEntity_.ADMISSION_RATE,
            CollegeEntity_.RANKING
    };
    //Special filters must be handled separately since they aren't actual columns
    public static final String[] SPECIAL_SORT_FILTERS = {
            "costOfAttendance"
    };

  }

  public static class Filenames {
    // these are the default config values ... they may be overwritten if there is a config file found
    public static String COLLEGE_SCORECARD_FILE = "Most-Recent-Cohorts-All-Data-Elements.csv";
    public static String COLLEGES = "colleges.txt";
    public static String COLLEGEDATATXT = "college_data_colleges.txt";
    public static String STUDENT_PROFILES_FILE = "students-random.csv";
    public static String STUDENT_APPLICATIONS_FILE = "applications-random.csv";

    public static String USER_AGENTS = "user-agents-small.txt";

    public static String THE_RANKINGS_JSON_URL = "https://www.timeshighereducation.com/sites/default/files/the_data_rankings/united_states_rankings_2020_0__fe9db1a86587c174feb9fd3820701c93.json";
    //public static final String THE_RANKINGS_JSON_URL = "http://allv22.all.cs.stonybrook.edu/~stoller/cse416/WSJ_THE/united_states_rankings_2020_limit0_25839923f8b1714cf54659d4e4af6c3b.json";

    public static String FIRST_NAMES_URL = "https://raw.githubusercontent.com/dominictarr/random-name/master/first-names.txt";
    public static String LAST_NAMES_URL = "https://raw.githubusercontent.com/dominictarr/random-name/master/names.txt";

    public static String FIRST_NAMES_FILE = "firstNames.txt";
    public static String LAST_NAMES_FILE = "lastNames.txt";

    public static String SEP = File.separator;
    public static String DATA_DIR = "c4me" + SEP + "src" + SEP + "data" + SEP;

    public static String TEST_HIGH_SCHOOL_URL = "https://www.niche.com/k12/east-islip-high-school-islip-terrace-ny";
    public static String TEST_HIGH_SCHOOL_SEARCH_URL = "https://www.niche.com/search/?q=east%20islip%20high%20school";
    //public static final String TEST_HIGH_SCHOOL_SEARCH_URL = "https://www.google.com/search?q=site%3Awww.niche.com+east+islip+high+school";

    public static String TEST_HIGH_SCHOOL_FILE = "suffolkHighSchools.txt";
    //public static final String TEST_SUFFOLK_HIGH_SCHOOLS = "https://en.wikipedia.org/wiki/List_of_high_schools_in_New_York";

    public static String ALL_HIGH_SCHOOLS_FILE = "all_highschools_sorted.txt";

    public static String NICHE_PREFIX = "https://www.niche.com/k12/";

    public static String COLLEGE_DATA_PREFIX = "https://www.collegedata.com/college/";

    public static void readConfigFile() {
      System.out.println("reading config file");
      File file = TestingDataUtils.findFile("config.txt", "txt");
      if(file == null) {
        System.out.println("Could not find config file; using default values");
        return;
      }
      try {
        Properties properties = new Properties();
        properties.load(new FileReader(file));
        COLLEGE_SCORECARD_FILE = properties.getProperty("COLLEGE_SCORECARD_FILE", COLLEGE_SCORECARD_FILE);
        COLLEGES = properties.getProperty("COLLEGES", COLLEGES);
        COLLEGEDATATXT = properties.getProperty("COLLEGEDATATXT", COLLEGEDATATXT);
        STUDENT_PROFILES_FILE = properties.getProperty("STUDENT_PROFILES_FILE", STUDENT_PROFILES_FILE);
        STUDENT_APPLICATIONS_FILE = properties.getProperty("STUDENT_APPLICATIONS_FILE", STUDENT_APPLICATIONS_FILE);
        USER_AGENTS = properties.getProperty("USER_AGENTS", USER_AGENTS);
        THE_RANKINGS_JSON_URL = properties.getProperty("THE_RANKINGS_JSON_URL", THE_RANKINGS_JSON_URL);
        ALL_HIGH_SCHOOLS_FILE = properties.getProperty("ALL_HIGH_SCHOOLS_FILE", ALL_HIGH_SCHOOLS_FILE);
        NICHE_PREFIX = properties.getProperty("NICHE_PREFIX", NICHE_PREFIX);
        COLLEGE_DATA_PREFIX = properties.getProperty("COLLEGE_DATA_PREFIX", COLLEGE_DATA_PREFIX);
      } catch (IOException e) {
        System.out.println("Could not find configuration file; using default values");
      }
    }
  }

  public final static class CollegeScorecardHeaders {
    public static final String ID                       = "OPEID";
    public static final String NAME                     = "INSTNM";
    public static final String TYPE                     = "SCHTYPE";
    public static final String ADM_RATE                 = "ADM_RATE";
    public static final String CITY                     = "CITY";
    public static final String STATE                    = "STABBR";
    public static final String WEBPAGE                  = "INSTURL";
    public static final String LATITUDE                 = "LATITUDE";
    public static final String LONGITUDE                = "LONGITUDE";
    public static final String IN_STATE_TUITION         = "TUITIONFEE_IN";
    public static final String OUT_STATE_TUITION        = "TUITIONFEE_OUT";
    public static final String MEDIAN_DEBT              = "DEBT_MDN";
    public static final String NUM_STUDENTS_ENROLLED    = "UGDS";
    public static final String RETENTION_RATE           = "RET_FT4";
    public static final String COMPLETION_RATE          = "C150_4";
    public static final String MEAN_EARNINGS            = "MN_EARN_WNE_P10";
    public static final String SATEBRW25                = "SATVR25";
    public static final String SATEBRW50                = "SATVRMID";
    public static final String SATEBRW75                = "SATVR75";
    public static final String SATM25                   = "SATMT25";
    public static final String SATM50                   = "SATMTMID";
    public static final String SATM75                   = "SATMT75";
    public static final String SAT_OVERALL              = "SAT_AVG";
    public static final String ACTE25                   = "ACTEN25";
    public static final String ACTE50                   = "ACTENMID";
    public static final String ACTE75                   = "ACTEN75";
    public static final String ACTM25                   = "ACTMT25";
    public static final String ACTM50                   = "ACTMTMID";
    public static final String ACTM75                   = "ACTMT75";
    public static final String ACT_OVERALL              = "ACTCMMID";

    public static final String ALIAS                    = "ALIAS";

    public static final String[] HEADERS = new String[] {
            ID,
            NAME,
            TYPE,
            ADM_RATE,
            CITY,
            STATE,
            WEBPAGE,
            LATITUDE,
            LONGITUDE,
            IN_STATE_TUITION,
            OUT_STATE_TUITION,
            MEDIAN_DEBT,
            NUM_STUDENTS_ENROLLED,
            RETENTION_RATE,
            COMPLETION_RATE,
            MEAN_EARNINGS,
            SATEBRW25,
            SATEBRW50,
            SATEBRW75,
            SATM25,
            SATM50,
            SATM75,
            SAT_OVERALL,
            ACTE25,
            ACTE50,
            ACTE75,
            ACTM25,
            ACTM50,
            ACTM75,
            ACT_OVERALL
    };
  }

  public static final class StudentProfileHeaders {
    public static final String USER_ID                  = "userid";
    public static final String PASSWORD                 = "password";
    public static final String RES_STATE                = "residence_state";
    public static final String HS_NAME                  = "high_school_name";
    public static final String HS_CITY                  = "high_school_city";
    public static final String HS_STATE                 = "high_school_state";
    public static final String GPA                      = "GPA";
    public static final String COLLEGE_CLASS            = "college_class";
    public static final String MAJOR1                   = "major_1";
    public static final String MAJOR2                   = "major_2";
    public static final String SAT_MATH                 = "SAT_math";
    public static final String SAT_EBRW                 = "SAT_EBRW";
    public static final String ACT_ENG                  = "ACT_English";
    public static final String ACT_MATH                 = "ACT_math";
    public static final String ACT_READING              = "ACT_reading";
    public static final String ACT_SCI                  = "ACT_science";
    public static final String ACT_COMP                 = "ACT_composite";
    public static final String SAT_LIT                  = "SAT_literature";
    public static final String SAT_US_HIST              = "SAT_US_hist";
    public static final String SAT_WORLD_HIST           = "SAT_world_hist";
    public static final String SAT_MATH1                = "SAT_math_I";
    public static final String SAT_MATH2                = "SAT_math_II";
    public static final String SAT_ECO_BIO              = "SAT_eco_bio";
    public static final String SAT_MOL_BIO              = "SAT_mol_bio";
    public static final String SAT_CHEM                 = "SAT_chemistry";
    public static final String SAT_PHYS                 = "SAT_physics";
    public static final String NUM_AP_PASSED            = "num_AP_passed";



    public static final String[] HEADERS = new String[] {
        USER_ID,
        PASSWORD,
        RES_STATE,
        HS_NAME,
        HS_CITY,
        HS_STATE,
        GPA,
        COLLEGE_CLASS,
        MAJOR1,
        MAJOR2,
        SAT_MATH,
        SAT_EBRW,
        ACT_ENG,
        ACT_MATH,
        ACT_READING,
        ACT_SCI,
        ACT_COMP,
        SAT_LIT,
        SAT_US_HIST,
        SAT_WORLD_HIST,
        SAT_MATH1,
        SAT_MATH2,
        SAT_ECO_BIO,
        SAT_MOL_BIO,
        SAT_CHEM,
        SAT_PHYS,
        NUM_AP_PASSED
    };
  }

  public static final class ApplicationFileHeaders {
    public static final String APP_USER_ID                  = "userid";
    public static final String APP_COLLEGE                  = "college";
    public static final String APP_STATUS                   = "status";

    public static final String[] APP_HEADERS = new String[] {
        APP_USER_ID,
        APP_COLLEGE,
        APP_STATUS
    };
  }

  public final static class States {
    public static final String[] STATES = {
            "AL",
            "AK",
            "AZ",
            "AR",
            "CA",
            "CO",
            "CT",
            "DE",
            "DC",
            "FL",
            "GA",
            "HI",
            "ID",
            "IL",
            "IN",
            "IA",
            "KS",
            "KY",
            "LA",
            "ME",
            "MD",
            "MA",
            "MI",
            "MN",
            "MS",
            "MO",
            "MT",
            "NE",
            "NV",
            "NH",
            "NJ",
            "NM",
            "NY",
            "NC",
            "ND",
            "OH",
            "OK",
            "OR",
            "PA",
            "RI",
            "SC",
            "SD",
            "TN",
            "TX",
            "UT",
            "VT",
            "VA",
            "WA",
            "WV",
            "WI",
            "WY"
    };
    public static final List<String> STATES_LIST = Arrays.asList(STATES);
    public static final String[] NORTHEAST = {
            "CT",
            "MA",
            "ME",
            "NH",
            "NJ",
            "NY",
            "PA",
            "RI",
            "VT"
    };
    public static final String[] MIDWEST = {
            "IA",
            "IL",
            "IN",
            "KS",
            "MI",
            "MN",
            "MO",
            "ND",
            "NE",
            "OH",
            "SD",
            "WI"
    };
    public static final String[] SOUTH = {
            "AL",
            "AR",
            "DC",
            "DE",
            "FL",
            "GA",
            "KY",
            "LA",
            "MD",
            "MS",
            "NC",
            "OK",
            "SC",
            "TN",
            "TX",
            "VA",
            "WV"
    };
    public static final String[] WEST = {
            "AK",
            "AZ",
            "CA",
            "CO",
            "HI",
            "ID",
            "MT",
            "NM",
            "NV",
            "OR",
            "UT",
            "WA",
            "WY"
    };
    public static final String NORTHEAST_STR = "Northeast";
    public static final String MIDWEST_STR = "Midwest";
    public static final String SOUTH_STR = "South";
    public static final String WEST_STR = "West";
    public static final List<String> NORTHEAST_LIST = Arrays.asList(NORTHEAST);
    public static final List<String> MIDWEST_LIST = Arrays.asList(MIDWEST);
    public static final List<String> SOUTH_LIST = Arrays.asList(SOUTH);
    public static final List<String> WEST_LIST = Arrays.asList(WEST);

    public static final HashMap<String, List<String>> REGIONS_MAP = new HashMap<String, List<String>>() {
      {
        put(NORTHEAST_STR, NORTHEAST_LIST);
        put(MIDWEST_STR, MIDWEST_LIST);
        put(SOUTH_STR, SOUTH_LIST);
        put(WEST_STR, WEST_LIST);
      }
    };
  }

  public final static class ProfilePropertyClasses {
    public static final String EDUCATION = "education";
    public static final String SAT = "sat";
    public static final String ACT = "act";
    public static final String NONE = "none";

    public static final String[] EDUCATIONPROPERTIES = {
        "gpa",
        "highschoolBySchoolId",
        "major1",
        "major2",
        "majorByMajor1",
        "majorByMajor2",
        "numApCourses",
        "schoolId",
        "schoolYear",
    };
    public static final String[] SATPROPERTIES = {
        "satMath",
        "satMathI",
        "satMathIi",
        "satMolBio",
        "satEcoBio",
        "satPhysics",
        "satUsHist",
        "satWorldHist",
        "satChemistry",
        "satEbrw",
        "satLiterature"
    };
    public static final String[] ACTPROPERTIES = {
        "actComposite",
        "actEnglish",
        "actMath",
        "actReading",
        "actScience"
    };
    public static final String[] NOPROPERTIES = {};
    public static final HashMap<String, String[]> PROPERTIES_MAP = new HashMap<String, String[]>() {
      {
        put(EDUCATION, EDUCATIONPROPERTIES);
        put(SAT, SATPROPERTIES);
        put(ACT, ACTPROPERTIES);
        put(NONE, NOPROPERTIES);
      }
    };
  }


}
