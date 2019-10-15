package com.ztgeo.general.biz;

import com.ace.cache.annotation.Cache;
import com.github.wxiaoqi.security.common.biz.BusinessBiz;
import com.ztgeo.general.entity.DicUsrdefField;
import com.ztgeo.general.mapper.DicUsrdefFieldMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 自定义字典字段定义表
 *
 * @author wei
 * @email 1205690873@qq.com
 * @version 2018-05-15 10:36:04
 */
@Service
public class DicUsrdefFieldBiz extends BusinessBiz<DicUsrdefFieldMapper,DicUsrdefField> {

    //目前没有通过接口去修改field的接口 所以只有保存的 之后如果拓展了字段的修改api 这里需要加上 切记（算不算挖坑？）
    @Cache(key = "pubserver:字段集合{1.dicCode}")
    @Override
    public List<DicUsrdefField> selectList(DicUsrdefField entity) {
        return super.selectList(entity);
    }
}