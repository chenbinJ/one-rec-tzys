package com.ztgeo.general.entity.extend;

public class Fjtm_Power {

    private String entryId;            //附件条目id
    private String entryName;          //条目名称
    private String processMouldId;     //隶属流程模板id
    private String entryType;           //是否必选（针对整个流程）
    private String node;               //业务说明
    private String powerId;            //权限id
    private String powerTitle;          //权限名称
    private String powerNode;           //岗位条目权限说明
    private String isnecessary;         //是否必选
    private String permissionLevel;     //权限级别

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getProcessMouldId() {
        return processMouldId;
    }

    public void setProcessMouldId(String processMouldId) {
        this.processMouldId = processMouldId;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getPowerId() {
        return powerId;
    }

    public void setPowerId(String powerId) {
        this.powerId = powerId;
    }

    public String getPowerTitle() {
        return powerTitle;
    }

    public void setPowerTitle(String powerTitle) {
        this.powerTitle = powerTitle;
    }

    public String getPowerNode() {
        return powerNode;
    }

    public void setPowerNode(String powerNode) {
        this.powerNode = powerNode;
    }

    public String getIsnecessary() {
        return isnecessary;
    }

    public void setIsnecessary(String isnecessary) {
        this.isnecessary = isnecessary;
    }

    public String getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(String permissionLevel) {
        this.permissionLevel = permissionLevel;
    }
}
