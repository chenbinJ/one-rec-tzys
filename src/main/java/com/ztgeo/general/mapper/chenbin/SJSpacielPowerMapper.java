package com.ztgeo.general.mapper.chenbin;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Spaciel_Position_Power;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface SJSpacielPowerMapper {
    public SJ_Spaciel_Position_Power selectPowerByPositionId(@Param("positionId") String postionId);
    public Integer insertPower(SJ_Spaciel_Position_Power power);
    public Integer deletePower(@Param("positionId") String postionId);
}
