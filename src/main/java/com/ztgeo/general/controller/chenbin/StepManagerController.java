package com.ztgeo.general.controller.chenbin;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.chenbin.StepManagerBiz;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Act_Step;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Step_Position;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Step_Interface;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Step_Service;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/sysManager")
@Api(tags = {"流程步骤管理"})
public class StepManagerController{

    @Autowired
    private StepManagerBiz stepBiz;

    //查询流程的step们
    @RequestMapping(value = "/stepManager/findProcessSteps", method = RequestMethod.POST)
    public ObjectRestResponse<List<SJ_Act_Step>> findProcessSteps(@RequestParam(value = "processId") String processId){
        ObjectRestResponse<List<SJ_Act_Step>> rv = new ObjectRestResponse<List<SJ_Act_Step>>();
        return rv.data(stepBiz.findProcessSteps(processId));
    }

    //流程的修改
    @RequestMapping(value = "/stepManager/modifyStep", method = RequestMethod.POST)
    public ObjectRestResponse<String> modifyStep(@RequestBody SJ_Act_Step step){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(stepBiz.modifyStep(step));
    }
    //精确查找step
    @RequestMapping(value = "/stepManager/findStepByStepId", method = RequestMethod.POST)
    public ObjectRestResponse<SJ_Act_Step> findStepByStepId(@RequestParam("stepId")String stepId){
        ObjectRestResponse<SJ_Act_Step> rv = new ObjectRestResponse<SJ_Act_Step>();
        return rv.data(stepBiz.findStepByStepId(stepId));
    }

    /*
     * 查询step各个权限
     */
    @RequestMapping(value = "/stepManager/searchStepIntfByStepId", method = RequestMethod.POST)
    public ObjectRestResponse<List<SJ_Power_Step_Interface>> searchStepIntfByStepId(@RequestParam("stepId")String stepId){
        ObjectRestResponse<List<SJ_Power_Step_Interface>> rv = new ObjectRestResponse<List<SJ_Power_Step_Interface>>();
        return rv.data(stepBiz.searchStepIntfByStepId(stepId));
    }
    @RequestMapping(value = "/stepManager/searchStepSvrByStepId", method = RequestMethod.POST)
    public ObjectRestResponse<List<SJ_Power_Step_Service>> searchStepSvrByStepId(@RequestParam("stepId")String stepId){
        ObjectRestResponse<List<SJ_Power_Step_Service>> rv = new ObjectRestResponse<List<SJ_Power_Step_Service>>();
        return rv.data(stepBiz.searchStepSvrByStepId(stepId));
    }
    @RequestMapping(value = "/stepManager/searchStepPositionByStepId", method = RequestMethod.POST)
    public ObjectRestResponse<List<SJ_Power_Step_Position>> searchStepPositionByStepId(@RequestParam("stepId")String stepId){
        ObjectRestResponse<List<SJ_Power_Step_Position>> rv = new ObjectRestResponse<List<SJ_Power_Step_Position>>();
        return rv.data(stepBiz.searchStepPosionsByStepId(stepId));
    }
    @RequestMapping(value = "/stepManager/findStepIntfByPowerId", method = RequestMethod.POST)
    public ObjectRestResponse<SJ_Power_Step_Interface> findStepIntfByPowerId(@RequestParam("powerId")String powerId){
        ObjectRestResponse<SJ_Power_Step_Interface> rv = new ObjectRestResponse<SJ_Power_Step_Interface>();
        return rv.data(stepBiz.findStepIntfByPowerId(powerId));
    }
    @RequestMapping(value = "/stepManager/findStepSvrByPowerId", method = RequestMethod.POST)
    public ObjectRestResponse<SJ_Power_Step_Service> findStepSvrByPowerId(@RequestParam("powerId")String powerId){
        ObjectRestResponse<SJ_Power_Step_Service> rv = new ObjectRestResponse<SJ_Power_Step_Service>();
        return rv.data(stepBiz.findStepSvrByPowerId(powerId));
    }
    @RequestMapping(value = "/stepManager/findStepPositionByPowerId", method = RequestMethod.POST)
    public ObjectRestResponse<SJ_Power_Step_Position> findStepPositionByPowerId(@RequestParam("powerId")String powerId){
        ObjectRestResponse<SJ_Power_Step_Position> rv = new ObjectRestResponse<SJ_Power_Step_Position>();
        return rv.data(stepBiz.findStepPositionByPowerId(powerId));
    }

    /*
     * 增加step各个权限
     */
    @RequestMapping(value = "/stepManager/addStepIntf", method = RequestMethod.POST)
    public ObjectRestResponse<String> addStepIntf(@RequestBody SJ_Power_Step_Interface stepIntf){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(stepBiz.addStepIntf(stepIntf));
    }
    @RequestMapping(value = "/stepManager/addStepSvr", method = RequestMethod.POST)
    public ObjectRestResponse<String> addStepSvr(@RequestBody SJ_Power_Step_Service stepSvr){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(stepBiz.addStepSvr(stepSvr));
    }
    @RequestMapping(value = "/stepManager/addStepPosition", method = RequestMethod.POST)
    public ObjectRestResponse<String> addStepPosition(@RequestBody SJ_Power_Step_Position stepPosition){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(stepBiz.addStepPosition(stepPosition));
    }

    /*
     * 改步骤各个权限
     */
    @RequestMapping(value = "/stepManager/modifyStepIntf", method = RequestMethod.POST)
    public ObjectRestResponse<String> modifyStepIntf(@RequestBody SJ_Power_Step_Interface stepIntf){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(stepBiz.modifyStepIntf(stepIntf));
    }
    @RequestMapping(value = "/stepManager/modifyStepSvr", method = RequestMethod.POST)
    public ObjectRestResponse<String> modifyStepSvr(@RequestBody SJ_Power_Step_Service stepSvr){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(stepBiz.modifyStepSvr(stepSvr));
    }
    @RequestMapping(value = "/stepManager/modifyStepPosition", method = RequestMethod.POST)
    public ObjectRestResponse<String> modifyStepPosition(@RequestBody SJ_Power_Step_Position stepPosition){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(stepBiz.modifyStepPosition(stepPosition));
    }

    /*
     * 删除步骤各个权限
     */
    @RequestMapping(value = "/stepManager/removeStepIntf", method = RequestMethod.POST)
    public ObjectRestResponse<String> removeStepIntf(@RequestParam("powerId") String powerId){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(stepBiz.removeStepIntf(powerId));
    }
    @RequestMapping(value = "/stepManager/removeStepSvr", method = RequestMethod.POST)
    public ObjectRestResponse<String> removeStepSvr(@RequestParam("powerId") String powerId){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(stepBiz.removeStepSvr(powerId));
    }
    @RequestMapping(value = "/stepManager/removeStepPosition", method = RequestMethod.POST)
    public ObjectRestResponse<String> removeStepPosition(@RequestParam("powerId") String powerId){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(stepBiz.removeStepPosition(powerId));
    }
}
