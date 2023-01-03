package com.github.charlesvhe.nativeimage.provider;

import javax.annotation.processing.Generated;

/**
 * ConfigMeta is a Querydsl bean type
 */
@Generated("com.querydsl.codegen.BeanSerializer")
public class ConfigMeta {

    private String appId;

    private String code;

    private String columnName;

    private String description;

    private java.time.LocalDateTime gmtCreate;

    private java.time.LocalDateTime gmtModified;

    private Long id;

    private String property;

    private Integer sort;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public java.time.LocalDateTime getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(java.time.LocalDateTime gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public java.time.LocalDateTime getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(java.time.LocalDateTime gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

}

