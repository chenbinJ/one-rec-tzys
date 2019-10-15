package com.ztgeo.general.biz.chenbin.impl;

import com.github.wxiaoqi.security.common.biz.BusinessBiz;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjtm;
import com.ztgeo.general.mapper.chenbin.EntryManagerMapper;
import com.ztgeo.general.mapper.chenbin.EntryPositManagerMapper;
import com.ztgeo.general.util.chenbin.IDUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class EntryManagerBiz extends BusinessBiz<EntryManagerMapper, SJ_Fjtm> {
    @Autowired
    private EntryPositManagerMapper entryPositManagerMapper;

    @Override
    public void insertSelective(SJ_Fjtm entity) {
        entity.setEntryId(IDUtil.getEntryId());
        entity.setCreateBy(UserUtil.checkAndGetUser());
        entity.setCreateTime(new Date());
        entity.setLastUpdate(new Date());
        super.insertSelective(entity);
    }

    @Override
    public void updateById(SJ_Fjtm entity) {
        entity.setLastUpdate(new Date());
        super.updateById(entity);
    }

    @Override
    public void deleteById(Object id) {
        entryPositManagerMapper.deleteByParam(null, id.toString());//删除岗位条目操作权限
        super.deleteById(id);//删除条目
    }

    public String removeByProcId(String processId) {
        SJ_Fjtm fjtm = new SJ_Fjtm();
        fjtm.setProcessMouldId(processId);
        List<SJ_Fjtm> fjtms = mapper.select(fjtm);
        for (SJ_Fjtm fj : fjtms) {
            entryPositManagerMapper.deleteByParam(null, fj.getEntryId());//删除岗位条目操作权限
            super.deleteById(fj.getEntryId());//删除条目
        }
        return "删除成功";
    }
}
