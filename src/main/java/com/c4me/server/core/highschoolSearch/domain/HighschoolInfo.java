package com.c4me.server.core.highschoolSearch.domain;

import com.c4me.server.entities.HighschoolEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description: Domain object representing a high school object that can be passed to the frontend
 * @Author: Maciej Wlodek
 * @CreateDate: 04-10-2020
 */

@Setter
@Getter
@NoArgsConstructor
public class HighschoolInfo {
    String name;
    String type;
    Integer satMath;
    Integer satEbrw;
    Integer satOverall;
    Integer actReading;
    Integer actScience;
    Integer actMath;
    Integer actEnglish;
    Integer actComposite;

    public HighschoolInfo(HighschoolEntity highschoolEntity) {
        this.name = highschoolEntity.getName();
        this.type = highschoolEntity.getType();
        this.satMath = highschoolEntity.getSatMath();
        this.satEbrw = highschoolEntity.getSatEbrw();
        this.satOverall = highschoolEntity.getSatOverall();
        this.actReading = highschoolEntity.getActReading();
        this.actScience = highschoolEntity.getActScience();
        this.actMath = highschoolEntity.getActMath();
        this.actEnglish = highschoolEntity.getActEnglish();
        this.actComposite = highschoolEntity.getActComposite();
    }
}
