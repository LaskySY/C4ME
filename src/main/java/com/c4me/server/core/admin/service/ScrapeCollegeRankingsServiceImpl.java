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
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-20-2020
 */

@Service
public class ScrapeCollegeRankingsServiceImpl {

    @Autowired
    CollegeRepository collegeRepository;

    public HashMap<String, Integer> readJsonRankings(File rankingsFile) throws FileNotFoundException {
        FileInputStream in = new FileInputStream(rankingsFile);
        BufferedInputStream buf = new BufferedInputStream(in);

        Gson gson = new Gson();
        FileReader reader = new FileReader(rankingsFile);
        //Object jsonObj = gson.fromJson(reader, Object.class);

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


    public void scrapeRankings() throws IOException {
        URL rankingsUrl = new URL(THE_RANKINGS_JSON_URL);
        File tmpJson = File.createTempFile("jsonRankings", "json");
//        ReadableByteChannel channel = Channels.newChannel(rankingsUrl.openStream());
        FileUtils.copyURLToFile(rankingsUrl, tmpJson);

        HashMap<String, Integer> rankingsMap = readJsonRankings(tmpJson);
        tmpJson.delete();


        List<CollegeEntity> collegeEntities = collegeRepository.findAll();
        for(CollegeEntity entity : collegeEntities) {
            String name = entity.getName();
            System.out.println(name);
            String processedName = name.replaceAll("[^A-Za-z ]", " ").replaceAll(" +", " ").trim();
            int max = -1;
//            int min = Integer.MAX_VALUE;
            String bestMatch = null;
//            String bestMatch2 = null;
            FuzzyScore fs = new FuzzyScore(Locale.US);
//            LevenshteinDistance ldist = new LevenshteinDistance();
            for(String s : rankingsMap.keySet()) {
                String processedS = s.replaceAll("[^A-Za-z ]", " ").replaceAll(" +", " ").trim();
                if(processedS.equals(processedName)) {
                    bestMatch = s;
                    break;
                }
                int dist = fs.fuzzyScore(processedName, processedS);
//                int dist2 = ldist.apply(processedName, processedS);
//                if(processedName.equals("University of Houston")) System.out.println("ldist between " + processedName + " and " + processedS + " is " + dist2);
                if(dist > max) {
                    max = dist;
                    bestMatch = s;
                }
//                if (dist2 < min) {
//                    min = dist2;
//                    bestMatch2 = s;
//                }
            }
            System.out.println(bestMatch);
//            System.out.println("leven = " + bestMatch2);
            System.out.println(rankingsMap.get(bestMatch));
            entity.setRanking(rankingsMap.get(bestMatch));
            collegeRepository.save(entity);
            //rankingsMap.remove(bestMatch);
        }
    }



}
