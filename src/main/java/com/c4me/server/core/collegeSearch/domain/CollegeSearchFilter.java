package com.c4me.server.core.collegeSearch.domain;

import lombok.*;

import java.util.List;
import java.util.Set;

/**
 * @Description: IMPORTANT: each supported range filter name should be min___ and max___, with the appropriate CollegeEntity property name (except for special filters)
 * @Author: Maciej Wlodek
 * @CreateDate: 04-06-2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CollegeSearchFilter {
    private String substring;

    private Double minAdmissionRate;
    private Double maxAdmissionRate;

    private Integer minCostOfAttendance;
    private Integer maxCostOfAttendance;

    private List<String> regions; //TODO: are we using regions or states?
    private List<String> states;

    private String major1;
    private String major2;

    private Integer minRanking;
    private Integer maxRanking;

    private Integer minNumStudentsEnrolled;
    private Integer maxNumStudentsEnrolled;

    private Integer minSatMath50;
    private Integer maxSatMath50;

    private Integer minSatEbrw50;
    private Integer maxSatEbrw50;

    private Integer minActComposite;
    private Integer maxActComposite;

    private String sortBy;

    private Boolean strict;
}
