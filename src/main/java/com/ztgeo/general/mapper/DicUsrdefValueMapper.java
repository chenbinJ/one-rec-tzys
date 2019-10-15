package com.ztgeo.general.mapper;

import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.ztgeo.general.entity.DicUsrdefValue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 自定义字典存储表
 * 
 * @author wei
 * @email 1205690873@qq.com
 * @version 2018-05-15 10:36:04
 */
public interface DicUsrdefValueMapper extends CommonMapper<DicUsrdefValue> {


    //获取模型树
    List<DicUsrdefValue> getModule(String dicCode);


}
