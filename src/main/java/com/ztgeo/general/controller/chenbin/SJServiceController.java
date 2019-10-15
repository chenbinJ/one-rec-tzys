package com.ztgeo.general.controller.chenbin;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.chenbin.SJServiceBiz;
import com.ztgeo.general.component.chenbin.StepManagerComponent;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Act_Step;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Service;
import com.ztgeo.general.mapper.chenbin.StepManagerMapper;
import com.ztgeo.general.util.chenbin.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/sjServiceManager")
@Api(tags = "服务")
public class SJServiceController {

    @Autowired
    private SJServiceBiz sjServiceBiz;
    @Autowired
    private StepManagerMapper stepManagerMapper;
    @Autowired
    private StepManagerComponent stepManagerComponent;

    //查询流程的step们
    @RequestMapping(value = "/findSjServiceByUid", method = RequestMethod.POST)
    @ApiOperation("首页服务")
    public Object findSjServiceByUid(@RequestParam(value = "uid" , required = false) String uid){
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        return rv.data(sjServiceBiz.findSjServiceByUid(UserUtil.getUerId()));
    }

    //转移登记流程权证信息服务
    @RequestMapping(value = "/findSjServiceByModuleId", method = RequestMethod.POST)
    @ApiOperation("转移登记流程权证信息服务")
    public Object findSjServiceByModuleId(String moduleId){
        SJ_Act_Step sjActStep=stepManagerMapper.findStepByModuleId(moduleId);
        return stepManagerComponent.getStepSettings(sjActStep.getStepId());
    }


}
