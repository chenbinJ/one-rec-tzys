package com.ztgeo.general.entity.service_data.busi_data;

import java.io.Serializable;
import java.util.Date;

public class FileEntryAndPowerEntity implements Serializable {
    private String entryId;            //附件条目id
    private String entryName;          //条目名称
    private String processMouldId;     //隶属流程模板id
    private String node;               //业务说明
    private String entryType;          //是否必选（针对整个流程）
    private String entryExt1;               //扩展字段1
    private String entryExt2;               //扩展字段2

    private String powerId;                  //权限id
    private String powerTitle;               //权限名称
    private String powerNode;                 //岗位条目权限说明
    private String isnecessary;              //是否必选（针对部门）
    private String permissionLevel;          //权限级别（读/写/空写）
    private String powerExt1;                     //扩展字段1
    private String powerExt2;                     //扩展字段2

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

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getEntryExt1() {
        return entryExt1;
    }

    public void setEntryExt1(String entryExt1) {
        this.entryExt1 = entryExt1;
    }

    public String getEntryExt2() {
        return entryExt2;
    }

    public void setEntryExt2(String entryExt2) {
        this.entryExt2 = entryExt2;
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

    public String getPowerExt1() {
        return powerExt1;
    }

    public void setPowerExt1(String powerExt1) {
        this.powerExt1 = powerExt1;
    }

    public String getPowerExt2() {
        return powerExt2;
    }

    public void setPowerExt2(String powerExt2) {
        this.powerExt2 = powerExt2;
    }
}
