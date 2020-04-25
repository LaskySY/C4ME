package com.c4me.server.core.collegeSearch.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @Description: Request domain object containing a list of college names to
 * @Author: Maciej Wlodek
 * @CreateDate: 04-12-2020
 */

@NoArgsConstructor
@Setter
@Getter
public class CollegeRecommendationRequest {
    private List<String> collegeList;
}
