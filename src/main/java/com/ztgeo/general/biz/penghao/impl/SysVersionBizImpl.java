package com.ztgeo.general.biz.penghao.impl;

import com.ztgeo.general.biz.penghao.SysVersionBiz;
import com.ztgeo.general.mapper.penghao.SysVersionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysVersionBizImpl implements SysVersionBiz {

    @Autowired
    private SysVersionMapper sysVersionMapper;

    @Override
    public Object findSysVersion() {
        return sysVersionMapper.findSysVersion();
    }
}
