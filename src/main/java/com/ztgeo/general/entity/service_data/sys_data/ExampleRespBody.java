package com.ztgeo.general.entity.service_data.sys_data;

import com.ztgeo.general.util.chenbin.TimeUtil;

import java.io.Serializable;
import java.util.Date;

public class ExampleRespBody implements Serializable {
    private String receiptNumber;
    private String businessType;
    private String approveType;             //实例名
    private String approveProcessinstanceId;//实例ID
    private String receiptMan;              //创建人
    private String createTime;              //创建时间
    private String endTime;                 //结束时间
    private String isDue;                   //是否完结

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getApproveType() {
        return approveType;
    }

    public void setApproveType(String approveType) {
        this.approveType = approveType;
    }

    public String getApproveProcessinstanceId() {
        return approveProcessinstanceId;
    }

    public void setApproveProcessinstanceId(String approveProcessinstanceId) {
        this.approveProcessinstanceId = approveProcessinstanceId;
    }

    public String getReceiptMan() {
        return receiptMan;
    }

    public void setReceiptMan(String receiptMan) {
        this.receiptMan = receiptMan;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        if(createTime!=null) {
            this.createTime = TimeUtil.getTimeString(createTime);
        }
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        if(endTime!=null) {
            this.endTime = TimeUtil.getTimeString(endTime);
        }
    }

    public String getIsDue() {
        return isDue;
    }

    public void setIsDue(String isDue) {
        this.isDue = isDue;
    }
}
