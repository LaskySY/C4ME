package com.c4me.server.core.admin.service;

import com.c4me.server.config.exception.NoCollegeTxtException;
import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.entities.CollegeEntity;
import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

        //TODO: Should we read colleges.txt to filter?
//        File topDir = new File(System.getProperty("user.dir"));
//        Iterator<File> files = FileUtils.iterateFiles(topDir, new String[] {"txt"}, true);
//        String collegesFilename = "";
//
//        while(files.hasNext()) {
//            File f = files.next();
//            String extension = FilenameUtils.getExtension(f.getAbsolutePath());
//            if (f.getName().equals(COLLEGES)) {
//                collegesFilename = f.getAbsolutePath();
//                break;
//            }
//        }
//        if(collegesFilename.equals("")) throw new NoCollegeTxtException("college.txt not found");
//        List<String> colleges = collegeScorecardService.readCollegesTxt(collegesFilename);

        List<CollegeEntity> collegeEntities = collegeRepository.findAll();
        for(String s : rankingsMap.keySet()) {
            System.out.println("key = " + s + ", val = " + rankingsMap.get(s));
            for(CollegeEntity entity : collegeEntities) {
                if(entity.getName().equals(s)) {       //TODO: need to implement college aliases --> find by alias
                    entity.setRanking(rankingsMap.get(s));
                    entity.setUpdatedTime(Timestamp.from(Instant.now()));
                    collegeRepository.save(entity);
                    break;
                }
            }
//            CollegeEntity collegeEntity = collegeRepository.findByName(s);
//            if(collegeEntity == null) continue;
//            else {
//                collegeEntity.setRanking(rankingsMap.get(s));
//                collegeRepository.save(collegeEntity);
//            }
        }

    }



}
