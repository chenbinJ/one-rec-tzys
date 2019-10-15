package com.ztgeo.general.mapper.penghao;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Fjtm_Position;
import org.apache.ibatis.annotations.Param;

public interface SJPowerFjtmPositionMapper {
    int deleteByPrimaryKey(String powerId);

    int insert(SJ_Power_Fjtm_Position record);

    int insertSelective(SJ_Power_Fjtm_Position record);

    SJ_Power_Fjtm_Position findLevelByEntryPosition(@Param("entryId") String entryId, @Param("positionId") String positionId);

    SJ_Power_Fjtm_Position selectByPrimaryKey(String powerId);

    int updateByPrimaryKeySelective(SJ_Power_Fjtm_Position record);

    int updateByPrimaryKeyWithBLOBs(SJ_Power_Fjtm_Position record);

    int updateByPrimaryKey(SJ_Power_Fjtm_Position record);
}