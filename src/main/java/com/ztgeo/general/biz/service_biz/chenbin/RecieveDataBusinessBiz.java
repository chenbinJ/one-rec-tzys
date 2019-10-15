package com.ztgeo.general.biz.service_biz.chenbin;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Sjsq;

public interface RecieveDataBusinessBiz {
    public String saveServiceData(SJ_Sjsq sjsq,String taskId);

    public SJ_Sjsq findSjsqServiceDataByCode(String sqbh,String serviceCode,String serviceDataTo);

    String removeServiceData(String infoId,String serviceCode,String taskId,String serviceDataTo);

    boolean preCheckServicesForAotuSubmitOut(String taskId,SJ_Sjsq sjsq);
}
