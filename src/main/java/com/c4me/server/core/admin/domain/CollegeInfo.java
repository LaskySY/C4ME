package com.c4me.server.core.admin.domain;

import com.c4me.server.config.constant.Const;
import com.c4me.server.entities.CollegeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.stream.Collectors;

import static com.c4me.server.config.constant.Const.Types.TYPES_MAP;

/**
 * @Description: Domain object representing a college that can be sent to frontend
 * @Author: Maciej Wlodek
 * @CreateDate: 03-20-2020
 */

@Setter
@Getter
@NoArgsConstructor
public class CollegeInfo {

    private String name;
    private Integer type;
    private Double averageGPA;
    private Double admissionRate;
    private String city;
    private String state;
    private String webpage;
    private Double latitude;
    private Double longitude;
    private Integer instateTuition;
    private Integer outstateTuition;
    private Double netPrice;
    private Double medianDebt;
    private Integer numStudentsEnrolled;
    private Double retentionRate;
    private Double completionRate;
    private Double meanEarnings;
    private String updatedTime;
    private Integer ranking;
    private Integer satMath25;
    private Integer satMath50;
    private Integer satMath75;
    private Integer satEbrw25;
    private Integer satEbrw50;
    private Integer satEbrw75;
    private Integer satOverall;
    private Integer actMath25;
    private Integer actMath50;
    private Integer actMath75;
    private Integer actEnglish25;
    private Integer actEnglish50;
    private Integer actEnglish75;
    private Integer actReading25;
    private Integer actReading50;
    private Integer actReading75;
    private Integer actScience25;
    private Integer actScience50;
    private Integer actScience75;
    private Integer actComposite;

    private String typeString;

    private Integer costOfAttendance;

    private Double recommendationScore;

    /**
     * Constructor for CollegeInfo from CollegeEntity
     * @param collegeEntity {@link CollegeEntity} database object
     */
    public CollegeInfo(CollegeEntity collegeEntity) {
        name = collegeEntity.getName();
        type = TYPES_MAP.entrySet().stream().filter(e -> e.getValue().equals(collegeEntity.getType())).collect(Collectors.toList()).get(0).getKey();
        averageGPA = collegeEntity.getAverageGpa();
        admissionRate = collegeEntity.getAdmissionRate();
        city = collegeEntity.getCity();
        state = collegeEntity.getState();
        webpage = collegeEntity.getWebpage();
        latitude = collegeEntity.getLatitude();
        longitude = collegeEntity.getLongitude();
        instateTuition = collegeEntity.getInstateTuition();
        outstateTuition = collegeEntity.getOutstateTuition();
        netPrice = collegeEntity.getNetPrice();
        medianDebt = collegeEntity.getMedianDebt();
        numStudentsEnrolled = collegeEntity.getNumStudentsEnrolled();
        retentionRate = collegeEntity.getRetentionRate();
        completionRate = collegeEntity.getCompletionRate();
        meanEarnings = collegeEntity.getMeanEarnings();
        updatedTime = collegeEntity.getUpdateTime().toString();
        ranking = collegeEntity.getRanking();
        satMath25 = collegeEntity.getSatMath25();
        satMath50 = collegeEntity.getSatMath50();
        satMath75 = collegeEntity.getSatMath75();
        satEbrw25 = collegeEntity.getSatEbrw25();
        satEbrw50 = collegeEntity.getSatEbrw50();
        satEbrw75 = collegeEntity.getSatEbrw75();
        satOverall = collegeEntity.getSatOverall();
        actMath25 = collegeEntity.getActMath25();
        actMath50 = collegeEntity.getActMath50();
        actMath75 = collegeEntity.getActMath75();
        actEnglish25 = collegeEntity.getActEnglish25();
        actEnglish50 = collegeEntity.getActEnglish50();
        actEnglish75 = collegeEntity.getActEnglish75();
        actReading25 = collegeEntity.getActReading25();
        actReading50 = collegeEntity.getActReading50();
        actReading75 = collegeEntity.getActReading75();
        actScience25 = collegeEntity.getActScience25();
        actScience50 = collegeEntity.getActScience50();
        actScience75 = collegeEntity.getActScience75();
        actComposite = collegeEntity.getActComposite();

        typeString = TYPES_MAP.get(type);
    }

    /**
     * Constructor for CollegeInfo to compute the costOfAttendance according to a user's home state
     * @param collegeEntity {@link CollegeEntity} database object
     * @param homeState user's home state to compute instate or outstate cost of attendance
     */
    public CollegeInfo(CollegeEntity collegeEntity, String homeState) {
        this(collegeEntity);
        this.costOfAttendance = (homeState.equals(state))? instateTuition : outstateTuition;
    }



}
