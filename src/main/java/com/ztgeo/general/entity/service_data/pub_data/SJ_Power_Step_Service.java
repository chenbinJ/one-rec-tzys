package com.ztgeo.general.entity.service_data.pub_data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SJ_Power_Step_Service implements Serializable {
    private String powerId;                      //关联id
    private String powerTitle;                   //权限名称
    private String powerNode;                 //服务接口权限说明
    private String stepId;                       //步骤id
    private String serviceId;                    //服务id
    private String permissionLevel;              //权限级别（前端权限：读/写/空写/非空修改/权限禁用）
    private String createBy;           //创建人
    private String createTime;                   //创建时间
    private String enableTime;                   //生效时间
    private String status;                       //状态
    private Integer dealOrder;                  //执行顺序

    private SJ_Service service;                  //服务


    public SJ_Service getService() {
        return service;
    }

    public void setService(SJ_Service service) {
        this.service = service;
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

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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

    public Integer getDealOrder() {
        return dealOrder;
    }

    public void setDealOrder(Integer dealOrder) {
        this.dealOrder = dealOrder;
    }

    public String getPowerNode() {
        return powerNode;
    }

    public void setPowerNode(String powerNode) {
        this.powerNode = powerNode;
    }
}
