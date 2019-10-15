package com.ztgeo.general.service.activity.impl;


import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ztgeo.general.component.penghao.ActivitiComponent;
import com.ztgeo.general.entity.activity.ActivityPojo;
import com.ztgeo.general.entity.extend.DataGridResult;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Act_Step;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.mapper.activity.ActivityMapper;
import com.ztgeo.general.mapper.chenbin.StepManagerMapper;
import com.ztgeo.general.service.activity.ActivityService;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Model;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service("activityService")
@Transactional(rollbackFor = Exception.class)
public class ActivityServiceImpl implements ActivityService {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private StepManagerMapper stepManagerMapper;
    @Autowired
    private ActivitiComponent activitiComponent;


    @Override
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true,isolation = Isolation.DEFAULT,
            rollbackFor=RuntimeException.class)
    public List<ActivityPojo> selectForARP(ActivityPojo activityPojo) {
        List<ActivityPojo> activityPojoList = activityMapper.selectForARP(activityPojo);
        return activityPojoList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true,isolation = Isolation.DEFAULT,
            rollbackFor=RuntimeException.class)
    public Object getFlowchartPresentation(String id,String processId) throws IOException {
            //前端传-1表示为空
            if ("-1".equals(id)){
                id=activitiComponent.getActivityByProcessId(processId);
            }
            //获取模型
            RepositoryService repositoryService = processEngine.getRepositoryService();
            Model modelData = repositoryService.getModel(id);
            if (modelData == null){
                throw new ZtgeoBizException(MsgManager.MODEL_MB);
            }
            byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());
            if (bytes == null) {
                throw new ZtgeoBizException(MsgManager.WORKFLOW_MODEL_IS_NULL);
            }
            JsonNode modelNode = new ObjectMapper().readTree(bytes);
            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            if(model.getProcesses().size()==0){
                throw new ZtgeoBizException(MsgManager.WORKFLOW_MODEL_MAIN_ERROR);
            }
            SJ_Act_Step sjActStep=stepManagerMapper.findStartModuleIdByStep(modelData.getId());
            if (sjActStep==null){
                throw new ZtgeoBizException(MsgManager.DEPLOMENT_MODULE_NULL);
            }
            Process process=model.getProcesses().get(0);
            //获取
            Collection<FlowElement> flowElements=process.getFlowElements();
            List<SJ_Act_Step> act_steps=new ArrayList<>();
            for (FlowElement flowElement:flowElements){
                if (flowElement instanceof UserTask) {
                    UserTask userTask = (UserTask) flowElement;
                    List<SequenceFlow> usertaskfj = ((UserTask) flowElement).getIncomingFlows();
                    for (SequenceFlow sequenceFlow : usertaskfj) {
                        if (sequenceFlow.getSourceRef().equals("start")){
                            SJ_Act_Step sj_act_step=new SJ_Act_Step();
                            sj_act_step.setStepMouldId(sequenceFlow.getSourceRef());
                            sj_act_step.setStepId(sjActStep.getStepId());
                            sj_act_step.setStepMouldName("开始");
                            act_steps.add(sj_act_step);
                        }
                        SJ_Act_Step sj_act_step=new SJ_Act_Step();
                        sj_act_step.setStepMouldId(userTask.getId());
                        String stepId=getStepId(userTask.getId());
                        if (StringUtils.isEmpty(stepId)){
                            throw  new ZtgeoBizException(MsgManager.STEP_MOULD_ID_NULL);
                        }
                        sj_act_step.setStepId(stepId);
                        sj_act_step.setStepMouldName(userTask.getName());
                        sj_act_step.setParentStepMouldId(sequenceFlow.getSourceRef());
                        act_steps.add(sj_act_step);
                    }
                }
                if (flowElement instanceof ParallelGateway){
                    ParallelGateway parallelGateway = (ParallelGateway) flowElement;
                    List<SequenceFlow> parllelgatewayFj = ((ParallelGateway) flowElement).getIncomingFlows();
                    for (SequenceFlow sequenceFlow : parllelgatewayFj) {
                        SJ_Act_Step sj_act_step=new SJ_Act_Step();
                        sj_act_step.setStepMouldId(parallelGateway.getId());
                        sj_act_step.setStepMouldName("并行网关");
                        sj_act_step.setParentStepMouldId(sequenceFlow.getSourceRef());
                        act_steps.add(sj_act_step);
                    }
                }
            }
            Map<String,Object> map=getCompletedNodeByNodeExecution(processId);
            map.put("act_steps",act_steps);
            return map;

    }

    private String getStepId(String stepMouldId){
        String stepId=null;
        SJ_Act_Step step=stepManagerMapper.selectStepByMouldId(stepMouldId);
        if (step!=null) {
            stepId=step.getStepId();
        }
        return stepId;
    }


    private Map<String,Object>  getCompletedNodeByNodeExecution(String processId){
        //正在执行的
        Map<String,Object> map=new HashMap<>();
        List<SJ_Act_Step> completed=new ArrayList<>();
        List<SJ_Act_Step> execution=new ArrayList<>();
        List<Task> taskes=taskService.createTaskQuery().processInstanceId(processId).list();
        for (Task task:taskes) {
            SJ_Act_Step sj_act_step=new SJ_Act_Step();
            sj_act_step.setStepMouldId(task.getTaskDefinitionKey());
            sj_act_step.setStepMouldName(task.getName());
            execution.add(sj_act_step);
        }
        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()//历史任务表查询
                .processInstanceId(processId)//使用流程实例ID查询
                .list();
        for(HistoricTaskInstance hti:htiList) {
            if (hti.getEndTime()!=null ) {
                SJ_Act_Step sj_act_step = new SJ_Act_Step();
                sj_act_step.setStepMouldId(hti.getTaskDefinitionKey());
                sj_act_step.setStepMouldName(hti.getName());
                completed.add(sj_act_step);
            }
        }
        map.put("completed",completed);
        map.put("execution",execution);
        return map;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED,readOnly = true,isolation = Isolation.DEFAULT,
            rollbackFor=RuntimeException.class)
    public Object selectForARPAndRowsAndPage(ActivityPojo activityPojo) {

            if(StringUtils.isEmpty(activityPojo.getPage())){
                activityPojo.setPage("1");
            }
            if(StringUtils.isEmpty(activityPojo.getRows())){
                activityPojo.setRows("20");
            }
            PageHelper.startPage(Integer.parseInt(activityPojo.getPage()),
                    Integer.parseInt(activityPojo.getRows()));//分页条件
            List<ActivityPojo> activityPojoList = selectForARP(activityPojo);
            PageInfo<ActivityPojo> pageInfo = new PageInfo<ActivityPojo>(activityPojoList);
            return new DataGridResult(pageInfo.getTotal(),pageInfo.getList());

    }
}
