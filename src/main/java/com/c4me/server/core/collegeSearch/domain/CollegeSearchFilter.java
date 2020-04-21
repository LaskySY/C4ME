package com.c4me.server.core.collegeSearch.domain;

import com.c4me.server.entities.CollegeEntity_;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private Set<String> states;
    private Set<String> region;

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

    public void setRegion(Set<String> region) {
        this.region = region;
        loadStatesFromRegions();
    }
    public void loadStatesFromRegions() {
        if(states == null) {
            states = new HashSet<>();
        }
        Set<String> regionStates = new HashSet<>();
        if(region != null && region.size() > 0) {
            for(String reg : region) {
                regionStates.addAll(REGIONS_MAP.get(reg));
            }
        }
        states.addAll(regionStates);
//        if (region == null || region.size() == 0) {
//            states = new HashSet<>(STATES_LIST);
//        } else {
//            states = new HashSet<>();
//            for (String region : region) {
//                states.addAll(REGIONS_MAP.get(region));
//            }
//        }
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
