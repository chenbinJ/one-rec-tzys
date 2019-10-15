package com.ztgeo.general.mapper.chenbin;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Service_Interface;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Step_Service;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Service;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SvrManagerMapper {
    public List<SJ_Service> selectSvr(@Param("searchParam") String searchParam,@Param("status") String status);
    public SJ_Service selectSvrById(@Param("svrId")String svrId);
    public Integer selectCountOfSvrCode(@Param("svrCode")String svrCode);
    public SJ_Service selectSvrByCode(@Param("svrCode")String svrCode);
    public Integer insertSvr(SJ_Service sjSvr);
    public Integer updateSvr(SJ_Service sjSvr);
    public Integer updateSvrStatus(@Param("status") String status,@Param("svrId")String svrId);
    public Integer deleteSvrById(@Param("svrId")String svrId);

    public List<SJ_Power_Service_Interface> selectSvrIntf(@Param("svrId") String svrId,@Param("searchParam") String searchParam);
    public SJ_Power_Service_Interface selectSvrIntfById(@Param("powerId") String powerId);
    public Integer insertSvrIntf(SJ_Power_Service_Interface svrIntfPower);
    public Integer updateSvrIntf(SJ_Power_Service_Interface svrIntfPower);
    public Integer deleteSvrIntfByParam(@Param("powerId") String powerId,@Param("svrId")String svrId,@Param("intfId")String intfId);

    public Integer selectCountOfSvrIntfSame(SJ_Power_Service_Interface svrIntf);
    public List<SJ_Power_Service_Interface> selectSvrIntfInfoById(@Param("serviceId") String serviceId);
    public String selectServiceDataToByServiceCode(@Param("serviceCode") String serviceCode);

    public Integer selectCountOfSvrIntfPower(@Param("svrCode")String svrCode,@Param("intfId")String intfId);
    public String selectPowerLevelWithPositAndServiceCode(@Param("positionId")String positionId,@Param("serviceCode")String serviceCode);
    SJ_Power_Service_Interface selectSvrIntfBySvrCodeAndIntfId(@Param("svrCode")String svrCode,@Param("intfId")String intfId);
}
