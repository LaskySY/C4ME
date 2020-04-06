package com.c4me.server.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-15-2020
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Major", schema = "siyoliu")
public class MajorEntity {
    private String name;
    private Collection<CollegeMajorAssociationEntity> collegeMajorAssociationsByName;
    private Collection<HighschoolMajorAssociationEntity> highschoolMajorAssociationsByName;
    private Collection<ProfileEntity> profilesByName;
    private Collection<ProfileEntity> profilesByName_0;

    @Id
    @Column(name = "name", nullable = false, length = 45)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MajorEntity that = (MajorEntity) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @OneToMany(mappedBy = "majorByMajorName")
    public Collection<CollegeMajorAssociationEntity> getCollegeMajorAssociationsByName() {
        return collegeMajorAssociationsByName;
    }

    public void setCollegeMajorAssociationsByName(Collection<CollegeMajorAssociationEntity> collegeMajorAssociationsByName) {
        this.collegeMajorAssociationsByName = collegeMajorAssociationsByName;
    }

    @OneToMany(mappedBy = "majorByMajorName")
    public Collection<HighschoolMajorAssociationEntity> getHighschoolMajorAssociationsByName() {
        return highschoolMajorAssociationsByName;
    }

    public void setHighschoolMajorAssociationsByName(Collection<HighschoolMajorAssociationEntity> highschoolMajorAssociationsByName) {
        this.highschoolMajorAssociationsByName = highschoolMajorAssociationsByName;
    }

    @OneToMany(mappedBy = "majorByMajor1")
    public Collection<ProfileEntity> getProfilesByName() {
        return profilesByName;
    }

    public void setProfilesByName(Collection<ProfileEntity> profilesByName) {
        this.profilesByName = profilesByName;
    }

    @OneToMany(mappedBy = "majorByMajor2")
    public Collection<ProfileEntity> getProfilesByName_0() {
        return profilesByName_0;
    }

    public void setProfilesByName_0(Collection<ProfileEntity> profilesByName_0) {
        this.profilesByName_0 = profilesByName_0;
    }
}
