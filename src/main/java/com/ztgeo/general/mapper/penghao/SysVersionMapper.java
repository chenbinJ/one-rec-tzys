package com.ztgeo.general.mapper.penghao;

import com.ztgeo.general.entity.service_data.sys_data.SysVersion;

public interface SysVersionMapper {
    int deleteByPrimaryKey(String sysId);

    int insert(SysVersion record);

    int insertSelective(SysVersion record);

    SysVersion findSysVersion();

    SysVersion selectByPrimaryKey(String sysId);

    int updateByPrimaryKeySelective(SysVersion record);

    int updateByPrimaryKey(SysVersion record);
}