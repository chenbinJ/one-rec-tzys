package com.ztgeo.general.controller.chenbin;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.chenbin.SvrManagerBiz;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Service_Interface;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Service;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.util.chenbin.ErrorDealUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/sysManager")
@Api(tags = {"服务及服务接口相关配置API"})
public class SvrManagerController {

    @Autowired
    private SvrManagerBiz svrBiz;

    //搜索服务
    @ApiOperation(value = "按条件搜索服务",
            notes = "按给出条件搜索服务，可以是服务名，title，code等")
    @RequestMapping(value = "/svrManager/searchSvr", method = RequestMethod.POST)
    public ObjectRestResponse<List<SJ_Service>> searchSvr(@RequestParam(value = "svrParam",required = false) String searchParam){
        log.info("SvrManager 搜索服务，搜索条件为："+searchParam);
        ObjectRestResponse<List<SJ_Service>> rv = new ObjectRestResponse<List<SJ_Service>>();
        return rv.data(svrBiz.searchSvr(searchParam));
    }

    //通过id查询服务
    @ApiOperation(value = "按id发现服务",
            notes = "按给出的服务id发现服务，精确查询")
    @RequestMapping(value = "/svrManager/findSvrById", method = RequestMethod.POST)
    public ObjectRestResponse<SJ_Service> findSvrById(@RequestParam(value = "svrId") String svrId){
        log.info("SvrManager find服务，传入主键为："+svrId);
        ObjectRestResponse<SJ_Service> rv = new ObjectRestResponse<SJ_Service>();
        return rv.data(svrBiz.findSvrById(svrId));
    }

    //查询有效的服务列表
    @ApiOperation(value = "查询当前启用的全部服务",
            notes = "仅仅按照服务状态进行查询")
    @RequestMapping(value = "/svrManager/findEnableSvrs", method = RequestMethod.POST)
    public ObjectRestResponse<List<SJ_Service>> findEnableSvrs(){
        log.info("SvrManager 查询全部可用服务");
        ObjectRestResponse<List<SJ_Service>> rv = new ObjectRestResponse<List<SJ_Service>>();
        return rv.data(svrBiz.findEnableSvrs());
    }

    //验证服务标识是否重复
    @ApiOperation(value = "验证服务标识",
            notes = "验证服务标识，防止服务标识重复")
    @RequestMapping(value = "/svrManager/checkSvrCode", method = RequestMethod.POST)
    public ObjectRestResponse<String> checkSvrCode(@RequestParam(value = "svrCode") String svrCode){
        log.info("用户："+"xxx"+"正在验证服务标识,传入服务标识为："+svrCode);
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(svrBiz.selectSameSvrCode(svrCode));
    }
    //验证服务标识是否可以被修改
    @ApiOperation(value = "验证服务标识是否可以被修改",
            notes = "验证服务标识是否可以被修改，防止办件服务挂接异常")
    @RequestMapping(value = "/svrManager/checkHavingSvrReceit", method = RequestMethod.POST)
    public ObjectRestResponse<String> checkHavingSvrReceit(@RequestParam(value = "svrId") String svrId){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(svrBiz.isHavingSvrReceit(svrId));
    }
    //添加服务
    @ApiOperation(value = "添加服务",
            notes = "添加新服务，传入服务实体类")
    @RequestMapping(value = "/svrManager/addSvr", method = RequestMethod.POST)
    public ObjectRestResponse<String> addSvr(@RequestBody SJ_Service sjSvr){
        log.info("用户："+"xxx"+"正在添加服务,服务名为："+sjSvr.getServiceName());
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        String result = "";
        try {
            result = svrBiz.addSvr(sjSvr);
        } catch (org.springframework.dao.DuplicateKeyException e){
            log.error(ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException(BizOrBizExceptionConstant.SERVICE_CODE_CAN_NOT_REPEAT);
        }
        return rv.data(result);
    }

    //修改服务
    @ApiOperation(value = "修改服务",
            notes = "修改老服务，传入服务实体类")
    @RequestMapping(value = "/svrManager/modifySvr", method = RequestMethod.POST)
    public ObjectRestResponse<String> modifySvr(@RequestBody SJ_Service sjSvr){
        log.info("用户："+"xxx"+"正在修改服务,新服务名为："+sjSvr.getServiceName());
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        String result = "";
        try{
            result = svrBiz.modifySvr(sjSvr);
        }catch (org.springframework.dao.DuplicateKeyException e){
            log.error(ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException(BizOrBizExceptionConstant.SERVICE_CODE_CAN_NOT_REPEAT);
        }
        return rv.data(result);
    }

    //废弃服务
    @ApiOperation(value = "废弃服务",
            notes = "废弃服务、服务接口权限、步骤服务权限等")
    @RequestMapping(value = "/svrManager/discardSvr", method = RequestMethod.POST)
    public ObjectRestResponse<String> discardSvr(@RequestParam(value = "svrId") String svrId){
        log.info("用户："+"xxx"+"废弃主键为"+svrId+"的服务");
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(svrBiz.discardSvr(svrId));
    }

    //启用服务
    @ApiOperation(value = "启用服务",
            notes = "启用服务、服务接口权限、步骤服务权限等")
    @RequestMapping(value = "/svrManager/enableSvr", method = RequestMethod.POST)
    public ObjectRestResponse<String> enableSvr(@RequestParam(value = "svrId") String svrId){
        log.info("用户："+"xxx"+"启用主键为"+svrId+"的服务");
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(svrBiz.enableSvr(svrId));
    }

    //删除服务
    @ApiOperation(value = "删除服务",
            notes = "删除服务、服务接口权限、步骤服务权限等，该步骤谨慎使用，删除后服务及相关配置将彻底销毁")
    @RequestMapping(value = "/svrManager/removeSvr", method = RequestMethod.POST)
    public ObjectRestResponse<String> removeSvr(@RequestParam(value = "svrId") String svrId){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(svrBiz.removeSvr(svrId));
    }

    /*
     * 提供有效的服务接口权限关系服务
     */
    //搜索服务接口权限列表
    @ApiOperation(value = "搜索服务接口列表",
            notes = "搜索服务接口列表，不定条件param，确定条件服务主键")
    @RequestMapping(value = "/svrIntfManager/searchSvrIntfPowers", method = RequestMethod.POST)
    public ObjectRestResponse<List<SJ_Power_Service_Interface>> searchSvrIntfPowers(
            @RequestParam(value = "svrIntfParam",required = false) String svrIntfParam,
            @RequestParam(value = "svrId") String svrId){
        ObjectRestResponse<List<SJ_Power_Service_Interface>> rv = new ObjectRestResponse<List<SJ_Power_Service_Interface>>();
        return rv.data(svrBiz.searchSvrIntfPowers(svrIntfParam,svrId));
    }

    //通过id查询服务接口权限详情
    @ApiOperation(value = "根据id服务接口权限",
            notes = "根据id精确查询服务接口权限")
    @RequestMapping(value = "/svrIntfManager/findSvrIntfById", method = RequestMethod.POST)
    public ObjectRestResponse<SJ_Power_Service_Interface> findSvrIntfById(@RequestParam(value = "svrIntfId") String svrIntfId){
        ObjectRestResponse<SJ_Power_Service_Interface> rv = new ObjectRestResponse<SJ_Power_Service_Interface>();
        return rv.data(svrBiz.findSvrIntfById(svrIntfId));
    }

    //添加服务接口权限
    @ApiOperation(value = "添加服务接口权限",
            notes = "添加服务接口权限")
    @RequestMapping(value = "/svrIntfManager/addSvrIntfPower", method = RequestMethod.POST)
    public ObjectRestResponse<String> addSvrIntfPower(@RequestBody SJ_Power_Service_Interface svrIntfPower){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(svrBiz.addSvrIntfPower(svrIntfPower));
    }

    //修改服务接口权限
    @ApiOperation(value = "修改服务接口权限",
            notes = "修改服务接口权限")
    @RequestMapping(value = "/svrIntfManager/modifySvrIntfPower", method = RequestMethod.POST)
    public ObjectRestResponse<String> modifySvrIntfPower(@RequestBody SJ_Power_Service_Interface svrIntfPower){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(svrBiz.modifySvrIntfPower(svrIntfPower));
    }

    //删除服务接口权限
    @ApiOperation(value = "删除服务接口权限",
            notes = "删除服务接口权限")
    @RequestMapping(value = "/svrIntfManager/removeSvrIntf", method = RequestMethod.POST)
    public ObjectRestResponse<String> removeSvrIntf(@RequestParam(value = "svrIntfId") String svrIntfId){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(svrBiz.removeSvrIntf(svrIntfId));
    }

}
