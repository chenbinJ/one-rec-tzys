package com.ztgeo.general.biz;

import com.ace.cache.annotation.Cache;
import com.ace.cache.api.impl.CacheRedis;
import com.github.wxiaoqi.security.common.biz.BusinessBiz;
import com.ztgeo.general.entity.DicMain;
import com.ztgeo.general.mapper.DicMainMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典-字典主项表
 *
 * @author wei
 * @email 1205690873@qq.com
 * @version 2018-05-15 10:36:04
 */
@Service
public class DicMainBiz extends BusinessBiz<DicMainMapper,DicMain> {


    @Autowired
    private CacheRedis cacheRedis;
//    获取所有字典树的重写
    @Cache(key = "pubserver:main:all")
    @Override
    public List<DicMain> selectListAll() {
        return super.selectListAll();
    }

    @Override
    public void updateSelectiveById(DicMain entity) {
        super.updateSelectiveById(entity);
        cacheRedis.removeByPre("pubserver");
    }

    @Override
    public void insertSelective(DicMain entity) {
        super.insertSelective(entity);
        cacheRedis.removeByPre("pubserver");
    }

    @Override
    public void deleteById(Object id) {
        super.deleteById(id);
        cacheRedis.removeByPre("pubserver");
    }
}