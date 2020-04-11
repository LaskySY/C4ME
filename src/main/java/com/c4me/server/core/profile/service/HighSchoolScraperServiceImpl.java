package com.c4me.server.core.profile.service;

import com.c4me.server.config.exception.HighSchoolDoesNotExistException;
import com.c4me.server.core.credential.repository.HighschoolRepository;
import com.c4me.server.core.profile.repository.CollegeHighSchoolAssociationRepository;
import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.core.profile.repository.HighschoolMajorAssociationRepository;
import com.c4me.server.core.profile.repository.MajorRepository;
import com.c4me.server.entities.*;
import com.c4me.server.utils.SearchHSUtils;
import com.c4me.server.utils.TestingDataUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.c4me.server.config.constant.Const.AcademicQuality.LETTER_TO_NUMBER_GRADE;
import static com.c4me.server.config.constant.Const.Filenames.*;
import static com.c4me.server.config.constant.Const.Ranges.*;
import static com.c4me.server.config.constant.Const.States.STATES_LIST;
import static com.c4me.server.config.constant.Const.Types.*;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-22-2020
 */

@Service
public class HighSchoolScraperServiceImpl {

    @Autowired
    HighschoolRepository highschoolRepository;
    @Autowired
    CollegeRepository collegeRepository;
    @Autowired
    CollegeHighSchoolAssociationRepository collegeHighSchoolAssociationRepository;
    @Autowired
    MajorRepository majorRepository;
    @Autowired
    HighschoolMajorAssociationRepository highschoolMajorAssociationRepository;

    Random random = new Random(System.currentTimeMillis());

    public boolean scrapeBaseSite(String webpage, HighschoolEntity highschoolEntity) throws IOException {
        URL url = new URL(webpage);
        Document nicheBaseDoc = openWebpage(url);
        //Document nicheBaseDoc = openWebpage("base_page_test.html");


//        Element nameHeader = nicheBaseDoc.selectFirst("h1.postcard__title");
//        if(nameHeader == null) return false;
//        highschoolEntity.setName(nameHeader.ownText());


        Element attributesElement = nicheBaseDoc.selectFirst("ul.postcard__attrs");
        Elements gradeAttribute = attributesElement.select("li.postcard__attr.postcard__attr--has-grade");
        if(gradeAttribute.size() >= 1) {
            Element grade = gradeAttribute.first();
            String nicheGrade = grade.select("div").text();
            System.out.println("nicheGrade = " + nicheGrade);
            if(LETTER_TO_NUMBER_GRADE.containsKey(nicheGrade)) {
                highschoolEntity.setAcademicQuality(nicheGrade);
            }
        }

        Elements attributeFacts = attributesElement.select("li.postcard__attr.postcard-fact");
        for(Element e : attributeFacts) {
            String text = e.ownText();
            if(text.contains(PUBLIC_STR)) {
                System.out.println("Public HS");
                highschoolEntity.setType(PUBLIC_STR);
            }
            if(text.contains(PRIVATE_STR)) {
                System.out.println("Private HS");
                highschoolEntity.setType(PRIVATE_STR);
            }
            if(text.contains(", ")) {
                String[] location = text.split(", ");
                if (STATES_LIST.contains(location[1])) {
                    System.out.println("city = " + location[0] + ", state = " + location[1]);
                    highschoolEntity.setCity(location[0]);
                    highschoolEntity.setState(location[1]);
                }
            }
        }
        return true;
    }

    //return map with two elements: ("majors", List of major strings), ("colleges", List of college names)
    public HashMap<String, List<String>> scrapeAcademicsSite(String webpage, HighschoolEntity highschoolEntity) throws IOException {
        String suffix = "academics/";
        String academicsUrl = webpage.endsWith("/")? webpage + suffix : webpage + "/" + suffix;

        URL url = new URL(academicsUrl);
        Document doc = openWebpage(url);
        //Document doc = openWebpage("academics_page_test.html");

        Element satActScores = doc.getElementById("sat-act-scores");
        getAllScores(satActScores, highschoolEntity);

        Element collegesElement = doc.getElementById("colleges-students-are-considering");
        List<String> popularColleges = getPopularColleges(collegesElement);

        Element majorsElement = doc.getElementById("what-students-want-to-study");
        List<String> popularMajors = getPopularMajors(majorsElement);

        HashMap<String, List<String>> result = new HashMap<>();
        result.put("colleges", popularColleges);
        result.put("majors", popularMajors);

        return result;
    }

    public List<String> getPopularColleges(Element collegesElement) {
        if(collegesElement == null) return new ArrayList<String>();
        List<String> popularColleges = new ArrayList<>();
        Elements colleges = collegesElement.select("h6.popular-entity__name");
        for(Element college : colleges) {
            popularColleges.add(college.text());
            System.out.println("college = " + college.text());
        }
        return popularColleges;
    }
    public List<String> getPopularMajors(Element majorsElement) {
        if(majorsElement == null) return new ArrayList<>();
        List<String> popularMajors = new ArrayList<>();
        Elements majors = majorsElement.select("h6.popular-entity__name");
        for(Element major : majors) {
            popularMajors.add(major.text());
            System.out.println("major = " + major.text());
        }
        return popularMajors;
    }

    public void getAllScores(Element scoresElement, HighschoolEntity highschoolEntity) {
        if(scoresElement == null || highschoolEntity == null) return;
        Elements scalars = scoresElement.select("div.scalar__label");
        for(Element scalar : scalars) {
            for(Element c : scalar.children()) {
                if(c.ownText().contains("SAT")) {
                    Integer score = getScore(scalar);
                    if(!(score == -1 || score < MIN_SAT_OVERALL || score > MAX_SAT_OVERALL)) {
                        highschoolEntity.setSatOverall(score);
                    }
                }
                else if (c.ownText().contains("ACT")) {
                    Integer score = getScore(scalar);
                    if(!(score == -1 || score < MIN_ACT || score > MAX_ACT)) {
                        highschoolEntity.setActComposite(score);
                    }
                }
                else if (c.ownText().contains("Math")) {
                    Integer score = getScore(scalar);
                    if(!(score == -1 || score < MIN_SAT || score > MAX_SAT)) {
                        highschoolEntity.setSatMath(score);
                    }
                    else if (!(score == -1 || score < MIN_ACT || score > MAX_ACT)) {
                        highschoolEntity.setActMath(score);
                    }
                }
                else if (c.ownText().contains("Verbal")) {
                    Integer score = getScore(scalar);
                    if(!(score == -1 || score < MIN_SAT || score > MAX_SAT)) {
                        highschoolEntity.setSatEbrw(score);
                    }
                }
                else if (c.ownText().contains("English")) {
                    Integer score = getScore(scalar);
                    if (!(score == -1 || score < MIN_ACT || score > MAX_ACT)) {
                        highschoolEntity.setActEnglish(score);
                    }
                }
                else if (c.ownText().contains("Reading")) {
                    Integer score = getScore(scalar);
                    if (!(score == -1 || score < MIN_ACT || score > MAX_ACT)) {
                        highschoolEntity.setActReading(score);
                    }
                }
                else if (c.ownText().contains("Science")) {
                    Integer score = getScore(scalar);
                    if (!(score == -1 || score < MIN_ACT || score > MAX_ACT)) {
                        highschoolEntity.setActScience(score);
                    }
                }
            }
        }
    }

    public Integer getScore(Element scalarLabel) {
        if(scalarLabel == null || scalarLabel.siblingElements().size() == 0) return -1;
        Element parent = scalarLabel.parent();
        Elements values = parent.select("div.scalar__value");
        if(values.size() != 1) return -1;
        Element value = values.get(0);
        try {
            return Integer.parseInt(value.ownText());
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }

    public Document openWebpage(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        System.out.println(conn.getRequestProperties());

        File userAgentsFile = TestingDataUtils.findFile(USER_AGENTS, "txt");
        List<String> userAgents = TestingDataUtils.readFile(userAgentsFile);
        String userAgent = userAgents.get(random.nextInt(userAgents.size()));

        //Jsoup.connect().

        //conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
        //conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36 OPR/67.0.3575.115");
        conn.setRequestProperty("User-Agent", userAgent);
        //conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)");

        System.out.println(conn.getRequestProperties());

        String line = null;
        StringBuilder tmp = new StringBuilder();
        BufferedReader buf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        while ((line = buf.readLine()) != null) {
            tmp.append(line);
        }
        Document doc = Jsoup.parse(tmp.toString());
        return doc;
    }

    //temporarily
//    public Document openWebpage(String filename) throws IOException {
//        File file = TestingDataUtils.findFile(filename, "html");
//        String line = null;
//        StringBuilder tmp = new StringBuilder();
//        FileReader fr = new FileReader(file);
//        BufferedReader buf = new BufferedReader(fr);
//        while((line = buf.readLine()) != null) {
//            tmp.append(line);
//        }
//        return Jsoup.parse(tmp.toString());
//    }

    //matches earlier in the line are worth more (more likely matching high school and not just accidental city matches)
    public String getBestMatch(HashMap<String, Integer> matches) {
        int min = Integer.MAX_VALUE;
        String bestMatch = null;
        for(String match : matches.keySet()) {
            if(matches.get(match) < min) {
                min = matches.get(match);
                bestMatch = match;
            }
        }
        return bestMatch;
    }

//    public List<String> findMatches(String query) throws IOException {
//        HashMap<String, Integer> matches =  SearchHSUtils.searchForNicheUrl(query);
//        return matches.keySet().stream().collect(Collectors.toList());
//    }

    public String buildUrl(String match) {
        String url = NICHE_PREFIX + match;
        if(!url.endsWith("/")) url += "/";
        return url;
    }
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

    public HighschoolEntity scrapeHighSchool(String query) throws IOException, HighSchoolDoesNotExistException {
        return scrapeHighSchool(query, true);
    }
    public HighschoolEntity scrapeHighSchool(String query, boolean scrapeEvenIfFound) throws IOException, HighSchoolDoesNotExistException {
        System.out.println("trying to find niche url");
//        HashMap<String, Integer> matches =  SearchHSUtils.searchForNicheUrl(query);
//        String bestMatch = getBestMatch(matches);

        String bestMatch = SearchHSUtils.searchForNicheUrl(query);
        System.out.println("best match = " + bestMatch);

        if(bestMatch == null) throw new HighSchoolDoesNotExistException("could not find high school");

        String parsedName = parseHSName(bestMatch);
        HighschoolEntity entity = highschoolRepository.findByName(parsedName);
        if(entity == null) {
            entity = new HighschoolEntity();
            entity.setName(parsedName);
        }
        else {
            if(!scrapeEvenIfFound) {
                System.out.println("hs already exists -- returning existing entity");
                return entity;
            }
            else System.out.println("hs already exists, scraping to update info");
        }

//        try {
//            System.out.println("timeout before next scrape");
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        System.out.println("trying to scrape niche.com for " + bestMatch);
        String url = buildUrl(bestMatch);
        scrapeBaseSite(url, entity);
//        try {
//            System.out.println("timeout before next scrape");
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        HashMap<String, List<String>> result = scrapeAcademicsSite(url, entity);

        if(!highschoolRepository.existsByName(entity.getName())) { //final check to make sure the high school doesn't yet exist
            highschoolRepository.save(entity);
        }

        entity = highschoolRepository.findByName(entity.getName());

        List<String> colleges = result.get("colleges");
        List<String> majors = result.get("majors");

        List<CollegeEntity> collegesInDatabase = collegeRepository.findAll();
        List<String> collegeNamesInDatabase = collegesInDatabase.stream().map(e -> e.getName()).collect(Collectors.toList());
        for(String college : colleges) {
            int index = matchCollege(college, collegeNamesInDatabase);
            if(index != -1) {
                System.out.println("found match");
                CollegeEntity ce = collegesInDatabase.get(index);
                CollegeHighschoolAssociationEntityPK ch_pk = CollegeHighschoolAssociationEntityPK.builder()
                        .college_id(ce.getId())
                        .highschool_id(entity.getSchoolId()).build();
                CollegeHighschoolAssociationEntity ch = CollegeHighschoolAssociationEntity.builder()
                        .collegeByCollegeId(ce)
                        .highschoolByHighschoolId(entity)
                        .collegeHighschoolAssociationEntityPK(ch_pk).build();
                collegeHighSchoolAssociationRepository.save(ch);
            }
        }

        List<MajorEntity> majorEntities = majorRepository.findAll();
        List<String> majorsInDatabase = majorEntities.stream().map(MajorEntity::getName).collect(Collectors.toList());
        for(String major : majors) {
            major = truncateMajor(major);
            int index = matchMajor(major, majorsInDatabase);
            MajorEntity majorEntity;
            if(index == -1) {
                majorEntity = MajorEntity.builder()
                        .name(major).build();
                majorRepository.save(majorEntity);
            }
            else {
                majorEntity = majorEntities.get(index);
            }
            HighschoolMajorAssociationEntityPK hm_pk = HighschoolMajorAssociationEntityPK.builder()
                    .highschool_id(entity.getSchoolId())
                    .major_name(majorEntity.getName()).build();
            HighschoolMajorAssociationEntity hm = HighschoolMajorAssociationEntity.builder()
                    .highschoolByHighschoolId(entity)
                    .majorByMajorName(majorEntity)
                    .highschoolMajorAssociationEntityPK(hm_pk).build();
            highschoolMajorAssociationRepository.save(hm);
        }

        return entity;
    }

    //TODO: we need to improve this method (use fuzzy search)
    private int matchCollege(String collegeString, List<String> allColleges) {
        for(int i=0; i < allColleges.size(); i++) {
            String collegeI = allColleges.get(i);
            if(collegeI.contains(collegeString) || collegeString.contains(collegeI)) return i;
        }
        return -1;
    }

    private int matchMajor(String major, List<String> majorsInDatabase) {
        for(int i=0; i < majorsInDatabase.size(); i++) {
            String majorI = majorsInDatabase.get(i);
            if(majorI.contains(major) || major.contains(majorI)) return i;
        }
        return -1;
    }

    private String truncateMajor(String major) {
        if(major.length() < 45) return major;
        if(!major.contains(" ")) return major.substring(0,44);
        else return truncateMajor(major.substring(0, major.lastIndexOf(" ")));
    }

}
