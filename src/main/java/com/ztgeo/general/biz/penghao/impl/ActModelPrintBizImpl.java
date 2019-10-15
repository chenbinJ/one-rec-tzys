package com.ztgeo.general.biz.penghao.impl;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.penghao.ActModelPrintBiz;
import com.ztgeo.general.entity.service_data.sys_data.ActModelPrint;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.mapper.penghao.ActModelPrintMapper;
import com.ztgeo.general.util.chenbin.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActModelPrintBizImpl implements ActModelPrintBiz {

    @Autowired
    private ActModelPrintMapper actModelPrintMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object insertModelPrint(ActModelPrint actModelPrint) {
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        actModelPrintMapper.deleteByModelId(actModelPrint.getModelId());
        actModelPrint.setId(IDUtil.getMdpId());
        if (actModelPrintMapper.insertSelective(actModelPrint) > 0) {
            rv.setMessage(MsgManager.OPERATING_ERROR);
        }
        rv.setMessage(MsgManager.OPERATING_SUCCESS);
        return rv;
    }

    @Override
    public Object selectByModelId(String modelId) {
        return actModelPrintMapper.selectByModelId(modelId);
    }
}
