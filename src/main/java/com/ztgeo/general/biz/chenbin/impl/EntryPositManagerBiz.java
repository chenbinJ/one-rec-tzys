package com.ztgeo.general.biz.chenbin.impl;

import com.github.wxiaoqi.security.common.biz.BusinessBiz;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Fjtm_Position;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.mapper.chenbin.EntryPositManagerMapper;
import com.ztgeo.general.util.chenbin.IDUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class EntryPositManagerBiz extends BusinessBiz<EntryPositManagerMapper, SJ_Power_Fjtm_Position> {
    @Override
    public void insertSelective(SJ_Power_Fjtm_Position entity) {
        entity.setPowerId(IDUtil.getPowerId());
        entity.setCreateBy(UserUtil.checkAndGetUser());
        entity.setCreateTime(new Date());
        super.insertSelective(entity);
        if (mapper.selectCountOfEntryPositSame(entity) > 1) {
            log.error("entryId:" + entity.getEntryId() + "positionId:" + entity.getPositionId() + BizOrBizExceptionConstant.FOUND_SAME_ENTRY_POSITION_MSG);
            throw new ZtgeoBizException(BizOrBizExceptionConstant.FOUND_SAME_ENTRY_POSITION_MSG);
        }
    }

    @Override
    public void updateSelectiveById(SJ_Power_Fjtm_Position entity) {
        super.updateSelectiveById(entity);
        if (mapper.selectCountOfEntryPositSame(entity) > 1) {
            log.error("entryId:" + entity.getEntryId() + "positionId:" + entity.getPositionId() + BizOrBizExceptionConstant.FOUND_SAME_ENTRY_POSITION_MSG);
            throw new ZtgeoBizException(BizOrBizExceptionConstant.FOUND_SAME_ENTRY_POSITION_MSG);
        }
    }
}
