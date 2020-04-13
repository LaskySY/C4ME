package com.c4me.server.entities;

import javax.persistence.*;
import java.util.Objects;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 04-13-2020
 */

@Entity
@Table(name = "major_alias", schema = "siyoliu")
public class MajorAliasEntity {
    private String alias;
    private MajorEntity majorByMajorName;

    @Id
    @Column(name = "alias", nullable = false, length = 45)
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MajorAliasEntity that = (MajorAliasEntity) o;
        return Objects.equals(alias, that.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alias);
    }

    @ManyToOne
    @JoinColumn(name = "major_name", referencedColumnName = "name")
    public MajorEntity getMajorByMajorName() {
        return majorByMajorName;
    }

    public void setMajorByMajorName(MajorEntity majorByMajorName) {
        this.majorByMajorName = majorByMajorName;
    }
}
