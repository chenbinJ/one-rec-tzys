package com.ztgeo.general.mapper.chenbin;

import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Service_Position;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PositSvrManagerMapper extends CommonMapper<SJ_Power_Service_Position> {
    public Integer deletePositSvrByParam(@Param("serviceId")String serviceId,@Param("positionId")String positionId);
    public Integer selectCountOfPositSvrSame(SJ_Power_Service_Position svrPosit);
    public SJ_Power_Service_Position selectPositionSvr(@Param("positionId")String positionId,@Param("serviceCode")String serviceCode);
}
