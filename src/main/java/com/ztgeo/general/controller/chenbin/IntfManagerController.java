package com.ztgeo.general.controller.chenbin;

import com.github.ag.core.context.BaseContextHandler;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.chenbin.IntfManagerBiz;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.service_data.page.PageResponseBean;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Interface;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Interface_Params;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.util.chenbin.ErrorDealUtil;
import com.ztgeo.general.util.chenbin.PackageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/sysManager")
@Api(tags = {"接口及接口参数相关配置API"})
public class IntfManagerController {
    @Autowired
    private IntfManagerBiz intfBiz;

    //搜索接口
    @ApiOperation(value = "按条件搜索接口",
            notes = "按给出条件搜索接口，可以是接口名，code，使用说明（模糊查询）等")
    @RequestMapping(value = "/intfManager/searchIntfs", method = RequestMethod.POST)
    public ObjectRestResponse<PageResponseBean<SJ_Interface>> searchIntfs(@RequestBody Map<String, Object> params){
        ObjectRestResponse<PageResponseBean<SJ_Interface>> rv = new ObjectRestResponse<PageResponseBean<SJ_Interface>>();
        return rv.data(intfBiz.searchIntfs(params));
    }

    //通过id查询接口
    @ApiOperation(value = "id精确查询接口",
            notes = "按给出接口id精确查询接口，条件必须为接口id")
    @ApiImplicitParam(name = "intfId", value = "接口id", dataType = "string", paramType = "query", example = "example-interface-id-0001")
    @RequestMapping(value = "/intfManager/findIntfById", method = RequestMethod.POST)
    public ObjectRestResponse<SJ_Interface> findIntfById(@RequestParam(value = "intfId") String intfId){
        ObjectRestResponse<SJ_Interface> rv = new ObjectRestResponse<SJ_Interface>();
        return rv.data(intfBiz.findIntfById(intfId));
    }

    //查询全部可用接口
    @ApiOperation(value = "查询全部可用接口",
            notes = "为其他系统配置提供可供选择的接口")
    @RequestMapping(value = "/intfManager/findAllEnableIntfs", method = RequestMethod.POST)
    public ObjectRestResponse<List<SJ_Interface>> findAllEnableIntfs(){
        ObjectRestResponse<List<SJ_Interface>> rv = new ObjectRestResponse<List<SJ_Interface>>();
        return rv.data(intfBiz.findAllEnableIntfs());
    }

    //添加接口
    @ApiOperation(value = "添加新接口",
            notes = "新接口入库")
    @RequestMapping(value = "/intfManager/addIntf", method = RequestMethod.POST)
    public ObjectRestResponse<String> addIntf(@RequestBody SJ_Interface sjIntf){
        log.info("用户："+ BaseContextHandler.getUsername() +"正在添加接口,接口名为："+sjIntf.getInterfaceName());
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        String result = "";
        try {
            result = intfBiz.addIntf(sjIntf);
        } catch (org.springframework.dao.DuplicateKeyException e){
            log.error(ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException(BizOrBizExceptionConstant.INTERFACE_CODE_CAN_NOT_REPEAT);
        }
        return rv.data(result);
    }

    //修改接口
    @ApiOperation(value = "修改接口",
            notes = "旧接口信息修改,不对接口状态进行修改")
    @RequestMapping(value = "/intfManager/modifyIntf", method = RequestMethod.POST)
    public ObjectRestResponse<String> modifyIntf(@RequestBody SJ_Interface sjIntf){
        log.info("用户："+ BaseContextHandler.getUsername() +"修改了接口,接口名为："+sjIntf.getInterfaceName());
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        String result = "";
        try {
            result = intfBiz.modifyIntf(sjIntf);
        } catch (org.springframework.dao.DuplicateKeyException e){
            log.error(ErrorDealUtil.getErrorInfo(e));
            throw new ZtgeoBizException(BizOrBizExceptionConstant.INTERFACE_CODE_CAN_NOT_REPEAT);
        }
        return rv.data(result);
    }

    //废弃接口
    @ApiOperation(value = "废弃接口",
            notes = "按照给出的接口id修改接口状态，停止正在使用的接口")
    @ApiImplicitParam(name = "intfId", value = "接口id", dataType = "string", paramType = "query", example = "example-interface-id-0001")
    @RequestMapping(value = "/intfManager/discardIntf", method = RequestMethod.POST)
    public ObjectRestResponse<String> discardIntf(@RequestParam(value = "intfId") String intfId){
        log.info("用户："+ BaseContextHandler.getUsername() +"禁用了接口："+intfId+";同时接口相关权限将被禁用");
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(intfBiz.discardIntf(intfId));
    }

    //启用接口
    @ApiOperation(value = "启用接口",
            notes = "按照给出的接口id修改接口状态，启用已经禁用的接口")
    @ApiImplicitParam(name = "intfId", value = "接口id", dataType = "string", paramType = "query", example = "example-interface-id-0001")
    @RequestMapping(value = "/intfManager/enableIntf", method = RequestMethod.POST)
    public ObjectRestResponse<String> enableIntf(@RequestParam(value = "intfId") String intfId){
        log.info("用户："+ BaseContextHandler.getUsername() +"启用了接口："+intfId+";同时接口相关权限将被启用");
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(intfBiz.enableIntf(intfId));
    }

    //删除接口
    @ApiOperation(value = "删除接口",
            notes = "按照给出的接口id直接删除接口配置，慎用！")
    @ApiImplicitParam(name = "intfId", value = "接口id", dataType = "string", paramType = "query", example = "example-interface-id-0001")
    @RequestMapping(value = "/intfManager/removeIntf", method = RequestMethod.POST)
    public ObjectRestResponse<String> removeIntf(@RequestParam(value = "intfId") String intfId){
        log.info("用户："+ BaseContextHandler.getUsername() +"删除了接口："+intfId+";同时接口相关权限将被删除");
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        //三个相关联的都要删除
        return rv.data(intfBiz.removeIntf(intfId));
    }

    //获取接口参数支持的类结果集合（全部）
    @ApiOperation(value = "获取接口参数的类型信息",
            notes = "按照给出的搜索条件搜索接口可配置的参数类型，条件可以是参数类名中的某部分字符串，为空时查全部")
    @ApiImplicitParam(name = "searchSign", value = "参数搜索条件", dataType = "string", paramType = "query", example = "HT")
    @RequestMapping(value = "/intfParamsManager/searchParams", method = RequestMethod.POST)
    public ObjectRestResponse<List<Map<String,String>>> searchParams(@RequestParam(value = "searchSign",required = false) String searchSign){
        ObjectRestResponse<List<Map<String,String>>> rv = new ObjectRestResponse<List<Map<String,String>>>();
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        try {
            PackageUtil.getClasses(BizOrBizExceptionConstant.INTERFACE_PACKAGE_URL, searchSign, list);
            rv.setData(list);
        } catch (IOException ioe){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.PACKAGE_URL_ERROR_MSG);
        } catch (ClassNotFoundException cfe){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.PACKAGE_CLASSNOTFOUND_ERROR_MSG);
        }
        return rv;
    }

    //获取接口参数实体类中的全部属性集合
    @ApiOperation(value = "获取接口参数类型包含的类属性信息",
            notes = "查询条件只能是选择的参数类完整类名（包括包名）")
    @ApiImplicitParam(name = "paramClassName", value = "参数类名", dataType = "string", paramType = "query",
            example = "com.ztgeo.general.entity.service_data.intface_param.BankDYHTParamBodyEntity")
    @RequestMapping(value = "/intfParamsManager/getParamFeilds", method = RequestMethod.POST)
    public ObjectRestResponse<List<Map<String,String>>> getParamFeilds(@RequestParam(value = "paramClassName",required = false) String paramClassName){
        ObjectRestResponse<List<Map<String,String>>> rv = new ObjectRestResponse<List<Map<String,String>>>();
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        try {
            List<Map<String,String>> feilds = PackageUtil.getFiledName(paramClassName);
            rv.setData(feilds);
        } catch (ClassNotFoundException cfe){
            throw new ZtgeoBizException(BizOrBizExceptionConstant.PACKAGE_CLASSNOTFOUND_ERROR_MSG);
        }
        return rv;
    }

    //添加接口参数信息
    @ApiOperation(value = "添加接口参数属性信息入库",
            notes = "条件为参数对象集合的JSON格式字符串")
    @RequestMapping(value = "/intfParamsManager/addParams", method = RequestMethod.POST)
    public ObjectRestResponse<String> addOrMergeParams(@RequestParam(value = "paramsList") String paramList,@RequestParam(value = "intf") String intf){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        //先删除旧参数再添加新参数,同步更新接口表paramClass信息
        return rv.data(intfBiz.addOrMergeParams(paramList,intf));
    }

    //修改接口参数信息
    @ApiOperation(value = "修改接口参数属性信息入库",
            notes = "条件为‘参数’对象，支持修改的项为param_name，param_type，order_number，status，ext1，ext2等")
    @RequestMapping(value = "/intfParamsManager/modifyParam", method = RequestMethod.POST)
    public ObjectRestResponse<String> modifyParam(@RequestBody SJ_Interface_Params param){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        log.info("用户："+ BaseContextHandler.getUsername() +"修改了接口参数："+param.getParamId()+"的信息");
        return rv.data(intfBiz.modifyParam(param));
    }

    //获取接口参数信息集合
    @ApiOperation(value = "查询接口的全部参数，包括可用和不可用",
            notes = "条件为接口id")
    @RequestMapping(value = "/intfParamsManager/getIntfParams", method = RequestMethod.POST)
    public ObjectRestResponse<List<SJ_Interface_Params>> getIntfParams(@RequestParam(value = "intfId") String intfId){
        ObjectRestResponse<List<SJ_Interface_Params>> rv = new ObjectRestResponse<List<SJ_Interface_Params>>();
        return rv.data(intfBiz.getIntfParams(intfId));
    }

    //放弃使用接口参数
    @ApiOperation(value = "废弃接口参数",
            notes = "具体操作为将参数状态修改为不可用")
    @RequestMapping(value = "/intfParamsManager/discardParam", method = RequestMethod.POST)
    public ObjectRestResponse<String> discardParam(@RequestParam(value = "paramId") String paramId){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(intfBiz.discardParam(paramId));
    }

    //放弃使用接口参数
    @ApiOperation(value = "启用接口参数",
            notes = "具体操作为将参数状态修改为可用")
    @RequestMapping(value = "/intfParamsManager/enableParam", method = RequestMethod.POST)
    public ObjectRestResponse<String> enableParam(@RequestParam(value = "paramId") String paramId){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(intfBiz.enableParam(paramId));
    }

    //查询单个接口参数的详细信息
    @ApiOperation(value = "按id精确的查询一个参数的详细信息",
            notes = "通过id精确查询")
    @RequestMapping(value = "/intfParamsManager/getParamById", method = RequestMethod.POST)
    public ObjectRestResponse<SJ_Interface_Params> getParamById(@RequestParam(value = "paramId") String paramId){
        ObjectRestResponse<SJ_Interface_Params> rv = new ObjectRestResponse<SJ_Interface_Params>();
        return rv.data(intfBiz.getParamById(paramId));
    }
}
