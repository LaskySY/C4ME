package com.c4me.server.core.highschoolSearch.domain;

import com.c4me.server.entities.HighschoolEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 04-12-2020
 */

@NoArgsConstructor
@Getter
@Setter
public class HighschoolInfo2 {
    String highschoolName;
    String type;
    Integer averageSAT;
    Integer averageACT;
    String academicQuality;

    public HighschoolInfo2(HighschoolEntity highschoolEntity) {
        this.highschoolName = highschoolEntity.getName();
        this.type = highschoolEntity.getType();
        this.averageSAT = highschoolEntity.getSatOverall();
        this.averageACT = highschoolEntity.getActComposite();
        this.academicQuality = highschoolEntity.getAcademicQuality();
    }
}
