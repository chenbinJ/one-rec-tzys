package com.ztgeo.general.mapper.penghao;

import com.ztgeo.general.entity.Position;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Step_Position;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SjPowerStepPositionMapper {
    int deleteByPrimaryKey(String powerId);

    int insertSelective(SJ_Power_Step_Position record);

    void deleteByStepId(String stepId);

    Integer findPowerStepByReadOnly(@Param("stepId") String stepId, @Param("permissionLevel") String permissionLevel, @Param("list") List<String> list);

    void insertList(List<SJ_Power_Step_Position> list);

    List<SJ_Power_Step_Position> getPositionByStepIdOrQx(@Param("stepId") String stepId, @Param("permissionLevel") String permissionLevel);

    List<SJ_Power_Step_Position> getPositionByStepId(String stepId);

    SJ_Power_Step_Position selectByPrimaryKey(String powerId);

    int updateByPrimaryKeySelective(SJ_Power_Step_Position record);


    int updateByPrimaryKey(SJ_Power_Step_Position record);
}