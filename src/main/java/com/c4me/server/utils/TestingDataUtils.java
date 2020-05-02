package com.c4me.server.utils;

import com.c4me.server.config.constant.Const;
import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.entities.CollegeEntity;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static com.c4me.server.config.constant.Const.Filenames.*;

/**
 * @Description: Utility functions for creating testing data
 * @Author: Maciej Wlodek
 * @CreateDate: 03-22-2020
 */

public class TestingDataUtils {

    @Autowired
    CollegeRepository collegeRepository;

    private static Random r = new Random(System.currentTimeMillis());
    private static String[] majors = {"Computer Science", "Mathematics", "Biology", "Chemistry", "Physics", "Music Theory", "Art History", "Political Science", "History", "Geology"};
    private static List<String> majorsList = Arrays.asList(majors);

    /**
     * Generate a username from a first and last name
     * @param firstName {@link String}
     * @param lastName {@link String}
     * @return {@link String} the username
     */
    private static String generateUsername(String firstName, String lastName) {
        return firstName.toLowerCase() + lastName + r.nextInt(10000);
    }

    /**
     * Generate a random score according to the type of test and student strength
     * @param type int - the type of score (0,1,or 2 for gpa, sat, act resp.)
     * @param mean double
     * @param sigma double
     * @return {@link Integer} the student's score
     */
    private static Integer t(int type, double mean, double sigma) {
        double nextG = r.nextGaussian();
        double score = nextG * sigma + mean;
        switch(type) {
            case 0: score = Math.max(Math.min(40, score), 10); break;
            case 1: score = 10 * ((int) (Math.max(Math.min(800, score), 200)/10)); break;
            case 2: score = Math.max(Math.min(36, score), 1); break;
            default: break;
        }
        return new Integer((int) score);
    }

    /**
     * @param firstNames {@link List} of firstNames to choose from
     * @param lastNames {@link List} of lastNames to choose from
     * @param highschools {@link List} of highschoolNames to choose from
     * @return {@link Pair} of a {@link StudentCSVRecord} student and {@link Double} academic strength
     */
    private static Pair<StudentCSVRecord, Double> generateStudent(List<String> firstNames, List<String> lastNames, List<String> highschools) {
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

        int hsIndex = r.nextInt(highschools.size()-1) + 1;
        String hsEntry = highschools.get(hsIndex);
        String[] split = hsEntry.split(", ");
        String hsName = split[0];
        String hsCity = split[1];

        Collections.shuffle(majorsList);

        return Pair.of(new StudentCSVRecord(username, username, "NY", hsName, hsCity, "NY", t(0, g, gs)/10.0, 2024, majorsList.get(0), majorsList.get(1), t(1,s,ss), t(1,s,ss), t(2,a,as),t(2,a,as), t(2,a,as),t(2,a,as),t(2,a,as), t(1,s,ss),t(1,s,ss),t(1,s,ss), t(1,s,ss), t(1,s,ss), t(1,s,ss),t(1,s,ss), t(1,s,ss), t(1,s,ss), r.nextInt(10)), strength);
    }

    /**
     * Read a file line by line
     * @param file {@link File} to read from
     * @return {@link List} of lines
     * @throws IOException
     */
    public static List<String> readFile(File file) throws IOException {
        Reader in = new FileReader(file);
        BufferedReader  br = new BufferedReader(in);
        ArrayList<String> lines = new ArrayList<String>();
        String line = "";
        while((line = br.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

    /**
     * Find a .txt file in the project directory
     * @param filename {@link String} - the name of the .txt file (no path)
     * @return {@link File} the found file, or null if it doesn't exist
     */
    public static File findFile(String filename) {
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

    /**
     * Find a file in the project directory
     * @param filename {@link String} - the name of the file (no path)
     * @param extension {@link String} - the file extension name, e.g. .csv
     * @return {@link File} - the found file or null if it doesn't exist
     */
    public static File findFile(String filename, String extension) {
        File topDir = new File(System.getProperty("user.dir"));
        Iterator<File> files = FileUtils.iterateFiles(topDir, new String[] {extension}, true);

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

    /**
     * Download a file from the web to a local file
     * @param file {@link File} file to download to
     * @param webpage {@link String} url to download from
     */
    public static void downloadFile(File file, String webpage) {
        try {
            URL url = new URL(webpage);
            FileUtils.copyURLToFile(url, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate a students.csv file with random students
     * @param numStudents int - the number of students to generate
     * @param filename {@link String} - the filename to create
     * @return {@link HashMap} of usernames -> academic strengths (used by {@link #generateStudentApplications})
     */
    public static HashMap<String, Double> generateStudents(int numStudents, String filename) {
        File firstNamesFile = findFile(FIRST_NAMES_FILE);
        File lastNamesFile = findFile(LAST_NAMES_FILE);
        File highschoolFile = findFile(TEST_HIGH_SCHOOL_FILE);
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
            List<String> highSchools = readFile(highschoolFile);

            HashMap<String, Double> students = new HashMap<>();

            File outputFile = new File(filename);
            outputFile.createNewFile();
            FileWriter fw = new FileWriter(outputFile);
            BufferedWriter bw = new BufferedWriter(fw);
            CSVPrinter printer = new CSVPrinter(bw, CSVFormat.EXCEL);
            printer.printRecord("userid", "password","residence_state", "high_school_name", "high_school_city", "high_school_state","GPA", "college_class","major_1","major_2", "SAT_math", "SAT_EBRW", "ACT_English", "ACT_math", "ACT_reading", "ACT_science", "ACT_composite", "SAT_literature", "SAT_US_hist", "SAT_world_hist", "SAT_math_I", "SAT_math_II", "SAT_eco_bio", "SAT_mol_bio", "SAT_chemistry", "SAT_physics","num_AP_passed");
            for(int i=0; i<numStudents; i++) {
                Pair<StudentCSVRecord, Double> student = generateStudent(firstNames, lastNames, highSchools);
                StudentCSVRecord record = student.getFirst();
                printer.printRecord(record.userid, record.password,record.residence_state, record.high_school_name, record.high_school_city, record.high_school_state,record.GPA, record.college_class,record.major_1,record.major_2, record.SAT_math, record.SAT_EBRW, record.ACT_English, record.ACT_math, record.ACT_reading, record.ACT_science, record.ACT_composite, record.SAT_literature, record.SAT_US_hist, record.SAT_world_hist, record.SAT_math_I, record.SAT_math_II, record.SAT_eco_bio, record.SAT_mol_bio, record.SAT_chemistry, record.SAT_physics, record.num_AP_passed);
                students.put(record.userid, student.getSecond());
            }
            printer.close();
            bw.close();
            fw.close();

            return students;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Generate a random applications.csv file
     * @param students {@link HashMap} the return value from {@link #generateStudents}
     * @param collegeEntities {@link List} of {@link CollegeEntity}'s to choose from
     * @param appsPerStudent int - average number of applications to generate per user
     * @param filename {@link String}
     */
    private static void generateStudentApplications(HashMap<String, Double> students, List<CollegeEntity> collegeEntities, int appsPerStudent, String filename)  {
        //File collegesFile = findFile(COLLEGES);
        try {
            //List<String> collegeNames = readFile(collegesFile);
            String[] status = {"pending", "accepted", "denied", "wait-listed"};
            File outputFile = new File(filename);
            outputFile.createNewFile();
            FileWriter fw = new FileWriter(outputFile);
            BufferedWriter bw = new BufferedWriter(fw);
            CSVPrinter printer = new CSVPrinter(bw, CSVFormat.EXCEL);
            printer.printRecord("userid", "college", "status");
            for(String user : students.keySet()) {
                int nApps = r.nextInt(2 * appsPerStudent);
                nApps = Math.min(nApps, collegeEntities.size());
                Collections.shuffle(collegeEntities);
                for(int j=0; j<nApps; j++) {
                    int s = 0;
                    int rand = r.nextInt(2);
                    if(rand == 1) {
                        Double strength = students.get(user);
                        Double admissionRate = collegeEntities.get(j).getAdmissionRate();
                        if(admissionRate == null) admissionRate = 1.0;
                        Double rejectionRate = 1-admissionRate;
                        Double y = strength > rejectionRate? admissionRate + Math.pow(strength-rejectionRate,1.5) : admissionRate - Math.pow(rejectionRate-strength, 1.5);
                        Double x = r.nextDouble();
                        if(x < y) {
                            if(r.nextDouble() < 0.2) s = 3;
                            else s=1;
                        }
                        else {
                            if(r.nextDouble() < 0.1) s=3;
                            else s=2;
                        }
                    }
                    printer.printRecord(user, collegeEntities.get(j).getName(), status[s]);
                    //StudentApplicationCSVRecord record = generateStudentApplication(user, students.get(user));
                    //printer.printRecord();
                }
            }
            printer.close();
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Generate a random students.csv and applications.csv file
     * @param numStudents int
     * @param averageNumberApplicationsPerStudent int
     * @param collegeEntities {@link List} of {@link CollegeEntity}
     * @param studentsFilename {@link String}
     * @param applicationsFilename {@link String}
     */
    public static void generateStudentsAndApplications(int numStudents, int averageNumberApplicationsPerStudent, List<CollegeEntity> collegeEntities, String studentsFilename, String applicationsFilename) {
        HashMap<String, Double> students = generateStudents(numStudents, studentsFilename);
        generateStudentApplications(students, collegeEntities, averageNumberApplicationsPerStudent, applicationsFilename);
    }
}
