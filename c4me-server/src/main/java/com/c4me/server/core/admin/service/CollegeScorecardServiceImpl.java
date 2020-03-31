package com.c4me.server.core.admin.service;

import com.c4me.server.config.exception.InvalidCollegeScorecardException;
import com.c4me.server.config.exception.NoCollegeScorecardException;
import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.entities.CollegeEntity;
//import org.apache.commons.beanutils.BeanUtils;
//import org.apache.commons.beanutils.BeanUtilsBean;
import com.c4me.server.utils.CopyUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static com.c4me.server.config.constant.Const.CollegeScorecardHeaders.*;
import static com.c4me.server.config.constant.Const.Types.*;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-20-2020
 */

@Service
public class CollegeScorecardServiceImpl {

    @Autowired
    CollegeRepository collegeRepository;


    public List<String> readCollegesTxt(String filename) throws IOException {
        Reader in = new FileReader(filename);
        BufferedReader buf = new BufferedReader(in);
        List<String> colleges = new ArrayList<>();
        String line = "";
        while((line = buf.readLine()) != null) {
            colleges.add(line);
        }
        buf.close();
        in.close();
        return colleges;
    }

    public Integer parseInt(String s) {
        try { return Integer.parseInt(s); }
        catch (NumberFormatException e) { return null; }
    }
    public Double parseDouble(String s) {
        try { return Double.parseDouble(s); }
        catch (NumberFormatException e) { return null; }
    }
    public String parseType(String s) {
        Integer typeI = parseInt(s);
        if(typeI == null || !(TYPES_MAP.containsKey(typeI))) return null;
        else return TYPES_MAP.get(typeI);
    }
    public String[] parseAlias(String s) {
        if(s.equals("NULL")) return new String[0];
        String[] aliases = s.split("\\|"); //TODO: remove empty aliases
        for(int i=0; i<aliases.length; i++) {
            aliases[i] = aliases[i].trim();
        }
        return aliases;
    }

    public CollegeEntity recordToEntity(CSVRecord record) { //TODO: should only check header for the first record
        Map<String, String> recordMap = record.toMap();
//        System.out.println(recordMap.toString());
//        System.out.println(recordMap.keySet().toString());
        Set<String> keysWeCareAbout = new HashSet<String>(Arrays.asList(HEADERS));
        if(!recordMap.keySet().containsAll(keysWeCareAbout)) {
            System.out.println("missing headers -- throw an error");
            for(String key : keysWeCareAbout) {
                if(!recordMap.containsKey(key)) {
                    System.out.println("missing header key " + key);
                }
            }
            return null;
        }
        else {
            return CollegeEntity.builder()
                    .id(parseInt(record.get(ID)))
                    .name(record.get(NAME))
                    .type(parseType(record.get(TYPE)))
                    .admissionRate(parseDouble(record.get(ADM_RATE)))
                    .city(record.get(CITY))
                    .state(record.get(STATE))
                    .webpage(record.get(WEBPAGE))
                    .latitude(parseDouble(record.get(LATITUDE)))
                    .longitude(parseDouble(record.get(LONGITUDE)))
                    .instateTuition(parseInt(record.get(IN_STATE_TUITION)))
                    .outstateTuition(parseInt(record.get(OUT_STATE_TUITION)))
                    .medianDebt(parseDouble(record.get(MEDIAN_DEBT)))
                    .numStudentsEnrolled(parseInt(record.get(NUM_STUDENTS_ENROLLED)))
                    .retentionRate(parseDouble(record.get(RETENTION_RATE)))
                    .completionRate(parseDouble(record.get(COMPLETION_RATE)))
                    .meanEarnings(parseDouble(record.get(MEAN_EARNINGS)))
                    .updatedTime(Timestamp.from(Instant.now()))
                    .satMath25(parseInt(record.get(SATM25)))
                    .satMath50(parseInt(record.get(SATM50)))
                    .satMath75(parseInt(record.get(SATM75)))
                    .satEbrw25(parseInt(record.get(SATEBRW25)))
                    .satEbrw50(parseInt(record.get(SATEBRW50)))
                    .satEbrw75(parseInt(record.get(SATEBRW75)))
                    .satOverall(parseInt(record.get(SAT_OVERALL)))
                    .actMath25(parseInt(record.get(ACTM25)))
                    .actMath50(parseInt(record.get(ACTM50)))
                    .actMath75(parseInt(record.get(ACTM75)))
                    .actEnglish25(parseInt(record.get(ACTE25)))
                    .actEnglish50(parseInt(record.get(ACTE50)))
                    .actEnglish75(parseInt(record.get(ACTE75)))
                    .actComposite(parseInt(record.get(ACT_OVERALL)))
                    .build();
        }
    }

    public void importCsv(String filename, List<String> colleges) throws NoCollegeScorecardException, IOException, InvalidCollegeScorecardException {
        if(filename.equals("")) throw new NoCollegeScorecardException("college scorecard file not found");

        Reader in = new FileReader(filename);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);

        List<CollegeEntity> currentEntities = collegeRepository.findAll();

        int counter = 0;
        int saveCounter = 0;
        for (CSVRecord record : records) { //TODO: transpose the iteration -- right now we can potentially get more than one record per college (aliases might not be unique)
            String name = record.get(NAME);
            String[] aliases = parseAlias(record.get(ALIAS));
            List<String> aliasesList = Arrays.asList(aliases); //TODO: shouldn't we save these in the database?

            if(colleges.contains(name) || aliasesList.stream().anyMatch(s -> colleges.contains(s))) {
                //System.out.println("name is in colleges");
                CollegeEntity collegeEntity = recordToEntity(record);
                if(collegeEntity == null) throw new InvalidCollegeScorecardException("invalid college scorecard file");
                else {
                    boolean found = false;
                    for(CollegeEntity ce: currentEntities) {
                        if(ce.getName().equals(name)) { //TODO: check alias as well
                            BeanUtils.copyProperties(collegeEntity, ce, CopyUtils.getNullPropertyNames(collegeEntity));
//                            try {
//                                System.out.println(name + "was found - attempting to copy properties");
//                                System.out.println("new ranking = " + collegeEntity.getRanking());
//                                System.out.println("old ranking = " + ce.getRanking());
//                                BeanUtilsBean.getInstance().getConvertUtils().register(false, false, 0);
//                                BeanUtils.copyProperties(ce, collegeEntity);
//                                System.out.println("it shouldn't have updated old ranking, but it is now " + ce.getRanking());
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            System.out.println("and now? : " + ce.getRanking());
                            collegeRepository.save(ce);
                            found = true;
                            break;
                        }
                    }
                    if(!found) {
                        System.out.println(name + " not found");
                        collegeRepository.save(collegeEntity);
                    }
                }
                saveCounter++;
            }
            counter++;
        }
//        System.out.println(counter + " records read");
//        System.out.println(saveCounter + " colleges saved to database");
        in.close();
    }




}
