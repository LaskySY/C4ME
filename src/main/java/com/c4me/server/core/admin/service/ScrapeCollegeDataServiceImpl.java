package com.c4me.server.core.admin.service;

import com.c4me.server.core.admin.repository.CollegeMajorAssociationRepository;
import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.core.profile.repository.MajorRepository;
import com.c4me.server.entities.CollegeEntity;
import com.c4me.server.entities.CollegeMajorAssociationEntity;
import com.c4me.server.entities.CollegeMajorAssociationEntityPK;
import com.c4me.server.entities.MajorEntity;
import com.c4me.server.core.profile.service.MajorAliasTable;
import com.c4me.server.utils.TestingDataUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


import org.apache.commons.text.similarity.LevenshteinDistance;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.c4me.server.config.constant.Const.Filenames.*;

/**
 * @Description:
 * @Author: Yousef Khan
 * @CreateDate: 03-22-2020
 */

@Service
public class ScrapeCollegeDataServiceImpl {
  @Autowired
  CollegeRepository collegeRepository;
  @Autowired
  MajorRepository majorRepository;
  @Autowired
  CollegeMajorAssociationRepository collegeMajorAssociationRepository;
  LevenshteinDistance distance = new LevenshteinDistance();

  public Document openWebpage(URL url) throws IOException {
    URLConnection conn = url.openConnection();
    conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
    String line = null;
    StringBuilder tmp = new StringBuilder();
    BufferedReader buf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    while ((line = buf.readLine()) != null) {
      tmp.append(line);
    }
    Document doc = Jsoup.parse(tmp.toString());
    return doc;
  }

  private static String preprocess(String collegeName) {
    collegeName = collegeName.replace("-", " ");
    collegeName = collegeName.trim();
    collegeName = collegeName.replaceAll(" +", " ");
    return collegeName;
  }

  private static Integer parseInt (String str) {
    try {
      return Integer.parseInt(str);
    } catch (NumberFormatException e) {return null;}
  }

  //TODO: please break up this method into smaller methods.
  public void scrapeCollegeData() throws IOException {
    List<CollegeEntity> colleges = collegeRepository.findAll();

    File dataFile = TestingDataUtils.findFile(COLLEGEDATATXT);
    List<String> collegesListedOnWebsite = TestingDataUtils.readFile(dataFile);

//    Collection<MajorEntity> majorEntities = majorRepository.findAll();

    for(CollegeEntity c : colleges){
      String name = preprocess(c.getName());

      int shortestDistance = Integer.MAX_VALUE;
      String desiredName = name;

      //get the closest url extension from name we have vs name on collegeData website
      for(String siteCollege: collegesListedOnWebsite){
        int stringDist = distance.apply(name, preprocess(siteCollege));
        if(stringDist < shortestDistance){
          shortestDistance = stringDist;
          desiredName = siteCollege;
        }
      }
      System.out.println("name = " + name + ", desiredName = " + desiredName);

      name = desiredName;

      //now we have the accurate name extension to append to our url

      String webpage = COLLEGE_DATA_PREFIX;
      if(!webpage.endsWith("/")) webpage += "/";
      webpage += name;

      System.out.println(webpage);

      URL collegeurl = new URL(webpage);
      Document document = openWebpage(collegeurl);

      Double netprice = null;
      Integer instateTu = 0;
      Integer outstateTu = 0;
      Double averageGPA = null;
      Integer actComp = 0;
      Integer satMath25 = 0;
      Integer satMath50 = 0;
      Integer satMath75 = 0;
      Integer satEbrw25 = 0;
      Integer satEbrw50 = 0;
      Integer satEbrw75 = 0;

      boolean hasSatMath50 = false;
      boolean hasSatEbrw50 = false;





      Elements cardbody = document.select("div.card-body");

      for(Element e: cardbody){
        //net price and tuition in this box
        if(e.wholeText().contains("Money Matters")){
          Elements dt = e.select("dt");
          for(Element e2: dt){

            // for net price
            if(e2.wholeText().contains("Cost of Attendance")){
              Element dd = e2.nextElementSibling();             //first immediate part after cost of attendance where values are stored
              if(dd == null){
                continue;
              }
              if(dd.wholeText().contains("Not")){
                //if value is listed as "not reported" or "not available"
                continue;
              }
              if(dd.select("br").size() > 0){
                //if we enter this we have both in and out of state numbers
                System.out.println("we have 2 net prices");
                //parse string for both numbers
                String wholeString = dd.wholeText();

                int startIndex = dd.wholeText().indexOf("$");
                wholeString = wholeString.substring(startIndex + 1);

                String inStateStr = "0";
                if(!Character.isDigit(dd.wholeText().charAt(16))){
                  inStateStr = dd.wholeText().substring(startIndex + 1, 16);
                }
                else{
                  inStateStr = dd.wholeText().substring(startIndex + 1, 17);
                }

                startIndex = wholeString.indexOf("$");
                String outStateStr = wholeString.substring(startIndex + 1);

                inStateStr = inStateStr.replace(",", "");
                outStateStr = outStateStr.replace(",", "");

                //take the average of both numbers to get net price
                Double inStr = Double.parseDouble(inStateStr);
                Double outStr = Double.parseDouble(outStateStr);


                netprice = (inStr + outStr)/2;

              }
              else {
                int startIndex = dd.wholeText().indexOf("$");
                String netPriceStr = dd.wholeText().substring(startIndex + 1);
                netPriceStr = netPriceStr.replace(",", "");
                netprice = Double.parseDouble(netPriceStr);
              }
            }



            //for the regular in and out of state tuition
            if(e2.wholeText().contains("Tuition and Fees")){
              Element dd = e2.nextElementSibling();
              if(dd == null){
                continue;
              }
              if(dd.wholeText().contains("Not")){
                //if value is listed as "not reported" or "not available"
                continue;
              }
              if(dd.select("br").size() > 0){
                //if we enter this we have both in and out of state numbers
                System.out.println("we have 2 tuitions");
                //parse string for both numbers
                String wholeString = dd.wholeText();

                int startIndex = dd.wholeText().indexOf("$");
                wholeString = wholeString.substring(startIndex + 1);

                String inStateStr = "0";
                if(!Character.isDigit(dd.wholeText().charAt(16))){
                  inStateStr = dd.wholeText().substring(startIndex + 1, 16);
                }
                else{
                  inStateStr = dd.wholeText().substring(startIndex + 1, 17);
                }


                startIndex = wholeString.indexOf("$");
                String outStateStr = wholeString.substring(startIndex + 1);

                inStateStr = inStateStr.replace(",", "");
                outStateStr = outStateStr.replace(",", "");


                instateTu = parseInt(inStateStr);
                outstateTu = parseInt(outStateStr);


              }
              else {
                //we only have one number
                int startIndex = dd.wholeText().indexOf("$");
                String tuitionStr = dd.wholeText().substring(startIndex + 1);
                tuitionStr = tuitionStr.replace(",", "");

                instateTu = parseInt(tuitionStr);
                outstateTu = parseInt(tuitionStr);
              }
            }



          }
        }


        //box for gpa and sat, act scores
        if(e.wholeText().contains("Admission")){
          Elements dt = e.select("dt");
          for(Element e2: dt){

            //for average gpa
            if(e2.wholeText().contains("Average GPA")){
              Element dd = e2.nextElementSibling();
              if(dd == null){
                continue;
              }
              if(dd.wholeText().contains("Not")){
                //if value is listed as "not reported" or "not available"
                continue;
              }
              String gpa = dd.wholeText();
              averageGPA = Double.parseDouble(gpa);

            }


            //for SAT Math
            if(e2.wholeText().contains("SAT Math")){
              Element dd = e2.nextElementSibling();
              if(dd == null){
                continue;
              }
              if(dd.wholeText().contains("Not")){
                //if value is listed as "not reported" or "not available"
                continue;
              }
              if(dd.select("br").size() > 0){
                //if here then we have data for 50th percentile as well
                String math50 = dd.wholeText().substring(0, 3);
                String math25 = dd.wholeText().substring(12, 15);
                String math75 = dd.wholeText().substring(16, 19);

                //make sure last digit of score is present otherwise trim last char and replace with 0
                //this avoid some urls forgetting the last digit of a score, such as NJIT's URL
                if(!Character.isDigit(math50.charAt(2))){
                  math50 = math50.substring(0, 2);
                  math50 += "0";
                }
                if(!Character.isDigit(math25.charAt(2))){
                  math25 = math25.substring(0, 2);
                  math25 += "0";
                }
                if(!Character.isDigit(math75.charAt(2))){
                  math75 = math75.substring(0, 2);
                  math75 += "0";
                }


                satMath50 = parseInt(math50);
                satMath25 = parseInt(math25);
                satMath75 = parseInt(math75);

                hasSatMath50 = true;

              }
              else{
                // we only have 25 and 75
                String wholeString = dd.wholeText().trim();

                String math25 = wholeString.substring(0, 3);
                String math75 = wholeString.substring(4, 7);

                if(!Character.isDigit(math25.charAt(2))){
                  math25 = math25.substring(0, 2);
                  math25 += "0";
                }
                if(!Character.isDigit(math75.charAt(2))){
                  math75 = math75.substring(0, 2);
                  math75 += "0";
                }

                satMath25 = parseInt(math25);
                satMath75 = parseInt(math75);
              }

            }




            //for SAT EBRW
            if(e2.wholeText().contains("SAT EBRW")){
              Element dd = e2.nextElementSibling();
              if(dd == null){
                continue;
              }
              if(dd.wholeText().contains("Not")){
                //if value is listed as "not reported" or "not available"
                continue;
              }
              if(dd.select("br").size() > 0){
                //if here then we have data for 50th percentile as well
                String ebrw50 = dd.wholeText().substring(0, 3);
                String ebrw25 = dd.wholeText().substring(12, 15);
                String ebrw75 = dd.wholeText().substring(16, 19);

                //make sure last digit of score is present otherwise trim last char and replace with 0
                //this avoid some urls forgetting the last digit of a score, such as NJIT's URL
                if(!Character.isDigit(ebrw50.charAt(2))){
                  ebrw50 = ebrw50.substring(0, 2);
                  ebrw50 += "0";
                }
                if(!Character.isDigit(ebrw25.charAt(2))){
                  ebrw25 = ebrw25.substring(0, 2);
                  ebrw25 += "0";
                }
                if(!Character.isDigit(ebrw75.charAt(2))){
                  ebrw75 = ebrw75.substring(0, 2);
                  ebrw75 += "0";
                }



                satEbrw50 = parseInt(ebrw50);
                satEbrw25 = parseInt(ebrw25);
                satEbrw75 = parseInt(ebrw75);

                hasSatEbrw50 = true;

              }
              else{
                // we only have 25 and 75
                String wholeString = dd.wholeText().trim();

                String ebrw25 = wholeString.substring(0, 3);
                String ebrw75 = wholeString.substring(4, 7);

                if(!Character.isDigit(ebrw25.charAt(2))){
                  ebrw25 = ebrw25.substring(0, 2);
                  ebrw25 += "0";
                }
                if(!Character.isDigit(ebrw75.charAt(2))){
                  ebrw75 = ebrw75.substring(0, 2);
                  ebrw75 += "0";
                }

                satEbrw25 = parseInt(ebrw25);
                satEbrw75 = parseInt(ebrw75);
              }

            }




            //for ACT Composite
            if(e2.wholeText().contains("ACT Composite")){
              Element dd = e2.nextElementSibling();
              if(dd == null){
                continue;
              }
              if(dd.wholeText().contains("Not")){
                //if value is listed as "not reported" or "not available"
                continue;
              }
                //since actComp is only 1 field we only care about 1 score
                String actStr = dd.wholeText().substring(0, 2);

                actComp = parseInt(actStr);

            }



          }


        }

        //get the majors at this college
        if(e.wholeText().contains("Undergraduate Majors")){
//          System.out.println("UG EDU is there");

          Collection<CollegeMajorAssociationEntity> cmaList = c.getCollegeMajorAssociationsById();
          Collection<CollegeMajorAssociationEntity> newCollegeMajorAssociations = new ArrayList<>();

          Elements list = e.select("li");
          for(Element e2: list){
            if(e2 != null) {
              if(e2.wholeText().contains("Not")){
                continue;
              }
              else {
//                System.out.println("Major is " + e2.wholeText());
                String majorName = e2.wholeText().trim();
                MajorEntity majEnt = getMajorEntityIfExists(majorName);
                if(majEnt == null) continue;

                CollegeMajorAssociationEntityPK collegeMajorAssociationEntityPK = CollegeMajorAssociationEntityPK.builder()
                        .college_id(c.getId())
                        .major_name(majEnt.getName())
                        .build();
                CollegeMajorAssociationEntity collegeMajorAssociationEntity = CollegeMajorAssociationEntity.builder()
                        .collegeByCollegeId(c)
                        .majorByMajorName(majEnt)
                        .collegeMajorAssociationEntityPK(collegeMajorAssociationEntityPK)
                        .build();
                //collegeMajorAssociationRepository.save(collegeMajorAssociationEntity);
                if(!cmaList.contains(collegeMajorAssociationEntity)) {
                  newCollegeMajorAssociations.add(collegeMajorAssociationEntity);
                };
              }

            }

          }
          if(newCollegeMajorAssociations.size() > 0) {
            collegeMajorAssociationRepository.saveAll(newCollegeMajorAssociations);
            cmaList.addAll(newCollegeMajorAssociations);
          }

        }


      }

      //only set values in database if they are nonzero/non-null
      if(netprice != null){
        c.setNetPrice(netprice);
      }
      if(instateTu != null && instateTu != 0){
        c.setInstateTuition(instateTu);
      }
      if(outstateTu != null && outstateTu != 0){
        c.setOutstateTuition(outstateTu);
      }
      if(averageGPA != null){
        c.setAverageGpa(averageGPA);
      }
      if(satMath25 != null && satMath25 != 0){
        c.setSatMath25(satMath25);
      }

      if(satMath50 != null && satMath50 != 0) {
        if (hasSatMath50) {
          c.setSatMath50(satMath50);
        }
      }

      if(satMath75 != null && satMath75 != 0){
        c.setSatMath75(satMath75);
      }
      if(satEbrw25 != null && satEbrw25 != 0){
        c.setSatEbrw25(satEbrw25);
      }

      if(satEbrw50 != null && satEbrw50 != 0) {
        if (hasSatEbrw50) {
          c.setSatEbrw50(satEbrw50);
        }
      }

      if(satEbrw75 != null && satEbrw75 != 0){
        c.setSatEbrw75(satEbrw75);
      }

      if(actComp != null && actComp != 0){
        c.setActComposite(actComp);
      }


      collegeRepository.save(c);

//      Collection<CollegeMajorAssociationEntity> myEnts = c.getCollegeMajorAssociationsById();
//      break;


    }



  }

  private MajorEntity getMajorEntityIfExists(String major) {
    MajorAliasTable majorAliasTable = new MajorAliasTable();
    return majorAliasTable.parseMajorName(major);
//    MajorEntity majorEntity;
//    if(major == null) {
//      majorEntity = null;
//    }
//    else {
//      majorEntity = MajorEntity.builder().name(major).build();
//      if(!(majorEntities.contains(majorEntity))) majorRepository.save(majorEntity);
//    }
//    return majorEntity;
  }

  private String truncateMajor(String major) {
    if(major.length() < 45) return major;
    if(!major.contains(" ")) return major.substring(0,44);
    else return truncateMajor(major.substring(0, major.lastIndexOf(" ")));
  }



}
