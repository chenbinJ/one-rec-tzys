package com.ztgeo.general.mapper.activity;

import com.ztgeo.general.entity.activity.actApprove;

public interface actApproveMapper {
    Integer deleteByPrimaryKey(String id);

    Integer insert(actApprove record);

    Integer insertSelective(actApprove record);

    actApprove selectByPrimaryKey(String id);

    Integer updateByPrimaryKeySelective(actApprove record);

    Integer updateByPrimaryKey(actApprove record);
}