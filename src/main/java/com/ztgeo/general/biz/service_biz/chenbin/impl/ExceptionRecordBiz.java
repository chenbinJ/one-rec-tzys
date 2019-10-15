package com.ztgeo.general.biz.service_biz.chenbin.impl;

import com.github.wxiaoqi.security.common.biz.BusinessBiz;
import com.github.wxiaoqi.security.common.util.BooleanUtil;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Exception_Record;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.mapper.UserMapper;
import com.ztgeo.general.mapper.chenbin.ExceptionRecordMapper;
import com.ztgeo.general.util.chenbin.IDUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ExceptionRecordBiz extends BusinessBiz<ExceptionRecordMapper, SJ_Exception_Record> {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW , isolation = Isolation.DEFAULT
            ,rollbackFor = RuntimeException.class)
    public void insertSelective(SJ_Exception_Record entity) {
        entity.setId(IDUtil.getExceptionId());
        super.insertSelective(entity);
    }

    @Override
    public SJ_Exception_Record selectById(Object id) {
        if(!BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(UserUtil.getUerId()).getIsSuperAdmin())){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.PROCESS_HANDLE_SUB_NOT_ENOUGH_POWER);
        }
        return super.selectById(id);
    }

    @Override
    public void deleteById(Object id) {
        if(!BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(UserUtil.getUerId()).getIsSuperAdmin())){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.PROCESS_HANDLE_SUB_NOT_ENOUGH_POWER);
        }
        super.deleteById(id);
    }
}
