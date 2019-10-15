package com.ztgeo.general.mapper.penghao;

import com.ztgeo.general.entity.service_data.sys_data.ActModelPrint;
import org.apache.ibatis.annotations.Param;

public interface ActModelPrintMapper {
    int deleteByModelId(String modelId);

    int insert(ActModelPrint record);

    int insertSelective(ActModelPrint record);

    ActModelPrint selectByModelId(String modelId);

    int updateByPrimaryKeySelective(ActModelPrint record);

    int updateByPrimaryKey(ActModelPrint record);

    public String selectPrintPathByModelId(@Param("modelId") String modelId);
}
