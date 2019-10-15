package com.ztgeo.general.biz.chenbin.impl;

import com.ztgeo.general.biz.chenbin.SpacielPowerBiz;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Spaciel_Position_Power;
import com.ztgeo.general.mapper.chenbin.SJSpacielPowerMapper;
import com.ztgeo.general.util.chenbin.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("spacielBiz")
public class SpacielPowerBizImpl implements SpacielPowerBiz {
    @Autowired
    private SJSpacielPowerMapper sJSpacielPowerMapper;


    @Override
    public SJ_Spaciel_Position_Power findOne(String positionId) {
        return sJSpacielPowerMapper.selectPowerByPositionId(positionId);
    }

    @Override
    public String insertBody(String positionId) {
        SJ_Spaciel_Position_Power p = new SJ_Spaciel_Position_Power();
        p.setPowerId(IDUtil.getPowerId());
        p.setPositionId(positionId);
        sJSpacielPowerMapper.insertPower(p);
        return "授权成功";
    }

    @Override
    public String deleteOne(String positionId) {
        sJSpacielPowerMapper.deletePower(positionId);
        return "取消授权成功";
    }
}
