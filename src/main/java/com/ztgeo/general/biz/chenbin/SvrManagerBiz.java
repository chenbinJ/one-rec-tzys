package com.ztgeo.general.biz.chenbin;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Service_Interface;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Service;

import java.util.List;

public interface SvrManagerBiz {
    public List<SJ_Service> searchSvr(String searchParam);

    public List<SJ_Service> findEnableSvrs();

    public SJ_Service findSvrById(String svrId);

    public String selectSameSvrCode(String svrCode);

    public String isHavingSvrReceit(String svrId);

    public String addSvr(SJ_Service sjSvr);

    public String modifySvr(SJ_Service sjSvr);

    public String discardSvr(String svrId);

    public String enableSvr(String svrId);

    public String removeSvr(String svrId);

    public List<SJ_Power_Service_Interface> searchSvrIntfPowers(String svrIntfParam, String svrId);

    public SJ_Power_Service_Interface findSvrIntfById(String svrIntfId);

    public String addSvrIntfPower(SJ_Power_Service_Interface svrIntfPower);

    public String modifySvrIntfPower(SJ_Power_Service_Interface svrIntfPower);

    public String removeSvrIntf(String svrIntfId);
}
