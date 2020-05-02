package com.c4me.server.core.admin.controller;


import com.c4me.server.config.annotation.LogAndWrap;
import com.c4me.server.config.exception.*;
import com.c4me.server.core.admin.service.ImportStudentProfileServiceImpl;
import com.c4me.server.domain.BaseResponse;
import java.util.Iterator;

import com.c4me.server.utils.TestingDataUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

import static com.c4me.server.config.constant.Const.Filenames.*;

/**
 * @Description:
 * @Author: Yousef Khan
 * @CreateDate: 03-25-2020
 */

@RestController
@RequestMapping("/api/v1/admin/profile")
public class ImportStudentProfileController {

  @Autowired
  ImportStudentProfileServiceImpl importStudentProfileService;

  @PostMapping
  @LogAndWrap(log="importing profiles and applications")
  public void importStudentProfilesAndApplications()
          throws IOException, NoStudentProfileCSVException, InvalidStudentProfileException, HighSchoolDoesNotExistException, InvalidStudentApplicationException, NoStudentApplicationCSVException, DuplicateUsernameException {

    File studentProfilesFile = TestingDataUtils.findFile(STUDENT_PROFILES_FILE, "csv");

    if(studentProfilesFile == null) throw new NoStudentProfileCSVException("Student profiles file you requested was not found");
    importStudentProfileService.importStudentProfileCsv(studentProfilesFile);

    File studentApplicationsFile = TestingDataUtils.findFile(STUDENT_APPLICATIONS_FILE, "csv");
    importStudentProfileService.importStudentApplicationsCsv(studentApplicationsFile);
  }

}
