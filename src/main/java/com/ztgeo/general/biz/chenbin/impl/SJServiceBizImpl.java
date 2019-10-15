package com.ztgeo.general.biz.chenbin.impl;

import com.github.wxiaoqi.security.common.util.BooleanUtil;
import com.ztgeo.general.biz.chenbin.SJServiceBiz;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Service;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.mapper.UserMapper;
import com.ztgeo.general.mapper.chenbin.SJServiceMapper;
import com.ztgeo.general.util.chenbin.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = RuntimeException.class)
public class SJServiceBizImpl implements SJServiceBiz {

    @Autowired
    private SJServiceMapper sjServiceMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Object findSjServiceByUid(String uid) {
        if (BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(uid).getIsSuperAdmin())) {
            //管理员查全部
            return sjServiceMapper.findAllSjServices();
        } else {
            return sjServiceMapper.findSjServiceByUid(uid);
        }
    }


}
