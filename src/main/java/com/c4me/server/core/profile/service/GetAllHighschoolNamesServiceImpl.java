package com.c4me.server.core.profile.service;

import com.c4me.server.utils.TestingDataUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.c4me.server.config.constant.Const.Filenames.ALL_HIGH_SCHOOLS_FILE;

/**
 * @Description: Implementation of the getAllHighSchoolNames service
 * @Author: Maciej Wlodek
 * @CreateDate: 03-26-2020
 */

@Service
public class GetAllHighschoolNamesServiceImpl {

    /**
     * Parse a niche.com url suffix into a high school name, city, and state
     * @param name {@link String}
     * @return {@link String} parsed name
     */
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
                if(word.endsWith("/")) word = word.substring(0, word.length()-1);
                word=word.toUpperCase();
                builder.append(word);
            }
        }
        return builder.toString();
    }

    /**
     * Get the list of all high school names
     * @return {@link List} of high school names present on Niche.com
     * @throws IOException
     */
    public List<String> getAllHighschoolNames() throws IOException {
        File all_hs = TestingDataUtils.findFile(ALL_HIGH_SCHOOLS_FILE);
        List<String> highschools = TestingDataUtils.readFile(all_hs);
        return highschools.stream().map(this::parseHSName).collect(Collectors.toList());
    }

    /**
     * Read a file and return the list of lines matching the partial input
     * @deprecated
     * @param file {@link File}
     * @param partialInput {@link String}
     * @return {@link List} of high school names
     * @throws IOException
     */
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

    /**
     * Get a list of names matching some partial input
     * @deprecated
     * @param partialInput {@link String}
     * @return {@link List} of high school names matching the partial input
     * @throws IOException
     */
    public List<String> getAllHighschoolNames(String partialInput) throws IOException {
        File all_hs = TestingDataUtils.findFile(ALL_HIGH_SCHOOLS_FILE);
        List<String> highschools = readFile(all_hs, partialInput);
        return highschools.stream().map(this::parseHSName).collect(Collectors.toList());
    }

}
