package com.c4me.server.core.collegeSearch.domain;

import com.c4me.server.config.constant.Const;
import com.c4me.server.core.admin.domain.ProfileInfo;
import lombok.*;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 04-29-2020
 */

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProfilesWithStatisticalSummary {

    List<ProfileInfo> profiles;
    Double averageSatMath;
    Double averageSatEbrw;
    Double averageActComposite;
    Double averageGpa;
    Double averageWeightedAvgPercentileScore;
    Double averageSat;

    Double averageSatMathAccepted;
    Double averageSatEbrwAccepted;
    Double averageActCompositeAccepted;
    Double averageGpaAccepted;
    Double averageWeightedAvgPercentileScoreAccepted;
    Double averageSatAccepted;

    public ProfilesWithStatisticalSummary(List<ProfileInfo> profiles) {
        this.profiles = profiles;
        computeStatisticalSummary();
    }

    private void computeStatisticalSummary() {
        Map<String, Integer> counts = new HashMap<>();
        Map<String, Double> scores = new HashMap<>();
        for(String field : Const.StatisticalSummary.FIELDS) {
            counts.put(field, 0);
            scores.put(field, 0.);
            counts.put(field+"Accepted", 0);
            scores.put(field+"Accepted", 0.);
        }
        for(ProfileInfo profileInfo : profiles) {
            BeanWrapperImpl wrapper = new BeanWrapperImpl(profileInfo);
            for(String field : Const.StatisticalSummary.FIELDS) {
                Object score = wrapper.getPropertyValue(field);
                if(score != null) {
                    counts.put(field, counts.get(field)+1);
                    double scoreD = (score instanceof Integer)? (double) ((Integer) score) : (double) score;
                    scores.put(field, scores.get(field)+scoreD);
                    if(profileInfo.getApplicationStatus().equals(Const.Status.ACCEPTED_STR)) {
                        counts.put(field+"Accepted", counts.get(field+"Accepted")+1);
                        scores.put(field+"Accepted", scores.get(field+"Accepted")+scoreD);
                    }
                }
            }
        }
        BeanWrapperImpl wrapper = new BeanWrapperImpl(this);
        for(String field : scores.keySet()) {
            String averageField = "average" + field.substring(0,1).toUpperCase() + field.substring(1);
            Integer finalCount = counts.get(field);
            Double finalScore = finalCount == 0? null : scores.get(field) / finalCount;
            wrapper.setPropertyValue(averageField, finalScore);
        }
    }

}
