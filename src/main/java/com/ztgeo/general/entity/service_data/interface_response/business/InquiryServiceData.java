package com.ztgeo.general.entity.service_data.interface_response.business;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Information;

import java.io.Serializable;
import java.util.List;

public class InquiryServiceData <T extends InquiryInformation> implements Serializable {
    private String serviceCode;
    private String serviceDataTo;       //保留字段，用于入库
    private List<T> serviceDataInfos;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceDataTo() {
        return serviceDataTo;
    }

    public void setServiceDataTo(String serviceDataTo) {
        this.serviceDataTo = serviceDataTo;
    }

    public List<T> getServiceDataInfos() {
        return serviceDataInfos;
    }

    public void setServiceDataInfos(List<T> serviceDataInfos) {
        this.serviceDataInfos = serviceDataInfos;
    }
}
