package com.c4me.server.core.admin.controller;


import com.c4me.server.config.exception.*;
import com.c4me.server.core.admin.service.ImportStudentProfileServiceImpl;
import com.c4me.server.domain.BaseResponse;
import java.util.Iterator;
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
@RequestMapping("/admin/profile")
public class ImportStudentProfileController {

  @Autowired
  ImportStudentProfileServiceImpl importStudentProfileService;

  @PostMapping
  public BaseResponse importStudentProfile()
          throws IOException, NoStudentProfileCSVException, InvalidStudentProfileException, HighSchoolDoesNotExistException, InvalidStudentApplicationException, NoStudentApplicationCSVException, DuplicateUsernameException {

    System.out.println("current dir = " + System.getProperty("user.dir"));

    File topDir = new File(System.getProperty("user.dir"));
    Iterator<File> files = FileUtils.iterateFiles(topDir, new String[] {"csv"}, true);

    String studentProfilesFilename = "";
    boolean b1 = false, b2 = false;

    while(files.hasNext()) {
      File f = files.next();
      String extension = FilenameUtils.getExtension(f.getAbsolutePath());
      if (f.getName().equals(STUDENT_PROFILES_FILE) && extension.equals("csv")) {
        studentProfilesFilename = f.getAbsolutePath();
        b1 = true;
        if (b2)
          break;
      }
    }

    if(studentProfilesFilename.equals("")) throw new NoStudentProfileCSVException("Student profiles file you requested was not found");
    importStudentProfileService.importStudentProfileCsv(studentProfilesFilename);

    return BaseResponse.builder()
        .code("success")
        .message("")
        .data(null).build();

  }

}
