package com.ztgeo.general.biz.chenbin.impl;

import com.github.wxiaoqi.security.common.biz.BusinessBiz;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Service_Position;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.mapper.chenbin.PositSvrManagerMapper;
import com.ztgeo.general.util.chenbin.IDUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class PositSvrManagerBiz extends BusinessBiz<PositSvrManagerMapper, SJ_Power_Service_Position> {
    @Override
    public void insertSelective(SJ_Power_Service_Position entity) {
        entity.setPowerId(IDUtil.getPowerId());
        entity.setCreateBy(UserUtil.checkAndGetUser());
        entity.setCreateTime(new Date());
        super.insertSelective(entity);
        if(mapper.selectCountOfPositSvrSame(entity)>1){
            log.error("svrId:"+entity.getServiceId()+"positionId:"+entity.getPositionId()+ BizOrBizExceptionConstant.FOUND_SAME_POSITION_SVR_MSG);
            throw  new ZtgeoBizException(BizOrBizExceptionConstant.FOUND_SAME_POSITION_SVR_MSG);
        }
    }

    @Override
    public void updateSelectiveById(SJ_Power_Service_Position entity) {
        super.updateSelectiveById(entity);
        if(mapper.selectCountOfPositSvrSame(entity)>1){
            log.error("svrId:"+entity.getServiceId()+"positionId:"+entity.getPositionId()+ BizOrBizExceptionConstant.FOUND_SAME_POSITION_SVR_MSG);
            throw  new ZtgeoBizException(BizOrBizExceptionConstant.FOUND_SAME_POSITION_SVR_MSG);
        }
    }
}
