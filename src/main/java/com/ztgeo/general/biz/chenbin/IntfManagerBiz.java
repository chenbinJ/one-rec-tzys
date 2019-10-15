package com.ztgeo.general.biz.chenbin;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.entity.service_data.page.PageResponseBean;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Interface;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Interface_Params;

import java.util.List;
import java.util.Map;

public interface IntfManagerBiz {
    public PageResponseBean<SJ_Interface> searchIntfs(Map<String, Object> params);//搜索接口

    public SJ_Interface findIntfById(String intfId);//按id搜索接口详情

    public List<SJ_Interface> findAllEnableIntfs();//查询全部可用接口

    public String addIntf(SJ_Interface sjIntf);//添加接口

    public String modifyIntf(SJ_Interface sjIntf);//修改接口

    public String discardIntf(String intfId);//废弃接口

    public String enableIntf(String intfId);//废弃接口

    public String removeIntf(String intfId);//删除接口

    public String addOrMergeParams(String paramList, String intf);//给接口添加有效参数

    public String modifyParam(SJ_Interface_Params param);//修改接口参数信息

    public List<SJ_Interface_Params> getIntfParams(String intfId);//获取接口全部参数信息

    public String discardParam(String paramId);//废弃接口参数

    public String enableParam(String paramId);

    public SJ_Interface_Params getParamById(String paramId);//通过参数id获取接口参数详情
}
