package com.ztgeo.general.entity.service_data.interface_response;

import java.io.Serializable;

public class Sj_Inquiry_Record_Ext implements Serializable {
    private String extId;
    private String recordId;
    private String paramsId;
    private String extKey;
    private String extName;
    private String extValue;
    private String isFile;
    private Integer ifFileSort;

    public Sj_Inquiry_Record_Ext() {
        super();
    }

    public Sj_Inquiry_Record_Ext(String extId, String recordId, String paramsId, String extKey, String extName, String extValue, String isFile, Integer ifFileSort) {
        this.extId = extId;
        this.recordId = recordId;
        this.paramsId = paramsId;
        this.extKey = extKey;
        this.extName = extName;
        this.extValue = extValue;
        this.isFile = isFile;
        this.ifFileSort = ifFileSort;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getParamsId() {
        return paramsId;
    }

    public void setParamsId(String paramsId) {
        this.paramsId = paramsId;
    }

    public String getExtKey() {
        return extKey;
    }

    public void setExtKey(String extKey) {
        this.extKey = extKey;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public String getExtValue() {
        return extValue;
    }

    public void setExtValue(String extValue) {
        this.extValue = extValue;
    }

    public String getIsFile() {
        return isFile;
    }

    public void setIsFile(String isFile) {
        this.isFile = isFile;
    }

    public Integer getIfFileSort() {
        return ifFileSort;
    }

    public void setIfFileSort(Integer ifFileSort) {
        this.ifFileSort = ifFileSort;
    }
}
