package com.ztgeo.general.biz.chenbin.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.ag.core.context.BaseContextHandler;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ztgeo.general.biz.chenbin.IntfManagerBiz;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.service_data.page.PageResponseBean;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Interface;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Interface_Params;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.mapper.chenbin.IntfManagerMapper;
import com.ztgeo.general.mapper.chenbin.StepManagerMapper;
import com.ztgeo.general.mapper.chenbin.SvrManagerMapper;
import com.ztgeo.general.util.chenbin.IDUtil;
import com.ztgeo.general.util.chenbin.MyQuery;
import com.ztgeo.general.util.chenbin.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("intfBiz")
@Transactional(rollbackFor = Exception.class)
public class IntfManagerBizImpl implements IntfManagerBiz {

    @Autowired
    private IntfManagerMapper intfManagerMapper;
    @Autowired
    private SvrManagerMapper svrManagerMapper;
    @Autowired
    private StepManagerMapper stepManagerMapper;

    @Override
    public PageResponseBean<SJ_Interface> searchIntfs(Map<String,Object> params) {
        MyQuery query = new MyQuery(params);
        String intfParam = query.getSearchParam();
        String status = (String) query.get("status");
        int pageNum = query.getPage();
        int pageSize = query.getLimit();
        PageHelper.startPage(pageNum,pageSize);
        Page<SJ_Interface> ints = intfManagerMapper.selectIntfsByPage(intfParam,status);
//        long tt = 0;
//        if(pageSize>0){
//            tt = MyMathUtil.getCeil(ints.getTotal() , pageSize);
//        }
        PageResponseBean<SJ_Interface> pageResult = new PageResponseBean<SJ_Interface>(ints,ints.getPageSize(),ints.getPageNum(),ints.getTotal());
        return pageResult;
    }

    @Override
    public SJ_Interface findIntfById(String intfId) {
        SJ_Interface intf = intfManagerMapper.selectIntfById(intfId);
        if(intf == null){
            log.error("通过ID查询接口详情信息时，传入接口ID为无效Id");
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_NO_MATCH_MSG);
        }



        return intf;
    }

    @Override
    public List<SJ_Interface> findAllEnableIntfs() {
        return intfManagerMapper.selectAllEnableIntfs();
    }

    @Override
    public String addIntf(SJ_Interface sjIntf) {
        sjIntf.setInterfaceId(IDUtil.getIntfId());
        sjIntf.setCreateBy(UserUtil.checkAndGetUser());
        sjIntf.setCreateDate(new Date());
        int count = intfManagerMapper.insertIntf(sjIntf);
        if(count!=1){
            log.error("接口数据写入异常");
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_INSERT_NOT_SUCCESS_MSG);
        }
        return "创建成功";
    }

    @Override
    public String modifyIntf(SJ_Interface sjIntf) {
        int count = intfManagerMapper.updateIntf(sjIntf);
        if(count!=1){
            log.error("接口数据更新异常");
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_UPDATE_NOT_SUCCESS_MSG);
        }
        return "保存成功";
    }

    @Override
    public String discardIntf(String intfId) {
        int count = intfManagerMapper.updateIntfStatus("禁用",intfId);
        if(count != 1){
            log.error("接口禁用失败");
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_DISCARD_NOT_SUCCESS_MSG);
        }
        return "接口禁用成功";
    }

    @Override
    public String enableIntf(String intfId) {
        int count = intfManagerMapper.updateIntfStatus("可用",intfId);
        if(count != 1){
            log.error("接口启用失败");
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_ENABLE_NOT_SUCCESS_MSG);
        }
        return "接口启用成功";
    }

    @Override
    public String removeIntf(String intfId) {
        int count = intfManagerMapper.deleteIntfById(intfId);
        if(count != 1){
            log.error("接口删除失败");
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_DELETE_NOT_SUCCESS_MSG);
        }
        intfManagerMapper.deleteIntfParams(intfId,null);
        svrManagerMapper.deleteSvrIntfByParam(null,null,intfId);
        stepManagerMapper.deleteStepIntfByParam(null,null,intfId);
        return "删除成功";
    }

    @Override
    public String addOrMergeParams(String paramList,String intf) {
        //配置接口参数
        SJ_Interface sjIntf = JSON.parseObject(intf,SJ_Interface.class);
        if(sjIntf.getReqParamClass()!=null && sjIntf.getReqParamClass().length()>0) {
            List<SJ_Interface_Params> params = JSONArray.parseArray(paramList, SJ_Interface_Params.class);
            System.out.println(intf);
            log.info("用户：" + BaseContextHandler.getUsername() + "正在重新配置接口 “" + sjIntf.getInterfaceId() + "” 的参数信息：期望的接口参数类型为：“" + sjIntf.getReqParamClass() + "”");
            //删除原参数数据
            intfManagerMapper.deleteIntfParams(sjIntf.getInterfaceId(), null);
            int i = 1;
            //新增现参数数据
            for (SJ_Interface_Params param : params) {
                param.setParamId(IDUtil.getIntfParamId());
                param.setInterfaceId(sjIntf.getInterfaceId());
                param.setOrderNumber(new Integer(i));
                intfManagerMapper.insertIntfParam(param);
                i++;
            }
        }else{
            //删除原参数数据
            intfManagerMapper.deleteIntfParams(sjIntf.getInterfaceId(), null);
        }
        //更新接口参数类型数据
        intfManagerMapper.updateReqParam(sjIntf);
        return "接口参数配置成功";
    }

    @Override
    public String modifyParam(SJ_Interface_Params param) {
        int count = intfManagerMapper.updateIntfParam(param);
        if(count!=1){
            log.error("接口参数更新失败");
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_UPDATE_NOT_SUCCESS_MSG);
        }
        return "保存成功";
    }

    @Override
    public List<SJ_Interface_Params> getIntfParams(String intfId) {
        return intfManagerMapper.selectParamsByIntfid(intfId);
    }

    @Override
    public String discardParam(String paramId) {
        int count = intfManagerMapper.updateIntfParamStatus("禁用",null,paramId);
        if(count!=1){
            log.error("接口参数禁用失败");
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_DISCARD_NOT_SUCCESS_MSG);
        }
        return "参数禁用成功";
    }

    @Override
    public String enableParam(String paramId) {
        int count = intfManagerMapper.updateIntfParamStatus("可用",null,paramId);
        if(count!=1){
            log.error("接口参数启用失败");
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_ENABLE_NOT_SUCCESS_MSG);
        }
        return "参数启用成功";
    }

    @Override
    public SJ_Interface_Params getParamById(String paramId) {
        return intfManagerMapper.selectSjIntfParamByParamid(paramId);
    }
}
