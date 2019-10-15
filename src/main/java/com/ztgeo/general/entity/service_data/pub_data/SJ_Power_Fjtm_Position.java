package com.ztgeo.general.entity.service_data.pub_data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@Table(name = "sj_power_fjtm_position")
public class SJ_Power_Fjtm_Position {
    @Id
    private String powerId;                  //权限id
    private String powerTitle;               //权限名称
    private String powerNode;                 //岗位条目权限说明
    private String entryId;                  //附件条目表中的条目id
    private String positionId;                 //部门id
    private String isnecessary;              //是否必选（针对部门）
    private String permissionLevel;          //权限级别（读/写/空写）
    private String createBy;           //创建人
    private Date createTime;               //创建时间
    private Date enableTime;               //生效时间
    private String status;                   //状态
    private String ext1;                     //扩展字段1
    private String ext2;                     //扩展字段2

    public String getPowerId() {
        return powerId;
    }

    public void setPowerId(String powerId) {
        this.powerId = powerId == null ? null : powerId.trim();
    }

    public String getPowerTitle() {
        return powerTitle;
    }

    public void setPowerTitle(String powerTitle) {
        this.powerTitle = powerTitle == null ? null : powerTitle.trim();
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId == null ? null : entryId.trim();
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId == null ? null : positionId.trim();
    }

    public String getIsnecessary() {
        return isnecessary;
    }

    public void setIsnecessary(String isnecessary) {
        this.isnecessary = isnecessary == null ? null : isnecessary.trim();
    }

    public String getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(String permissionLevel) {
        this.permissionLevel = permissionLevel == null ? null : permissionLevel.trim();
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEnableTime() {
        return enableTime;
    }

    public void setEnableTime(Date enableTime) {
        this.enableTime = enableTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1 == null ? null : ext1.trim();
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2 == null ? null : ext2.trim();
    }

    public String getPowerNode() {
        return powerNode;
    }

    public void setPowerNode(String powerNode) {
        this.powerNode = powerNode == null ? null : powerNode.trim();
    }
}