package com.ztgeo.general.entity.activity;

import com.fasterxml.jackson.annotation.JsonFormat;


import java.util.Date;


public class Approve {
    private String id;//主键ID

    private String approveFkId;//审批外键

    private String approveProcessinstanceid;//流程实例ID

    private String approveType;//流程名称

    private String approveStatus;//审批状态

    private String approvePerson;//审批人

    private String approveTaskId;//taskID

    private String approvePaticipant;//参与者

    private Integer approveState;//状态

    private String approveCreateDapartment;

    //审批标识
    private String approveFlag;

    private String applyPerson;

    private String applyDepartment;

    private String annualWorkPlan;

    private String applyFileName;

    private String requiredCost;

    private String attr1;

    private String attr2;

    private String attr3;

    private String permission;//存储临时数据(权限)

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getApproveCreateDapartment() {
        return approveCreateDapartment;
    }


    public Integer getApproveState() {
        return approveState;
    }

    public void setApproveState(Integer approveState) {
        this.approveState = approveState;
    }

    public void setApproveCreateDapartment(String approveCreateDapartment) {
        this.approveCreateDapartment = approveCreateDapartment;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date approveTaskWarningTime;//预警时间

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date approveTaskOverdueTime;//超时时间

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date approveTaskEndTime;//结束时间

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date approveStartTime;//审批创建时间

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date approveTaskStartTime;//节点开始时间


    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date applyTime;

    public String getApprovePaticipant() {
        return approvePaticipant;
    }

    public void setApprovePaticipant(String approvePaticipant) {
        this.approvePaticipant = approvePaticipant;
    }

    public String getApproveTaskId() {
        return approveTaskId;
    }

    public void setApproveTaskId(String approveTaskId) {
        this.approveTaskId = approveTaskId;
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

    public Date getApproveTaskStartTime() {
        return approveTaskStartTime;
    }

    public void setApproveTaskStartTime(Date approveTaskStartTime) {
        this.approveTaskStartTime = approveTaskStartTime;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getRequiredCost() {
        return requiredCost;
    }

    public void setRequiredCost(String requiredCost) {
        this.requiredCost = requiredCost;
    }

    public String getApplyFileName() {
        return applyFileName;
    }

    public void setApplyFileName(String applyFileName) {
        this.applyFileName = applyFileName;
    }

    public String getAnnualWorkPlan() {
        return annualWorkPlan;
    }

    public void setAnnualWorkPlan(String annualWorkPlan) {
        this.annualWorkPlan = annualWorkPlan;
    }

    public String getApplyPerson() {
        return applyPerson;
    }

    public void setApplyPerson(String applyPerson) {
        this.applyPerson = applyPerson;
    }

    public String getApplyDepartment() {
        return applyDepartment;
    }

    public void setApplyDepartment(String applyDepartment) {
        this.applyDepartment = applyDepartment;
    }

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

    public String getApproveFlag() {
        return approveFlag;
    }

    public void setApproveFlag(String approveFlag) {
        this.approveFlag = approveFlag;
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