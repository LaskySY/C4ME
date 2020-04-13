package com.c4me.server.core.collegeSearch.domain;

import com.c4me.server.entities.CollegeEntity_;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static com.c4me.server.config.constant.Const.States.*;

/**
 * @Description: IMPORTANT: each supported range filter name should be min___ and max___, with the appropriate CollegeEntity property name (except for special filters)
 * @Author: Maciej Wlodek
 * @CreateDate: 04-06-2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true, builderClassName = "CollegeSearchFilterBuilder", buildMethodName = "internalBuild")
@ToString
public class CollegeSearchFilter {
    private String name;

    private Double minAdmissionRate;
    private Double maxAdmissionRate;

    private Integer minCostOfAttendance;
    private Integer maxCostOfAttendance;

    private List<String> regions;
    private List<String> states;

//    private String major1;
//    private String major2;
    private String[] major;

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

//    @Builder.Default
    private Boolean ascending = true;

//    @Builder.Default
    private Boolean strict = false;

    public void setRegions(List<String> regions) {
        this.regions = regions;
        loadStatesFromRegions();
    }
    public void loadStatesFromRegions() {
        if(states == null) {
            if (regions == null || regions.size() == 0) {
                states = STATES_LIST;
            } else {
                states = new ArrayList<>();
                for (String region : regions) {
                    states.addAll(REGIONS_MAP.get(region));
                }
            }
        }
        if(getAscending() == null) setAscending(true);
        if(getSortBy() == null || getSortBy().length() == 0) setSortBy(CollegeEntity_.RANKING);
        if(getStrict() == null) setStrict(false);
    }

    public static class CollegeSearchFilterBuilder{
        public CollegeSearchFilter build() {
            CollegeSearchFilter filter = internalBuild();
            filter.loadStatesFromRegions();
            return filter;
        }
    }

}
