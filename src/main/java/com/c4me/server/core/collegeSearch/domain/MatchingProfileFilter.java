package com.c4me.server.core.collegeSearch.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description:
 * @Author: Yousef Khan
 * @CreateDate: 04-24-2020
 */


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true, builderClassName = "MatchingProfileFilterBuilder", buildMethodName = "internalBuild")
@ToString
public class MatchingProfileFilter {

  private String name;
  private Integer minCollegeClass;
  private Integer maxCollegeClass;
  private List<String> highSchools;
  private List<String> applicationStatus;

  private List<Integer> applicationInts;

  private Boolean ascending = true;

  private Boolean strict = false;





  public static class MatchingProfileFilterBuilder{
    public MatchingProfileFilter build() {
      MatchingProfileFilter filter = internalBuild();
      return filter;
    }
  }

}
