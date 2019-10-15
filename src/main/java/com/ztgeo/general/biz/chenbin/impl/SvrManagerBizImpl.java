package com.ztgeo.general.biz.chenbin.impl;

import com.ztgeo.general.biz.chenbin.SvrManagerBiz;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Service_Interface;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Service;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.mapper.chenbin.PositSvrManagerMapper;
import com.ztgeo.general.mapper.chenbin.SJInfoManagerMapper;
import com.ztgeo.general.mapper.chenbin.StepManagerMapper;
import com.ztgeo.general.mapper.chenbin.SvrManagerMapper;
import com.ztgeo.general.util.chenbin.IDUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service("svrBiz")
@Transactional(rollbackFor = Exception.class)
public class SvrManagerBizImpl implements SvrManagerBiz {

    @Autowired
    private SvrManagerMapper svrManagerMapper;
    @Autowired
    private StepManagerMapper stepManagerMapper;
    @Autowired
    private SJInfoManagerMapper sJInfoManagerMapper;
    @Autowired
    private PositSvrManagerMapper positSvrManagerMapper;

    @Override
    public List<SJ_Service> searchSvr(String searchParam) {
        return svrManagerMapper.selectSvr(searchParam,null);
    }

    @Override
    public List<SJ_Service> findEnableSvrs() {
        return svrManagerMapper.selectSvr(null,"可用");
    }

    @Override
    public SJ_Service findSvrById(String svrId) {
        SJ_Service svr = svrManagerMapper.selectSvrById(svrId);
        if(svr == null){
            log.error(BizOrBizExceptionConstant.DATA_NO_MATCH_MSG);
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_NO_MATCH_MSG);
        }
        return svr;
    }

    @Override
    public String selectSameSvrCode(String svrCode) {
        if(svrManagerMapper.selectCountOfSvrCode(svrCode)>0){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.SERVICE_CODE_CAN_NOT_REPEAT);
        }
        return "success";
    }

    @Override
    public String isHavingSvrReceit(String svrId) {
        if(sJInfoManagerMapper.selectCountOfSvr(svrId)>0){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.SET_CAN_NOT_DELETE_MSG);
        }
        return "success";
    }


    @Override
    public String addSvr(SJ_Service sjSvr) {
        sjSvr.setServiceId(IDUtil.getSvrId());
        sjSvr.setCreateTime(new Date());
        String username = UserUtil.checkAndGetUser();
        sjSvr.setCreateBy(username);
        Integer countAffected = svrManagerMapper.insertSvr(sjSvr);
        if(countAffected==null || countAffected!=1){
            log.error(BizOrBizExceptionConstant.DATA_NO_MATCH_MSG);
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_NO_MATCH_MSG);
        }
        return "添加成功";
    }

    @Override
    public String modifySvr(SJ_Service sjSvr) {
        //是否允许更改ServiceCode
        Integer sm = sJInfoManagerMapper.selectCountOfSvr(sjSvr.getServiceId());
        Integer sm_code = svrManagerMapper.selectCountOfSvrCode(sjSvr.getServiceCode());
        if(sm>0 && sm_code<=0){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.SET_CAN_NOT_DELETE_MSG);
        }
        Integer countAffected = svrManagerMapper.updateSvr(sjSvr);
        if(countAffected==null || countAffected!=1){
            log.error(BizOrBizExceptionConstant.DATA_NO_MATCH_MSG);
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_NO_MATCH_MSG);
        }
        return "保存成功";
    }

    @Override
    public String discardSvr(String svrId) {
        svrManagerMapper.updateSvrStatus("禁用",svrId);
        return "服务禁用成功";
    }

    @Override
    public String enableSvr(String svrId) {
        svrManagerMapper.updateSvrStatus("可用",svrId);
        return "服务启用成功";
    }

    @Override
    public String removeSvr(String svrId) {
        Integer sm = sJInfoManagerMapper.selectCountOfSvr(svrId);
        log.info("查询到的服务办件数据目为："+ sm);
        if(sm>0){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.SET_CAN_NOT_DELETE_MSG);
        }
        svrManagerMapper.deleteSvrById(svrId);
        svrManagerMapper.deleteSvrIntfByParam(null,svrId,null);
        stepManagerMapper.deleteStepSvrByParam(null,null,svrId);
        positSvrManagerMapper.deletePositSvrByParam(svrId,null);
        return "服务删除成功";
    }

    @Override
    public List<SJ_Power_Service_Interface> searchSvrIntfPowers(String svrIntfParam, String svrId) {
        return svrManagerMapper.selectSvrIntf(svrId,svrIntfParam);
    }

    @Override
    public SJ_Power_Service_Interface findSvrIntfById(String svrIntfId) {
        SJ_Power_Service_Interface svrIntfPower = svrManagerMapper.selectSvrIntfById(svrIntfId);
        if(svrIntfPower==null){
            log.error(BizOrBizExceptionConstant.DATA_NO_MATCH_MSG);
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_NO_MATCH_MSG);
        }
        return svrIntfPower;
    }

    @Override
    public String addSvrIntfPower(SJ_Power_Service_Interface svrIntfPower) {
        if(svrIntfPower.getServiceId()==null||svrIntfPower.getServiceId().length()<=0){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_PARAM_NULL_ERROR_MSG);
        }
        svrIntfPower.setPowerId(IDUtil.getPowerId());
        svrIntfPower.setCreateBy(UserUtil.checkAndGetUser());
        svrIntfPower.setCreateTime(new Date());
        svrManagerMapper.insertSvrIntf(svrIntfPower);
        //验证添加是否产生重复
        if(svrManagerMapper.selectCountOfSvrIntfSame(svrIntfPower)>1){
            log.error("svrId:"+svrIntfPower.getServiceId()+"intfId:"+svrIntfPower.getInterfaceId()+BizOrBizExceptionConstant.FOUND_SAME_SVR_INTF_MSG);
            throw new ZtgeoBizException(BizOrBizExceptionConstant.FOUND_SAME_SVR_INTF_MSG);
        }
        return "服务接口权限添加成功";
    }

    @Override
    public String modifySvrIntfPower(SJ_Power_Service_Interface svrIntfPower) {
        if(svrIntfPower.getInterfaceId()==null||svrIntfPower.getInterfaceId().length()<=0){
            log.error("更新服务接口权限信息存在一个必填字段就是关联接口id，此次调用并没有给出");
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_PARAM_NULL_ERROR_MSG);
        }
        int count = svrManagerMapper.updateSvrIntf(svrIntfPower);
        if(count!=1){
            log.error("更新服务接口权限信息时产生了一个异常，更新条目不为1，请检查访问是否正常方式进入");
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_NO_MATCH_MSG);
        }
        //验证修改是否产生重复
        if(svrManagerMapper.selectCountOfSvrIntfSame(svrIntfPower)>1){
            log.error("svrId:"+svrIntfPower.getServiceId()+"intfId:"+svrIntfPower.getInterfaceId()+BizOrBizExceptionConstant.FOUND_SAME_SVR_INTF_MSG);
            throw new ZtgeoBizException(BizOrBizExceptionConstant.FOUND_SAME_SVR_INTF_MSG);
        }
        return "服务接口权限修改成功";
    }

    @Override
    public String removeSvrIntf(String svrIntfId) {
        svrManagerMapper.deleteSvrIntfByParam(svrIntfId,null,null);
        return "服务接口权限删除成功";
    }
}
