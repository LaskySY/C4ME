package com.c4me.server.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @Description:
 * @Author: Siyong Liu
 * @CreateDate: 02-22-2020
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "log", schema = "siyoliu")
public class LogEntity {
    private Integer id;
    private String userId;
    private String requestIp;
    private String userName;
    private Integer userRole;
    private String type;
    private String service;
    private String exceptionCode;
    private String exceptionDetail;
    private String description;
    private String params;
    private Timestamp updatedTime;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_id", nullable = true, length = 10)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "request_ip", nullable = true, length = 15)
    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    @Basic
    @Column(name = "user_name", nullable = true, length = 32)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
    @Column(name = "type", nullable = true, length = 32)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "service", nullable = true, length = 32)
    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Basic
    @Column(name = "exception_code", nullable = true, length = 8)
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
    @Column(name = "description", nullable = true, length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    @Column(name = "UPDATED_TIME", nullable = false)
    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogEntity logEntity = (LogEntity) o;
        return Objects.equals(id, logEntity.id) &&
                Objects.equals(userId, logEntity.userId) &&
                Objects.equals(requestIp, logEntity.requestIp) &&
                Objects.equals(userName, logEntity.userName) &&
                Objects.equals(userRole, logEntity.userRole) &&
                Objects.equals(type, logEntity.type) &&
                Objects.equals(service, logEntity.service) &&
                Objects.equals(exceptionCode, logEntity.exceptionCode) &&
                Objects.equals(exceptionDetail, logEntity.exceptionDetail) &&
                Objects.equals(description, logEntity.description) &&
                Objects.equals(params, logEntity.params) &&
                Objects.equals(updatedTime, logEntity.updatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, requestIp, userName, userRole, type, service, exceptionCode, exceptionDetail, description, params, updatedTime);
    }
}
