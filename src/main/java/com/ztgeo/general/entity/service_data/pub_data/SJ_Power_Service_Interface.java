package com.ztgeo.general.entity.service_data.pub_data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SJ_Power_Service_Interface implements Serializable {
    private String powerId;                   //权限id
    private String powerTitle;                //权限名称
    private String powerNode;                 //服务接口权限说明
    private String serviceId;                 //服务id
    private String interfaceId;               //接口id
    private String dataCode;                    //数据标识
    private String createBy;           //创建人
    private String createTime;                //创建时间
    private String enableTime;                //生效时间
    private String status;                    //状态
    private String ext1;                      //扩展字段1
    private String ext2;                      //扩展字段2

    private SJ_Interface interfacer;           //接口对象

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

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getDataCode() {
        return dataCode;
    }

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
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

    public SJ_Interface getInterfacer() {
        return interfacer;
    }

    public void setInterfacer(SJ_Interface interfacer) {
        this.interfacer = interfacer;
    }

    @Override
    public String toString() {
        return "SJ_Power_Service_Interface{" +
                "powerId='" + powerId + '\'' +
                ", powerTitle='" + powerTitle + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", interfaceId='" + interfaceId + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime='" + createTime + '\'' +
                ", enableTime='" + enableTime + '\'' +
                ", status='" + status + '\'' +
                ", ext1='" + ext1 + '\'' +
                ", ext2='" + ext2 + '\'' +
                ", interfacer=" + interfacer +
                '}';
    }

    public String getPowerNode() {
        return powerNode;
    }

    public void setPowerNode(String powerNode) {
        this.powerNode = powerNode;
    }
}
