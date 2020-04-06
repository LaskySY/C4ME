package com.c4me.server.core.profile.service;

import com.c4me.server.utils.TestingDataUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.c4me.server.config.constant.Const.Filenames.ALL_HIGH_SCHOOLS_FILE;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-26-2020
 */

@Service
public class GetAllHighschoolNamesServiceImpl {

    private String parseHSName(String name) {
        String[] words = name.split("-");
        StringBuilder builder = new StringBuilder();
        for(int i=0; i<words.length; i++) {
            String word = words[i];
            if(i < words.length-1) {
                if(word.length() > 0) {
                    word = word.substring(0,1).toUpperCase() + word.substring(1);
                }
                builder.append(word+" ");
            }
            if(i == words.length-1) {
                word = word.substring(0, word.length()-1);
                word=word.toUpperCase();
                builder.append(word);
            }
        }
        return builder.toString();
    }
    public List<String> getAllHighschoolNames() throws IOException {
        File all_hs = TestingDataUtils.findFile(ALL_HIGH_SCHOOLS_FILE);
        List<String> highschools = TestingDataUtils.readFile(all_hs);
        return highschools.stream().map(e -> parseHSName(e)).collect(Collectors.toList());
    }

    public static List<String> readFile(File file, String partialInput) throws IOException {
        Reader in = new FileReader(file);
        BufferedReader br = new BufferedReader(in);
        ArrayList<String> lines = new ArrayList<String>();
        String line = "";
        while((line = br.readLine()) != null) {
            if(line.startsWith(partialInput.toLowerCase().replace(" ", "-"))) lines.add(line);
        }
        return lines;
    }
    public List<String> getAllHighschoolNames(String partialInput) throws IOException {
        File all_hs = TestingDataUtils.findFile(ALL_HIGH_SCHOOLS_FILE);
        List<String> highschools = readFile(all_hs, partialInput);
        return highschools.stream().map(e -> parseHSName(e)).collect(Collectors.toList());
    }

}
