package com.c4me.server.core.collegeSearch.domain;

import lombok.*;

import java.util.List;
import java.util.Set;

/**
 * @Description:
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

    private Integer minNumberOfStudents;
    private Integer maxNumberOfStudents;

    private Integer minSatMath;
    private Integer maxSatMath;

    private Integer minSatEbrw;
    private Integer maxSatEbrw;

    private Integer minActComposite;
    private Integer maxActComposite;

    private String sortBy;

    private Boolean strict;
}
