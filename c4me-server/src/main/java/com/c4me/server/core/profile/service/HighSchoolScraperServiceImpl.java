package com.c4me.server.core.profile.service;

import com.c4me.server.config.exception.HighSchoolDoesNotExistException;
import com.c4me.server.core.credential.repository.HighschoolRepository;
import com.c4me.server.core.profile.repository.CollegeHighSchoolAssociationRepository;
import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.entities.CollegeEntity;
import com.c4me.server.entities.CollegeHighschoolAssociationEntity;
import com.c4me.server.entities.CollegeHighschoolAssociationEntityPK;
import com.c4me.server.entities.HighschoolEntity;
import com.c4me.server.utils.SearchHSUtils;
import com.c4me.server.utils.TestingDataUtils;
import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.c4me.server.config.constant.Const.Filenames.*;
import static com.c4me.server.config.constant.Const.STATES.STATES_LIST;
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

    public boolean scrapeBaseSite(String webpage, HighschoolEntity highschoolEntity) throws IOException {
        URL url = new URL(webpage);
        Document nicheBaseDoc = openWebpage(url);
        //Document nicheBaseDoc = openWebpage("base_page_test.html");

        //TODO: select for class li.search-tags__wrap__list__tag and check that High School tag exists (otherwise we've accidentally found a middle or elementary school)

        Element nameHeader = nicheBaseDoc.selectFirst("h1.postcard__title");
        if(nameHeader == null) return false;
        highschoolEntity.setName(nameHeader.ownText());


        Element attributesElement = nicheBaseDoc.selectFirst("ul.postcard__attrs");
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
        List<String> popularColleges = new ArrayList<>();
        Elements colleges = collegesElement.select("h6.popular-entity__name");
        for(Element college : colleges) {
            popularColleges.add(college.text());
            System.out.println("college = " + college.text());
        }
        return popularColleges;
    }
    public List<String> getPopularMajors(Element majorsElement) {
        List<String> popularMajors = new ArrayList<>();
        Elements majors = majorsElement.select("h6.popular-entity__name");
        for(Element major : majors) {
            popularMajors.add(major.text());
            System.out.println("major = " + major.text());
        }
        return popularMajors;
    }

    public void getAllScores(Element scoresElement, HighschoolEntity highschoolEntity) {
        Elements scalars = scoresElement.select("div.scalar__label");
        for(Element scalar : scalars) {
            for(Element c : scalar.children()) {
                if(c.ownText().contains("SAT")) {
                    Integer score = getScore(scalar);
                    if(!(score == -1 || score < 400 || score > 1600)) {
                        highschoolEntity.setSatOverall(score);
                    }
                }
                else if (c.ownText().contains("ACT")) {
                    Integer score = getScore(scalar);
                    if(!(score == -1 || score < 1 || score > 36)) {
                        highschoolEntity.setActComposite(score);
                    }
                }
                else if (c.ownText().contains("Math")) {
                    Integer score = getScore(scalar);
                    if(!(score == -1 || score < 400 || score > 1600)) {
                        highschoolEntity.setSatMath(score);
                    }
                    else if (!(score == -1 || score < 1 || score > 36)) {
                        highschoolEntity.setActMath(score);
                    }
                }
                else if (c.ownText().contains("Verbal")) {
                    Integer score = getScore(scalar);
                    if(!(score == -1 || score < 400 || score > 1600)) {
                        highschoolEntity.setSatEbrw(score);
                    }
                }
                else if (c.ownText().contains("English")) {
                    Integer score = getScore(scalar);
                    if (!(score == -1 || score < 1 || score > 36)) {
                        highschoolEntity.setActEnglish(score);
                    }
                }
                else if (c.ownText().contains("Reading")) {
                    Integer score = getScore(scalar);
                    if (!(score == -1 || score < 1 || score > 36)) {
                        highschoolEntity.setActReading(score);
                    }
                }
                else if (c.ownText().contains("Science")) {
                    Integer score = getScore(scalar);
                    if (!(score == -1 || score < 1 || score > 36)) {
                        highschoolEntity.setActScience(score);
                    }
                }
            }
        }
    }

    public Integer getScore(Element scalarLabel) {
        if(scalarLabel.siblingElements().size() == 0) return -1;
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
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0"); //TODO: do we need to cycle user-agents?
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

    public List<String> findMatches(String query) throws IOException {
        HashMap<String, Integer> matches =  SearchHSUtils.searchForNicheUrl(query);
        return matches.keySet().stream().collect(Collectors.toList());
    }

    public String buildUrl(String match) {
        String url = NICHE_PREFIX + match;
        if(!url.endsWith("/")) url += "/";
        return url;
    }
    public HighschoolEntity scrapeHighSchool(String query) throws IOException, HighSchoolDoesNotExistException {
        System.out.println("trying to find niche url");
        HashMap<String, Integer> matches =  SearchHSUtils.searchForNicheUrl(query);
        String bestMatch = getBestMatch(matches);
        System.out.println("best match = " + bestMatch);

        if(bestMatch == null) throw new HighSchoolDoesNotExistException("could not find high school");

        System.out.println("trying to scrape niche.com");
        HighschoolEntity entity = new HighschoolEntity();
        String url = buildUrl(bestMatch);
        scrapeBaseSite(url, entity);
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

        //TODO: save high school major associations as well

        return entity;
    }

    //TODO: we need to improve this method (maybe use college aliases etc.)
    private int matchCollege(String collegeString, List<String> allColleges) {
        for(int i=0; i < allColleges.size(); i++) {
            String collegeI = allColleges.get(i);
            if(collegeI.contains(collegeString) || collegeString.contains(collegeI)) return i;
        }
        return -1;
    }


}
