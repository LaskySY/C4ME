package com.c4me.server.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-21-2020
 */
@Entity
@Table(name = "user", schema = "siyoliu")
public class UserEntity {

    private byte[] id;
    private String username;
    private String password;
    private String lastName;
    private String fistName;
    private String email;
    private Timestamp updateTime;
    private Timestamp createTime;


    @Basic
    @Column(name = "update_time", nullable = false)
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Id
    @Column(name = "id", nullable = false)
    public byte[] getId() {
        return id;
    }

    public void setId(byte[] id) {
        this.id = id;
    }

    @Basic
    @Column(name = "last_name", nullable = true, length = 32)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "fist_name", nullable = true, length = 32)
    public String getFistName() {
        return fistName;
    }

    public void setFistName(String fistName) {
        this.fistName = fistName;
    }

    @Basic
    @Column(name = "username", nullable = false, length = 128)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 128)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "create_time", nullable = false)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "email", nullable = true, length = 128)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(updateTime, that.updateTime) &&
                Arrays.equals(id, that.id) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(fistName, that.fistName) &&
                Objects.equals(username, that.username) &&
                Objects.equals(password, that.password) &&
                Objects.equals(createTime, that.createTime) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(updateTime, lastName, fistName, username, password, createTime, email);
        result = 31 * result + Arrays.hashCode(id);
        return result;
    }
}
