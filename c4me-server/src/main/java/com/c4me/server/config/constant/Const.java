package com.c4me.server.config.constant;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    public static final String AUTHENTICATION = "513";
    public static final String MISS_TOKEN = "514";
    public static final String TOKEN_EXPIRED = "515";
    public static final String ACCESS_DENIED = "516";
    public static final String USER_NOT_FOUND = "userNotExist";
    public static final String COLLEGE_NOT_FOUND = "collegeInvalid";
    public static final String COLLEGE_SCORECARD_NOT_FOUND = "fail";
    public static final String COLLEGE_TXT_NOT_FOUND = "collegeTxtNotFound";
    public static final String INVALID_COLLEGE_SCORECARD = "invalidScorecardFile";
  }

  public final static class Role {
    public static final String ADMIN = "Admin";
    public static final String STUDENT = "Student";
  }

  public final static class Status {
    public static final Integer PENDING = 1;
    public static final Integer ACCEPTED = 2;
    public static final Integer DENIED = 3;
    public static final Integer WAITLISTED = 4;
  }

  public final static class Ranges {
    public static final Double MIN_ZSCORE_FOR_QUESTIONABLE = 1.645;
    public static final Integer MIN_STUDENTS_FOR_STDDEV = 10;
    public static final Integer MIN_SAT = 200;
    public static final Integer MAX_SAT = 800;
    public static final Integer MIN_ACT = 1;
    public static final Integer MAX_ACT = 36;
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

  public final static class Filenames {
    public static final String COLLEGE_SCORECARD_FILE = "Most-Recent-Cohorts-All-Data-Elements.csv";
    public static final String COLLEGES = "colleges.txt";

    public static final String THE_RANKINGS_JSON_URL = "https://www.timeshighereducation.com/sites/default/files/the_data_rankings/united_states_rankings_2020_limit0_25839923f8b1714cf54659d4e4af6c3b.json";
    //public static final String THE_RANKINGS_JSON_URL = "http://allv22.all.cs.stonybrook.edu/~stoller/cse416/WSJ_THE/united_states_rankings_2020_limit0_25839923f8b1714cf54659d4e4af6c3b.json";
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
    public static final String MEDIAN_DEBT              = "DEBT_MDN"; //TODO: maybe GRAD_DEBT_MDN instead?
    public static final String NUM_STUDENTS_ENROLLED    = "UGDS"; //TODO: maybe UG instead?
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

}
