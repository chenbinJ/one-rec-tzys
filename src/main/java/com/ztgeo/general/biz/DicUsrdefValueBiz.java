package com.ztgeo.general.biz;

import com.ace.cache.annotation.Cache;
import com.ace.cache.api.impl.CacheRedis;
import com.github.wxiaoqi.security.common.biz.BusinessBiz;
import com.ztgeo.general.entity.DicUsrdefValue;
import com.ztgeo.general.mapper.DicUsrdefValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 自定义字典存储表
 *
 * @author wei
 * @email 1205690873@qq.com
 * @version 2018-05-15 10:36:04
 */
@Service
public class DicUsrdefValueBiz extends BusinessBiz<DicUsrdefValueMapper,DicUsrdefValue> {

    @Autowired
    private CacheRedis cacheRedis;
    private static final String PIXCACHE="pubserver:";


    //获取模型树
    @Cache(key = "pubserver:自定义树形字典{1}")
    public List<DicUsrdefValue> getModlue(String dicCode) {
        return mapper.getModule(dicCode);
    }


    @Override
    public void updateSelectiveById(DicUsrdefValue entity) {
        super.updateSelectiveById(entity);
        cacheRedis.removeByPre("pubserver");
    }

    @Override
    public void insertSelective(DicUsrdefValue entity) {
        super.insertSelective(entity);
        cacheRedis.removeByPre("pubserver");
    }

    @Override
    public void deleteById(Object id) {
        super.deleteById(id);
        cacheRedis.removeByPre("pubserver");
    }
}