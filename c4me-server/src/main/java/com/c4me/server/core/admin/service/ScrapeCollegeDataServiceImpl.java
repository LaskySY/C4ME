package com.c4me.server.core.admin.service;

import com.c4me.server.config.exception.NoCollegeTxtException;
import com.c4me.server.core.profile.repository.CollegeRepository;
import com.c4me.server.entities.CollegeEntity;
import com.c4me.server.utils.TestingDataUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.text.similarity.LevenshteinDistance;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.c4me.server.config.constant.Const.Filenames.*;
import static com.c4me.server.utils.TestingDataUtils.*;

/**
 * @Description:
 * @Author: Yousef Khan
 * @CreateDate: 03-22-2020
 */

@Service
public class ScrapeCollegeDataServiceImpl {
  @Autowired
  CollegeRepository collegeRepository;

  LevenshteinDistance distance = new LevenshteinDistance();

  TestingDataUtils td;





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


  public List<String> readCollegeDataTxt(String filename) throws IOException {
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



  public void scrapeCollegeData() throws IOException {
    List<CollegeEntity> colleges = collegeRepository.findAll();

    td.findFile(COLLEGEDATATXT);
    File dataFile = td.findFile(COLLEGEDATATXT);
    String path = dataFile.getAbsolutePath();
    List<String> collegesListedOnWebsite = readCollegeDataTxt(path);


    for(CollegeEntity c : colleges){
      int shortestDistance = 10000;
      String name = c.getName();
      name = name.replace("-", " ");
      name = name.trim();
      name = name.replaceAll(" +"," ");

      String desiredName = name;

      //get the closest url extension from name we have vs name on collegeData website
      for(String siteCollege: collegesListedOnWebsite){
        siteCollege = siteCollege.replace("-", " ");
        siteCollege = siteCollege.trim();
        siteCollege = siteCollege.replaceAll(" +"," ");
        int stringDist = distance.apply(name, siteCollege);
        if(stringDist < shortestDistance){
          shortestDistance = stringDist;
          desiredName = siteCollege;
        }
      }

      name = desiredName;

      //now we have the accurate name extension to append to our url

      String webpage = "https://www.collegedata.com/college/";
      name = name.replace(" ", "-");
      webpage += name;
//      webpage += name.replace(" ", "-");

      System.out.println(name);

      URL collegeurl = new URL(webpage);
      Document document = openWebpage(collegeurl);

      Double netprice = null;
      int instateTu = 0;
      int outstateTu = 0;
      Double averageGPA = null;
      int actComp = 0;
      int satMath25 = 0;
      int satMath50 = 0;
      int satMath75 = 0;
      int satEbrw25 = 0;
      int satEbrw50 = 0;
      int satEbrw75 = 0;

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


                instateTu = Integer.parseInt(inStateStr);
                outstateTu = Integer.parseInt(outStateStr);


              }
              else {
                //we only have one number
                int startIndex = dd.wholeText().indexOf("$");
                String tuitionStr = dd.wholeText().substring(startIndex + 1);
                tuitionStr = tuitionStr.replace(",", "");

                instateTu = Integer.parseInt(tuitionStr);
                outstateTu = Integer.parseInt(tuitionStr);
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


                satMath50 = Integer.parseInt(math50);
                satMath25 = Integer.parseInt(math25);
                satMath75 = Integer.parseInt(math75);

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

                satMath25 = Integer.parseInt(math25);
                satMath75 = Integer.parseInt(math75);
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



                satEbrw50 = Integer.parseInt(ebrw50);
                satEbrw25 = Integer.parseInt(ebrw25);
                satEbrw75 = Integer.parseInt(ebrw75);

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

                satEbrw25 = Integer.parseInt(ebrw25);
                satEbrw75 = Integer.parseInt(ebrw75);
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

                actComp = Integer.parseInt(actStr);

            }



          }


        }




      }

      //only set values in database if they are nonzero/non-null
      if(netprice != null){
        c.setNetPrice(netprice);
      }
      if(instateTu != 0){
        c.setInstateTuition(instateTu);
      }
      if(outstateTu != 0){
        c.setOutstateTuition(outstateTu);
      }
      if(averageGPA != null){
        c.setAverageGpa(averageGPA);
      }
      if(satMath25 != 0){
        c.setSatMath25(satMath25);
      }

      if(satMath50 != 0) {
        if (hasSatMath50) {
          c.setSatMath50(satMath50);
        }
      }

      if(satMath75 != 0){
        c.setSatMath75(satMath75);
      }
      if(satEbrw25 != 0){
        c.setSatEbrw25(satEbrw25);
      }

      if(satEbrw50 != 0) {
        if (hasSatEbrw50) {
          c.setSatEbrw50(satEbrw50);
        }
      }

      if(satEbrw75 != 0){
        c.setSatEbrw75(satEbrw75);
      }

      if(actComp != 0){
        c.setActComposite(actComp);
      }


      collegeRepository.save(c);

//      break;



    }



  }



}
