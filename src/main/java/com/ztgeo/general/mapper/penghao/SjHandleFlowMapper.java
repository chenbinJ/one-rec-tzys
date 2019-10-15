package com.ztgeo.general.mapper.penghao;

import com.ztgeo.general.entity.service_data.pub_data.SjHandleFlow;

import java.util.List;

public interface SjHandleFlowMapper {
    int deleteByPrimaryKey(String handleId);

    int insert(SjHandleFlow record);

    int insertSelective(SjHandleFlow record);

    List<SjHandleFlow> findHandleFlowByProcess(String processId);

    SjHandleFlow selectByPrimaryKey(String handleId);

    void insertList(List<SjHandleFlow> list);

    int updateByPrimaryKeySelective(SjHandleFlow record);

    int updateByPrimaryKey(SjHandleFlow record);
}