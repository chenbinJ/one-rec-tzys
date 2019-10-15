package com.ztgeo.general.entity.service_data.pub_data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SJ_Service implements Serializable {
    private String serviceId;                       //服务表id
    private String serviceName;                     //服务名称
    private String serviceType;                     //服务类型
    private String classification;                  //服务分类
    private String serviceTitle;                    //服务标题
    private String serviceDataTo;                   //服务数据流向
    private String serviceCode;                     //服务标识
    private String serviceNode;                     //服务说明
    private String positionServiceShowSetting;      //岗位服务数据加载策略
    private String createBy;           //创建人
    private String createTime;                   //创建时间
    private String status;                          //服务可用状态
    private String ext1;                            //扩展字段1
    private String ext2;                            //扩展字段2

    private List<SJ_Power_Service_Interface> interfacerVoList;    //接口集合

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
    }

    public String getServiceDataTo() {
        return serviceDataTo;
    }

    public void setServiceDataTo(String serviceDataTo) {
        this.serviceDataTo = serviceDataTo;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getPositionServiceShowSetting() {
        return positionServiceShowSetting;
    }

    public void setPositionServiceShowSetting(String positionServiceShowSetting) {
        this.positionServiceShowSetting = positionServiceShowSetting;
    }

    public String getServiceNode() {
        return serviceNode;
    }

    public void setServiceNode(String serviceNode) {
        this.serviceNode = serviceNode;
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

    public List<SJ_Power_Service_Interface> getInterfacerVoList() {
        return interfacerVoList;
    }

    public void setInterfacerVoList(List<SJ_Power_Service_Interface> interfacerVoList) {
        this.interfacerVoList = interfacerVoList;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }
}
