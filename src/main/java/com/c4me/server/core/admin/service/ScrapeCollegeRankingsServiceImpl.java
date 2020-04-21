package com.c4me.server.core.admin.service;

import com.c4me.server.config.exception.NoCollegeTxtException;
import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.entities.CollegeEntity;
import com.google.gson.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.text.similarity.FuzzyScore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.*;

import static com.c4me.server.config.constant.Const.Filenames.*;

/**
 * @Description: Implementation of the scrapeCollegeRankings service
 * @Author: Maciej Wlodek
 * @CreateDate: 03-20-2020
 */

@Service
public class ScrapeCollegeRankingsServiceImpl {

    @Autowired
    CollegeRepository collegeRepository;

    /**
     * Get a map of names->rankings reported by TimesHigherEducation
     * @param rankingsFile the TimesHigherEducation rankings.json file
     * @return {@link HashMap} containing ({@literal Name}, {@literal Ranking}) for each college
     * @throws FileNotFoundException
     */
    private HashMap<String, Integer> readJsonRankings(File rankingsFile) throws FileNotFoundException {
        FileReader reader = new FileReader(rankingsFile);

        JsonObject rankingsObj = JsonParser.parseReader(reader).getAsJsonObject();
        JsonArray rankingsArray = rankingsObj.getAsJsonArray("data");

        HashMap<String, Integer> rankingsMap = new HashMap<>();
        for(JsonElement elem : rankingsArray) {
            JsonObject obj = elem.getAsJsonObject();
            JsonPrimitive prim = obj.getAsJsonPrimitive("rank_order");
            Integer rank = Integer.parseInt(prim.getAsString());
            JsonPrimitive name_prim = obj.getAsJsonPrimitive("name");
            String name = name_prim.getAsString();
            rankingsMap.put(name, rank);
        }

        return rankingsMap;
    }


    /**
     * Scrape the TimesHigherEducation rankings and save them to the database
     * @throws IOException
     */
    public void scrapeRankings() throws IOException {
        URL rankingsUrl = new URL(THE_RANKINGS_JSON_URL);
        File tmpJson = File.createTempFile("jsonRankings", "json");
        FileUtils.copyURLToFile(rankingsUrl, tmpJson);

        HashMap<String, Integer> rankingsMap = readJsonRankings(tmpJson);
        tmpJson.delete();


        List<CollegeEntity> collegeEntities = collegeRepository.findAll();
        for(CollegeEntity entity : collegeEntities) {
            String name = entity.getName();
            System.out.println(name);
            String processedName = name.replaceAll("[^A-Za-z ]", " ").replaceAll(" +", " ").trim();
            int max = -1;
            String bestMatch = null;
            FuzzyScore fs = new FuzzyScore(Locale.US);
            for(String s : rankingsMap.keySet()) {
                String processedS = s.replaceAll("[^A-Za-z ]", " ").replaceAll(" +", " ").trim();
                if(processedS.equals(processedName)) {
                    bestMatch = s;
                    break;
                }
                int dist = fs.fuzzyScore(processedName, processedS);
                if(dist > max) {
                    max = dist;
                    bestMatch = s;
                }
            }
            System.out.println(bestMatch);
            System.out.println(rankingsMap.get(bestMatch));
            entity.setRanking(rankingsMap.get(bestMatch));
            collegeRepository.save(entity);
        }
    }



}
