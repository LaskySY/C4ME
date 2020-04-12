package com.c4me.server.core.admin.controller;

import com.c4me.server.config.exception.InvalidCollegeScorecardException;
import com.c4me.server.config.exception.NoCollegeScorecardException;
import com.c4me.server.config.exception.NoCollegeTxtException;
import com.c4me.server.core.admin.service.CollegeScorecardServiceImpl;
import com.c4me.server.domain.BaseResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.Iterator;
import java.util.List;

import static com.c4me.server.config.constant.Const.Filenames.*;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-20-2020
 */

@RestController
@RequestMapping("/admin/college/scorecard")
public class CollegeScorecardController {

    @Autowired
    CollegeScorecardServiceImpl collegeScorecardService;

    @PostMapping
    public BaseResponse importCollegeScorecard() throws IOException, NoCollegeScorecardException, NoCollegeTxtException, InvalidCollegeScorecardException {
        System.out.println("current dir = " + System.getProperty("user.dir"));

        File topDir = new File(System.getProperty("user.dir"));
        Iterator<File> files = FileUtils.iterateFiles(topDir, new String[] {"csv", "txt"}, true);

        String scorecardFilename = "";
        String collegesFilename = "";
        boolean b1 = false, b2 = false;

        while(files.hasNext()) {
            File f = files.next();
            String extension = FilenameUtils.getExtension(f.getAbsolutePath());
            if(f.getName().equals(COLLEGE_SCORECARD_FILE) && extension.equals("csv")) {
                scorecardFilename = f.getAbsolutePath();
                b1 = true;
                if(b2) break;
            }
            else if (f.getName().equals(COLLEGES) && extension.equals("txt")) {
                collegesFilename = f.getAbsolutePath();
                b2 = true;
                if(b1) break;
            }
        }
        if(collegesFilename.equals("")) throw new NoCollegeTxtException("college.txt not found");
        List<String> colleges = collegeScorecardService.readCollegesTxt(collegesFilename);

        collegeScorecardService.importCsv(scorecardFilename, colleges);

        return BaseResponse.builder()
                .code("success")
                .message("")
                .data(null).build();
    }
}
