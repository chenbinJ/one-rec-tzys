package com.ztgeo.general.controller.activity;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ag.core.context.BaseContextHandler;
import com.google.common.collect.Maps;
import com.ztgeo.general.component.penghao.ActivitiComponent;
import com.ztgeo.general.entity.Depart;
import com.ztgeo.general.entity.activity.ActivityPojo;
import com.ztgeo.general.entity.activity.Approve;
import com.ztgeo.general.entity.activity.HistoryPojo;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.exceptionmsg.WorkFlowManager;
import com.ztgeo.general.mapper.penghao.DepartUserMapper;
import com.ztgeo.general.service.activity.ActivityService;
import com.ztgeo.general.service.activity.ApproveService;
import com.ztgeo.general.service.activity.MyActivityService;
import com.ztgeo.general.service.activity.WorkFlowOperateService;
import com.ztgeo.general.util.CommonUtils;
import com.ztgeo.general.util.DateUtils;
import com.ztgeo.general.util.ReturnMsgUtil;
import com.ztgeo.general.util.chenbin.IDUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.NativeUserQueryImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("api/myActivity")
@Api(tags = "定义流程")
public class MyActivityController {

    private Logger logger = Logger.getLogger(this.getClass());


    @Autowired
    private MyActivityService myActivityService;

    @Autowired
    private WorkFlowOperateService workFlowOperateService;

    @Autowired
    private ProcessEngine processEngine;



    /**
     * 根据审批单ID获取审批单详细信息
     * @param processInstanceId
     * @return
     */
    @RequestMapping(value = "getNextTaskInfo")
    @ResponseBody
    public Object getNextTaskInfo(String processInstanceId){
        try {
            String username = BaseContextHandler.getUsername();
            return ReturnMsgUtil.setAndReturn(true,workFlowOperateService.getNextTaskInfo(processInstanceId));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ReturnMsgUtil.setAndReturn(false,e.getMessage());
        }
    }



    /**
     * 查询activity定义流程
     * @param activityPojo
     * @return
     */
    @RequestMapping(value = "selectActivityDefinition",method = RequestMethod.POST)
    @ResponseBody
    public Object selectActivityDefinition(ActivityPojo activityPojo){
        try {
            return myActivityService.processList(activityPojo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ReturnMsgUtil.setAndReturn(false,e.getMessage());
        }
    }

    /**
     * 查询activity定义流程--分页
     * @param activityPojo
     * @return
     */
    @RequestMapping(value = "selectActivityDefinitionForPage",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("部署模板--分页")
    public Object selectActivityDefinitionForPage(ActivityPojo activityPojo){
        try {
            return myActivityService.processListForPage(activityPojo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ReturnMsgUtil.setAndReturn(false,e.getMessage());
        }
    }

    /**
     * 查询activity(定义、未定义)流程--分页
     * @param activityPojo
     * @return
     */
    @RequestMapping(value = "selectActivityAll",method = RequestMethod.POST)
    @ResponseBody
    public Object selectActivityAll(ActivityPojo activityPojo){
        try {
            return myActivityService.modelList(activityPojo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ReturnMsgUtil.setAndReturn(false,e.getMessage());
        }
    }

    /**
     * 查询activity(定义、未定义)流程--分页
     * @param activityPojo
     * @return
     */
    @RequestMapping(value = "selectActivityAllForPage",method =RequestMethod.POST)
    @ResponseBody
    @ApiOperation("部署模板分页")
    public Object selectActivityAllForPage(ActivityPojo activityPojo){
        try {
            return myActivityService.modelListForPage(activityPojo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ReturnMsgUtil.setAndReturn(false,e.getMessage());
        }
    }

    /**
     * 创建模型
     */
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("创建模型")
    public Object create(@RequestParam("name") String name, @RequestParam("key") String key, @RequestParam("description") String description,
                          HttpServletRequest request, HttpServletResponse response) {
        try {
            Model modelData = myActivityService.create(name, key, description);
            return ReturnMsgUtil.setAndReturn(true,"/activity/modeler.html?modelId= "+ modelData.getId());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("创建模型失败：", e);
            return ReturnMsgUtil.setAndReturn(false,e.getMessage());
        }
    }


    @RequestMapping(value = "modelDelete",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("删除模板")
    public Object deleteMode(@RequestParam("modelId") String id) {
        try {
            myActivityService.deleteModel(id);
            return ReturnMsgUtil.setAndReturn(true, MsgManager.OPERATING_SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ReturnMsgUtil.setAndReturn(false,e.getMessage());
        }
    }


    @RequestMapping(value = "deleteDeployeement",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("删除部署模板")
    public Object deleteDeployeement(String arpDeploymentId) {
        try {
            //删除部署表(根据arpDeploymentId删除)
            //true代表级联删除，也就是强制删除，无论有没有启动实例都删除（删除的是四张表，原先的三表+act_ru_execution表）
            processEngine.getRepositoryService().deleteDeployment(arpDeploymentId,true);
//            myActivityService.deleteDeployeement(arpDeploymentId);
            return ReturnMsgUtil.setAndReturn(true, MsgManager.OPERATING_SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ReturnMsgUtil.setAndReturn(false,e.getMessage());
        }
    }



    /**
     * 导出model的xml文件
     */
    @RequestMapping(value = "exportModel",method = RequestMethod.POST)
    @ResponseBody
    public void export(@RequestParam("modelId") String id, HttpServletResponse response) {
        try {
            myActivityService.exportModel(id, response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    /**
     * 删除定义流程
     * @param id 部署流程ID
     * @param response
     * @return
     */
    @RequestMapping(value = "deleteDefinition",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("删除定义流程")
    public Object deleteDefinition(@RequestParam("deploymentId") String id, HttpServletResponse response) {
        try {
            return myActivityService.deleteDefinition(id);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ReturnMsgUtil.setAndReturn(false,MsgManager.DELETE_ERROR);
        }
    }

    /**
     * 挂起、激活流程实例
     * @param taskId 节点id active激活流程 suspend挂起
     * @param id 部署流程ID
     * @param response
     * @return
     */
    @RequestMapping(value = "updateState",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("挂起、激活流程实例")
    public Object updateState(@RequestParam("taskId") String taskId, @RequestParam("arpId") String id, HttpServletResponse response) {
        try {
            return myActivityService.updateState(taskId,id);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ReturnMsgUtil.setAndReturn(false,MsgManager.DELETE_ERROR);
        }
    }

    /**
     * 将部署的流程转换为模型
     * @param id 部署流程ID
     * @param response
     * @return
     */
    @RequestMapping(value = "convertToModel",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("将部署的流程转换为模型")
    public Object convertToModel(@RequestParam("arpId") String id, HttpServletResponse response) {
        try {
            return myActivityService.convertToModel(id);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return ReturnMsgUtil.setAndReturn(false,MsgManager.DELETE_ERROR);
        }
    }

}
