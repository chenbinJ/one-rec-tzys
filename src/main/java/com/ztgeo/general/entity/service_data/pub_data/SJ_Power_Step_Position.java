package com.ztgeo.general.entity.service_data.pub_data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SJ_Power_Step_Position implements Serializable {
    private String powerId;                   //权限id
    private String powerTitle;                //权限名称
    private String powerNode;                 //服务接口权限说明
    private String positionId;                  //岗位id
    private String stepId;               	  //步骤id
    private String permissionLevel;			  //权限级别
    private String createBy;           //创建人
    private String createTime;                //创建时间
    private String enableTime;                //生效时间
    private String status;                    //状态


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


    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(String permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
    }

    public String getEnableTime() {
        return enableTime;
    }

    public void setEnableTime(Date enableTime) {
        this.enableTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(enableTime);
    }
    public void setEnableUseTime(String enableTime){
        this.enableTime = enableTime;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPowerNode() {
        return powerNode;
    }

    public void setPowerNode(String powerNode) {
        this.powerNode = powerNode;
    }

}
