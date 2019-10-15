package com.ztgeo.general.entity.activity;

import java.util.Date;

public class actApprove {
    private String id;

    private String approveFkId;

    private String approveProcessinstanceid;

    private String approveType;

    private String approveStatus;

    private String approvePerson;

    private Date approveStartTime;

    private Date approveTaskStartTime;

    private Date approveTaskWarningTime;

    private Date approveTaskOverdueTime;

    private Date approveTaskEndTime;

    private String attr1;

    private String attr2;

    private String attr3;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getApproveFkId() {
        return approveFkId;
    }

    public void setApproveFkId(String approveFkId) {
        this.approveFkId = approveFkId == null ? null : approveFkId.trim();
    }

    public String getApproveProcessinstanceid() {
        return approveProcessinstanceid;
    }

    public void setApproveProcessinstanceid(String approveProcessinstanceid) {
        this.approveProcessinstanceid = approveProcessinstanceid == null ? null : approveProcessinstanceid.trim();
    }

    public String getApproveType() {
        return approveType;
    }

    public void setApproveType(String approveType) {
        this.approveType = approveType == null ? null : approveType.trim();
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus == null ? null : approveStatus.trim();
    }

    public String getApprovePerson() {
        return approvePerson;
    }

    public void setApprovePerson(String approvePerson) {
        this.approvePerson = approvePerson == null ? null : approvePerson.trim();
    }

    public Date getApproveStartTime() {
        return approveStartTime;
    }

    public void setApproveStartTime(Date approveStartTime) {
        this.approveStartTime = approveStartTime;
    }

    public Date getApproveTaskStartTime() {
        return approveTaskStartTime;
    }

    public void setApproveTaskStartTime(Date approveTaskStartTime) {
        this.approveTaskStartTime = approveTaskStartTime;
    }

    public Date getApproveTaskWarningTime() {
        return approveTaskWarningTime;
    }

    public void setApproveTaskWarningTime(Date approveTaskWarningTime) {
        this.approveTaskWarningTime = approveTaskWarningTime;
    }

    public Date getApproveTaskOverdueTime() {
        return approveTaskOverdueTime;
    }

    public void setApproveTaskOverdueTime(Date approveTaskOverdueTime) {
        this.approveTaskOverdueTime = approveTaskOverdueTime;
    }

    public Date getApproveTaskEndTime() {
        return approveTaskEndTime;
    }

    public void setApproveTaskEndTime(Date approveTaskEndTime) {
        this.approveTaskEndTime = approveTaskEndTime;
    }

    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1 == null ? null : attr1.trim();
    }

    public String getAttr2() {
        return attr2;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2 == null ? null : attr2.trim();
    }

    public String getAttr3() {
        return attr3;
    }

    public void setAttr3(String attr3) {
        this.attr3 = attr3 == null ? null : attr3.trim();
    }
}