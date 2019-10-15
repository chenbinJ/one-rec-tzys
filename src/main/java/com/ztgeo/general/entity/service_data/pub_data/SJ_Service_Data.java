package com.ztgeo.general.entity.service_data.pub_data;

import java.io.Serializable;
import java.util.List;

public class SJ_Service_Data implements Serializable {
    private String serviceCode;
    private List<SJ_Information> serviceDataInfos;

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public List<SJ_Information> getServiceDataInfos() {
        return serviceDataInfos;
    }

    public void setServiceDataInfos(List<SJ_Information> serviceDataInfos) {
        this.serviceDataInfos = serviceDataInfos;
    }
}
