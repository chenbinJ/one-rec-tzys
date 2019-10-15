package com.ztgeo.general.entity.service_data.pub_data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "sj_power_service_position")
public class SJ_Power_Service_Position implements Serializable {

    @Id
    private String powerId;                      //关联id

    private String powerTitle;                   //权限名称
    private String powerNode;                 //服务接口权限说明
    private String serviceId;                       //服务id
    private String positionId;                    //岗位id
    private String permissionLevel;              //权限级别（前端权限：读/写/空写/非空修改/权限禁用）
    private String showMortgage;            //是否显示抵押
    private String showObjection;           //是否显示异议
    private String showSequestration;       //是否显示查封
    private String createBy;           //创建人
    private Date createTime;                   //创建时间
    private Date enableTime;                   //生效时间
    private String status;                       //状态
    private String ext1;
    private String ext2;

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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(String permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public String getShowMortgage() {
        return showMortgage;
    }

    public void setShowMortgage(String showMortgage) {
        this.showMortgage = showMortgage;
    }

    public String getShowObjection() {
        return showObjection;
    }

    public void setShowObjection(String showObjection) {
        this.showObjection = showObjection;
    }

    public String getShowSequestration() {
        return showSequestration;
    }

    public void setShowSequestration(String showSequestration) {
        this.showSequestration = showSequestration;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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
        this.status = status;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }
}
