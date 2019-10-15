package com.ztgeo.general.entity.activity;

import java.util.Date;


public class ActivityPojo {

    //model
    private String modelId;//model ID
    private String modelName;//名称
    private String modelKey;//key
    private String modelDescription;//描述
    private String modelCategory;//类别

    // 流程部署表
    private String ardId;
    private String ardName;
    private String ardCateGory;
    private String ardTenantId;
    private Date ardDeployTime;

    //流程二进制表
    private String agbId;
    private String agbName;
    private String agbCategory;
    private String agbTenantId;
    private Date agbDeployTime;

    //流程定义表
    private String arpId;
    private String arpRev;
    private String arpCategory;
    private String arpName;
    private String arpKey;
    private String arpVersion;
    private String arpDeploymentId;
    private String arpResourceName;
    private String arpDgrmResourceName;
    private String arpDescription;
    private String arpHasStartFormKey;
    private String arpHasGraphicalNotation;
    private String arpSuspensionState;
    private String arpTenantId;

    /**
     * 页数和行数
     */
    private String page;
    private String rows;


    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelKey() {
        return modelKey;
    }

    public void setModelKey(String modelKey) {
        this.modelKey = modelKey;
    }

    public String getModelDescription() {
        return modelDescription;
    }

    public void setModelDescription(String modelDescription) {
        this.modelDescription = modelDescription;
    }

    public String getModelCategory() {
        return modelCategory;
    }

    public void setModelCategory(String modelCategory) {
        this.modelCategory = modelCategory;
    }

    public String getArdId() {
        return ardId;
    }

    public void setArdId(String ardId) {
        this.ardId = ardId;
    }

    public String getArdName() {
        return ardName;
    }

    public void setArdName(String ardName) {
        this.ardName = ardName;
    }

    public String getArdCateGory() {
        return ardCateGory;
    }

    public void setArdCateGory(String ardCateGory) {
        this.ardCateGory = ardCateGory;
    }

    public String getArdTenantId() {
        return ardTenantId;
    }

    public void setArdTenantId(String ardTenantId) {
        this.ardTenantId = ardTenantId;
    }

    public Date getArdDeployTime() {
        return ardDeployTime;
    }

    public void setArdDeployTime(Date ardDeployTime) {
        this.ardDeployTime = ardDeployTime;
    }

    public String getAgbId() {
        return agbId;
    }

    public void setAgbId(String agbId) {
        this.agbId = agbId;
    }

    public String getAgbName() {
        return agbName;
    }

    public void setAgbName(String agbName) {
        this.agbName = agbName;
    }

    public String getAgbCategory() {
        return agbCategory;
    }

    public void setAgbCategory(String agbCategory) {
        this.agbCategory = agbCategory;
    }

    public String getAgbTenantId() {
        return agbTenantId;
    }

    public void setAgbTenantId(String agbTenantId) {
        this.agbTenantId = agbTenantId;
    }

    public Date getAgbDeployTime() {
        return agbDeployTime;
    }

    public void setAgbDeployTime(Date agbDeployTime) {
        this.agbDeployTime = agbDeployTime;
    }

    public String getArpId() {
        return arpId;
    }

    public void setArpId(String arpId) {
        this.arpId = arpId;
    }

    public String getArpRev() {
        return arpRev;
    }

    public void setArpRev(String arpRev) {
        this.arpRev = arpRev;
    }

    public String getArpCategory() {
        return arpCategory;
    }

    public void setArpCategory(String arpCategory) {
        this.arpCategory = arpCategory;
    }

    public String getArpName() {
        return arpName;
    }

    public void setArpName(String arpName) {
        this.arpName = arpName;
    }

    public String getArpKey() {
        return arpKey;
    }

    public void setArpKey(String arpKey) {
        this.arpKey = arpKey;
    }

    public String getArpVersion() {
        return arpVersion;
    }

    public void setArpVersion(String arpVersion) {
        this.arpVersion = arpVersion;
    }

    public String getArpDeploymentId() {
        return arpDeploymentId;
    }

    public void setArpDeploymentId(String arpDeploymentId) {
        this.arpDeploymentId = arpDeploymentId;
    }

    public String getArpResourceName() {
        return arpResourceName;
    }

    public void setArpResourceName(String arpResourceName) {
        this.arpResourceName = arpResourceName;
    }

    public String getArpDgrmResourceName() {
        return arpDgrmResourceName;
    }

    public void setArpDgrmResourceName(String arpDgrmResourceName) {
        this.arpDgrmResourceName = arpDgrmResourceName;
    }

    public String getArpDescription() {
        return arpDescription;
    }

    public void setArpDescription(String arpDescription) {
        this.arpDescription = arpDescription;
    }

    public String getArpHasStartFormKey() {
        return arpHasStartFormKey;
    }

    public void setArpHasStartFormKey(String arpHasStartFormKey) {
        this.arpHasStartFormKey = arpHasStartFormKey;
    }

    public String getArpHasGraphicalNotation() {
        return arpHasGraphicalNotation;
    }

    public void setArpHasGraphicalNotation(String arpHasGraphicalNotation) {
        this.arpHasGraphicalNotation = arpHasGraphicalNotation;
    }

    public String getArpSuspensionState() {
        return arpSuspensionState;
    }

    public void setArpSuspensionState(String arpSuspensionState) {
        this.arpSuspensionState = arpSuspensionState;
    }

    public String getArpTenantId() {
        return arpTenantId;
    }

    public void setArpTenantId(String arpTenantId) {
        this.arpTenantId = arpTenantId;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }
}
