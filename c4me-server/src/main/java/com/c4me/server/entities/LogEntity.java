package com.c4me.server.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
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
@Table(name = "Log", schema = "siyoliu")
public class LogEntity {
    private int id;
    private Timestamp createTime;
    private String description;
    private String exceptionCode;
    private String exceptionDetail;
    private String params;
    private String requestIp;
    private String service;
    private String type;
    private Integer userRole;
    private String username;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    @Column(name = "description", nullable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "exception_code", nullable = true, length = 255)
    public String getExceptionCode() {
        return exceptionCode;
    }

    public void setExceptionCode(String exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    @Basic
    @Column(name = "exception_detail", nullable = true, length = 255)
    public String getExceptionDetail() {
        return exceptionDetail;
    }

    public void setExceptionDetail(String exceptionDetail) {
        this.exceptionDetail = exceptionDetail;
    }

    @Basic
    @Column(name = "params", nullable = true, length = 255)
    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    @Basic
    @Column(name = "request_ip", nullable = true, length = 255)
    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    @Basic
    @Column(name = "service", nullable = true, length = 255)
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Basic
    @Column(name = "type", nullable = true, length = 255)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "user_role", nullable = true)
    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    @Basic
    @Column(name = "username", nullable = true, length = 255)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogEntity logEntity = (LogEntity) o;
        return id == logEntity.id &&
                Objects.equals(createTime, logEntity.createTime) &&
                Objects.equals(description, logEntity.description) &&
                Objects.equals(exceptionCode, logEntity.exceptionCode) &&
                Objects.equals(exceptionDetail, logEntity.exceptionDetail) &&
                Objects.equals(params, logEntity.params) &&
                Objects.equals(requestIp, logEntity.requestIp) &&
                Objects.equals(service, logEntity.service) &&
                Objects.equals(type, logEntity.type) &&
                Objects.equals(userRole, logEntity.userRole) &&
                Objects.equals(username, logEntity.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createTime, description, exceptionCode, exceptionDetail, params, requestIp, service, type, userRole, username);
    }
}
