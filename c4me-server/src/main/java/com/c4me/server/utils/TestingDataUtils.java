package com.c4me.server.utils;

import com.c4me.server.config.constant.Const;
import com.c4me.server.core.credential.repository.HighschoolRepository;
import com.c4me.server.entities.HighschoolEntity;
import com.c4me.server.entities.StudentApplicationEntity;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import static com.c4me.server.config.constant.Const.Filenames.*;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-22-2020
 */

public class TestingDataUtils {

    @Autowired
    static HighschoolRepository highschoolRepository;

    private static Random r = new Random(System.currentTimeMillis());


    private static String generateUsername(String firstName, String lastName) {
        String username = firstName.toLowerCase();
        username += lastName;
        int rand3 = r.nextInt(10000);
        username += rand3+"";
        return username;
    }
    private static Integer t(int type, double mean, double sigma) {
        double include = r.nextDouble();
        if(include < 0.1) return null;

        double nextG = r.nextGaussian();
        double score = nextG * sigma + mean;
        switch(type) {
            case 0: score = Math.max(Math.min(40, score), 10); break;
            case 1: score = Math.max(Math.min(800, score), 200); break;
            case 2: score = Math.max(Math.min(36, score), 1); break;
        }
        return new Integer((int) score);
    }
    private static StudentCSVRecord generateStudent(List<String> firstNames, List<String> lastNames, Page<HighschoolEntity> highSchools) {
        int firstNameIndex = r.nextInt(firstNames.size());
        int lastNameIndex = r.nextInt(lastNames.size());
        String firstName = firstNames.get(firstNameIndex);
        String lastName = lastNames.get(lastNameIndex);
        String username = generateUsername(firstName, lastName);

        double strength = Math.min(1, Math.max(0, r.nextGaussian()*0.2 + 0.7));
        int s = (int) (470 * strength + 300);
        int a = (int) (23 * strength + 10);
        int g = (int) (20 * strength + 15);

        int ss = 30;
        int as = 3;
        int gs = 2;





        return new StudentCSVRecord(username, username, "NY", "", "", "NY", t(0, g, gs)/10.0, 2024, "", "", t(1,s,ss), t(1,s,ss), t(2,a,as),t(2,a,as), t(2,a,as),t(2,a,as),t(2,a,as), t(1,s,ss),t(1,s,ss),t(1,s,ss), t(1,s,ss), t(1,s,ss), t(1,s,ss),t(1,s,ss), t(1,s,ss), t(1,s,ss), r.nextInt(10));
    }
    private static List<String> readFile(File file) throws IOException {
        Reader in = new FileReader(file);
        BufferedReader  br = new BufferedReader(in);
        ArrayList<String> lines = new ArrayList<String>();
        String line = "";
        while((line = br.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }
    private static File findFile(String filename) {

        File topDir = new File(System.getProperty("user.dir"));
        Iterator<File> files = FileUtils.iterateFiles(topDir, new String[] {"txt"}, true);

        File outFile = null;

        while(files.hasNext()) {
            File f = files.next();
            if(f.getName().equals(filename)) {
                outFile = f;
                break;
            }
        }
        return outFile;
    }
    private static void downloadFile(File file, String webpage) {
        try {
            URL url = new URL(webpage);
            FileUtils.copyURLToFile(url, file);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void generateStudents(int numStudents, String filename) {
        File firstNamesFile = findFile(FIRST_NAMES_FILE);
        File lastNamesFile = findFile(LAST_NAMES_FILE);
        try {
//            firstNamesFile = File.createTempFile("firstNames", "txt");
//            lastNamesFile = File.createTempFile("lastNames", "txt");
            if(firstNamesFile == null) {
                firstNamesFile = new File(DATA_DIR + FIRST_NAMES_FILE);
                firstNamesFile.createNewFile();
                System.out.println("downloading first names");
                downloadFile(firstNamesFile, FIRST_NAMES_URL);
            }
            if(lastNamesFile == null) {
                lastNamesFile = new File(DATA_DIR + LAST_NAMES_FILE);
                lastNamesFile.createNewFile();
                System.out.println("downloading last names");
                downloadFile(lastNamesFile, LAST_NAMES_URL);
            }
            List<String> firstNames = readFile(firstNamesFile);
            List<String> lastNames = readFile(lastNamesFile);

            Pageable request = PageRequest.of(0, 50);
            Page<HighschoolEntity> highschoolEntities = highschoolRepository.findAll(request);

            File outputFile = new File(filename);
            outputFile.createNewFile();
            FileWriter fw = new FileWriter(outputFile);
            BufferedWriter bw = new BufferedWriter(fw);
            CSVPrinter printer = new CSVPrinter(bw, CSVFormat.EXCEL);
            printer.printRecord("userid", "password","residence_state", "high_school_name", "high_school_city", "high_school_state","GPA", "college_class","major_1","major_2", "SAT_math", "SAT_EBRW", "ACT_English", "ACT_math", "ACT_reading", "ACT_science", "ACT_composite", "SAT_literature", "SAT_US_hist", "SAT_world_hist", "SAT_math_I", "SAT_math_II", "SAT_eco_bio", "SAT_mol_bio", "SAT_chemistry", "SAT_physics","num_AP_passed");
            for(int i=0; i<numStudents; i++) {
                StudentCSVRecord record = generateStudent(firstNames, lastNames, highschoolEntities);
                printer.printRecord(record.userid, record.password,record.residence_state, record.high_school_name, record.high_school_city, record.high_school_state,record.GPA, null,record.major_1,record.major_2, record.SAT_math, record.SAT_EBRW, record.ACT_English, record.ACT_math, record.ACT_reading, record.ACT_science, record.ACT_composite, record.SAT_literature, record.SAT_US_hist, record.SAT_world_hist, record.SAT_math_I, record.SAT_math_II, record.SAT_eco_bio, record.SAT_mol_bio, record.SAT_chemistry, record.SAT_physics, record.num_AP_passed);
            }

            printer.close();
            bw.close();
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
