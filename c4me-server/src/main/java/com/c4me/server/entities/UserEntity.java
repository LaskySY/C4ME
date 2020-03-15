package com.c4me.server.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

/**
 * @Description:
 * @Author: Maciej Wlodek
 * @CreateDate: 03-15-2020
 */

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "User", schema = "siyoliu")
public class UserEntity {
    private String username;
    private String name;
    private String password;
    private Integer role;
    private Timestamp createTime;
    private Timestamp updateTime;
    private ProfileEntity profileByUsername;
    private Collection<StudentApplicationEntity> studentApplicationsByUsername;

    @Id
    @Column(name = "username", nullable = false, length = 255)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "password", nullable = true, length = 255)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "role", nullable = true)
    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    @Basic
    @Column(name = "create_time", nullable = true)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_time", nullable = true)
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(name, that.name) &&
                Objects.equals(password, that.password) &&
                Objects.equals(role, that.role) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, name, password, role, createTime, updateTime);
    }

    @OneToOne(mappedBy = "userByUsername")
    public ProfileEntity getProfileByUsername() {
        return profileByUsername;
    }

    public void setProfileByUsername(ProfileEntity profileByUsername) {
        this.profileByUsername = profileByUsername;
    }

    @OneToMany(mappedBy = "userByUsername")
    public Collection<StudentApplicationEntity> getStudentApplicationsByUsername() {
        return studentApplicationsByUsername;
    }

    public void setStudentApplicationsByUsername(Collection<StudentApplicationEntity> studentApplicationsByUsername) {
        this.studentApplicationsByUsername = studentApplicationsByUsername;
    }
}
