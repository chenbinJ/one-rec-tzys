package com.ztgeo.general.entity.service_data.interface_response;

import com.ztgeo.general.util.chenbin.TimeUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Sj_Inquiry_Record implements Serializable {
    private String recordId;
    private String recordTitle;
    private String interfaceCode;
    private String serviceCode;
    private String returnData;
    private String executor;
    private String executionTime;
    private String applicant;
    private String IDNumber;
    private String pdfPath;

    private List<Sj_Inquiry_Record_Ext> recordExts;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getRecordTitle() {
        return recordTitle;
    }

    public void setRecordTitle(String recordTitle) {
        this.recordTitle = recordTitle;
    }

    public String getInterfaceCode() {
        return interfaceCode;
    }

    public void setInterfaceCode(String interfaceCode) {
        this.interfaceCode = interfaceCode;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getReturnData() {
        return returnData;
    }

    public void setReturnData(String returnData) {
        this.returnData = returnData;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Date executionTime) {
        this.executionTime = TimeUtil.getTimeString(executionTime);
    }

    public List<Sj_Inquiry_Record_Ext> getRecordExts() {
        return recordExts;
    }

    public void setRecordExts(List<Sj_Inquiry_Record_Ext> recordExts) {
        this.recordExts = recordExts;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getIDNumber() {
        return IDNumber;
    }

    public void setIDNumber(String IDNumber) {
        this.IDNumber = IDNumber;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }
}
