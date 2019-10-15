package com.ztgeo.general.biz.chenbin.impl;

import com.ztgeo.general.entity.Depart;
import com.ztgeo.general.mapper.chenbin.CommoMapper;
import com.ztgeo.general.util.chenbin.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("commonBiz")
public class CommonBiz {
    @Autowired
    private CommoMapper commoMapper;

    public Depart findBelongDepart() {
        String userId = UserUtil.getUerId();
        return commoMapper.selectBelongDepart(userId);
    }

    public Depart findSomeoneDepart(String preservationMan) {
        return commoMapper.selectBelongDepart(preservationMan);
    }
}
