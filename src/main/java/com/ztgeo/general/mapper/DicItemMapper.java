package com.ztgeo.general.mapper;

import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.ztgeo.general.entity.DicItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典-字典字项表
 * 
 * @author wei
 * @email 1205690873@qq.com
 * @version 2018-05-15 10:36:04
 */
public interface DicItemMapper extends CommonMapper<DicItem> {

    //获取阅读须知
    DicItem getDicWithPandC(@Param("parParam") String parParam, @Param("childParam") String childParam);

    List<DicItem> getDicWithP(@Param("parParam") String parParam);

    //根据diccody获取字典列表
    List<DicItem> getDicWithDicCode(@Param("dicCode") String dicCode);
}
