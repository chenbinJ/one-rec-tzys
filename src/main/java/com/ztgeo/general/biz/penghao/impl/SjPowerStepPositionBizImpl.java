package com.ztgeo.general.biz.penghao.impl;

import com.ztgeo.general.biz.penghao.SjPowerStepPositionBiz;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Step_Position;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.mapper.penghao.SjPowerStepPositionMapper;
import com.ztgeo.general.util.chenbin.IDUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class SjPowerStepPositionBizImpl implements SjPowerStepPositionBiz {

    @Autowired
    private SjPowerStepPositionMapper sjPowerStepPositionMapper;

    @Override
    public Object getPositionByStepId(String stepId) {
        return sjPowerStepPositionMapper.getPositionByStepId(stepId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object insetPositionList(List<SJ_Power_Step_Position> list, String stepId) {
        //删除step相关数据
        sjPowerStepPositionMapper.deleteByStepId(stepId);
        //新增
        for (SJ_Power_Step_Position stepPosition : list) {
            stepPosition.setCreateBy(UserUtil.checkAndGetUser());
            stepPosition.setCreateTime(new Date());
            stepPosition.setPowerId(IDUtil.getPowerId());
        }
        sjPowerStepPositionMapper.insertList(list);
        return MsgManager.OPERATING_SUCCESS;
    }
}
