package com.ztgeo.general.biz.penghao.impl;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.penghao.TemplateCategoryBiz;
import com.ztgeo.general.entity.service_data.pub_data.TemplateCategory;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.mapper.penghao.TemplateCategoryMapper;
import com.ztgeo.general.util.chenbin.IDUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class TemplateCategoryBizImpl implements TemplateCategoryBiz {

    @Autowired
    private TemplateCategoryMapper templateCategoryMapper;


    @Override
    public Object findTemplateByModel(String mid) {
        return templateCategoryMapper.findTemplateByModel(mid);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object insertTemplate(TemplateCategory templateCategory) {
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        templateCategoryMapper.deleteByModel(templateCategory.getTemplateModel());
        rv.setMessage(MsgManager.OPERATING_SUCCESS);
        if (StringUtils.isEmpty(templateCategory.getTemplateLarge()) || StringUtils.isEmpty(templateCategory.getTemplateLargeName())) {
            return rv;
        }
        templateCategory.setTemplateCreateby(UserUtil.checkAndGetUser());
        templateCategory.setTemplateCreatetime(new Date());
        templateCategory.setTemplateId(IDUtil.getTemplateId());
        templateCategoryMapper.insertSelective(templateCategory);
        return rv;
    }


}
