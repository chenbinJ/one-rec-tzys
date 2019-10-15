package com.ztgeo.general.mapper.penghao;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjfile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SJ_FjfileMapper {
    int deleteByPrimaryKey(String fileId);

    int insert(SJ_Fjfile record);

    List<SJ_Fjfile> selectSjFjfileByNameAndPnode(@Param("name") String name, @Param("pnode") String pnode);

    int insertSelective(SJ_Fjfile record);

    SJ_Fjfile selectByPrimaryKey(String fileId);

    int updateByPrimaryKeySelective(SJ_Fjfile record);

    int updateByPrimaryKeyWithBLOBs(SJ_Fjfile record);

    int updateByPrimaryKey(SJ_Fjfile record);
}