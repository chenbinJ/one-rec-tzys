package com.ztgeo.general.entity.service_data.sys_data;

import com.ztgeo.general.entity.service_data.page.PageRequestBean;

import java.io.Serializable;

public class RecordSearchParams extends PageRequestBean implements Serializable {
    private String recordTitle;
    private String serviceCode;
    private String executor;
    private String startTime;
    private String endTime;

    public String getRecordTitle() {
        return recordTitle;
    }

    public void setRecordTitle(String recordTitle) {
        this.recordTitle = recordTitle;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
