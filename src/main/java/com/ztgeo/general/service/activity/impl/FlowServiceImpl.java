package com.ztgeo.general.service.activity.impl;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc:流程管理service
 * @Author:wangli
 * @CreateTime:20:10 2018/10/15
 */
@Service
public class FlowServiceImpl {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;


    public List<TaskDefinition> getTaskDefinitionList(String procInstId) {
        List<TaskDefinition> taskDefinitionList = new ArrayList<TaskDefinition>();
        //流程标示
        String processDefinitionId = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).singleResult().getProcessDefinitionId();
        ProcessDefinitionEntity pde = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
        //执行实例
        ExecutionEntity execution = (ExecutionEntity) runtimeService.createProcessInstanceQuery().processInstanceId(procInstId).singleResult();
        //当前实例的执行到哪个节点
        if (execution != null) {
            String activitiId = execution.getActivityId();
            //获得当前任务的所有节点
            List<ActivityImpl> activitiList = pde.getActivities();
            String id = null;
            for (ActivityImpl activityImpl : activitiList) {
                id = activityImpl.getId();
                if (activitiId.equals(id)) {
                    logger.debug("当前任务：" + activityImpl.getProperty("name"));
                    taskDefinitionList = nextTaskDefinition(activityImpl, activityImpl.getId());
                }
            }
        }

        return taskDefinitionList;
    }

    private List<TaskDefinition> nextTaskDefinition(ActivityImpl activityImpl, String activityId) {
        List<TaskDefinition> taskDefinitionList = new ArrayList<TaskDefinition>();//所有的任务实例
        List<TaskDefinition> nextTaskDefinition = new ArrayList<TaskDefinition>();//逐个获取的任务实例
        TaskDefinition taskDefinition = null;
        if ("userTask".equals(activityImpl.getProperty("type")) && !activityId.equals(activityImpl.getId())) {
            taskDefinition = ((UserTaskActivityBehavior) activityImpl.getActivityBehavior()).getTaskDefinition();
            taskDefinitionList.add(taskDefinition);
        } else {
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            List<PvmTransition> outTransitionsTemp = null;
            for (PvmTransition tr : outTransitions) {
                PvmActivity ac = tr.getDestination(); //获取线路的终点节点
                //如果是互斥网关或者是并行网关
                if ("exclusiveGateway".equals(ac.getProperty("type")) || "parallelGateway".equals(ac.getProperty("type"))) {
                    outTransitionsTemp = ac.getOutgoingTransitions();
                    if (outTransitionsTemp.size() == 1) {
                        nextTaskDefinition = nextTaskDefinition((ActivityImpl) outTransitionsTemp.get(0).getDestination(), activityId);
                        taskDefinitionList.addAll(nextTaskDefinition);
                    } else if (outTransitionsTemp.size() > 1) {
                        for (PvmTransition tr1 : outTransitionsTemp) {
                            nextTaskDefinition = nextTaskDefinition((ActivityImpl) tr1.getDestination(), activityId);
                            taskDefinitionList.addAll(nextTaskDefinition);
                        }
                    }
                } else if ("userTask".equals(ac.getProperty("type"))) {
                    taskDefinition = ((UserTaskActivityBehavior) ((ActivityImpl) ac).getActivityBehavior()).getTaskDefinition();
                    taskDefinitionList.add(taskDefinition);
                } else {
                    logger.debug((String) ac.getProperty("type"));
                }
            }
        }
        return taskDefinitionList;
    }

    @SuppressWarnings("unused")
    private List<TaskDefinition> nextTaskDefinition(ActivityImpl activityImpl, String activityId, String elString) {
        List<TaskDefinition> taskDefinitionList = new ArrayList<TaskDefinition>();//所有的任务实例
        List<TaskDefinition> nextTaskDefinition = new ArrayList<TaskDefinition>();//逐个获取的任务实例
        TaskDefinition taskDefinition = null;
        if ("userTask".equals(activityImpl.getProperty("type")) && !activityId.equals(activityImpl.getId())) {
            taskDefinition = ((UserTaskActivityBehavior) activityImpl.getActivityBehavior()).getTaskDefinition();
            taskDefinitionList.add(taskDefinition);
        } else {
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            List<PvmTransition> outTransitionsTemp = null;
            for (PvmTransition tr : outTransitions) {
                PvmActivity ac = tr.getDestination(); //获取线路的终点节点
                //如果是互斥网关或者是并行网关
                if ("exclusiveGateway".equals(ac.getProperty("type")) || "parallelGateway".equals(ac.getProperty("type"))) {
                    outTransitionsTemp = ac.getOutgoingTransitions();
                    if (outTransitionsTemp.size() == 1) {
                        nextTaskDefinition = nextTaskDefinition((ActivityImpl) outTransitionsTemp.get(0).getDestination(), activityId, elString);
                        taskDefinitionList.addAll(nextTaskDefinition);
                    } else if (outTransitionsTemp.size() > 1) {
                        for (PvmTransition tr1 : outTransitionsTemp) {
                            Object s = tr1.getProperty("conditionText");
                            if (elString.equals(StringUtils.trim(s.toString()))) {
                                nextTaskDefinition = nextTaskDefinition((ActivityImpl) tr1.getDestination(), activityId, elString);
                                taskDefinitionList.addAll(nextTaskDefinition);
                            }
                        }
                    }
                } else if ("userTask".equals(ac.getProperty("type"))) {
                    taskDefinition = ((UserTaskActivityBehavior) ((ActivityImpl) ac).getActivityBehavior()).getTaskDefinition();
                    taskDefinitionList.add(taskDefinition);
                } else {
                    logger.debug((String) ac.getProperty("type"));
                }
            }
        }
        return taskDefinitionList;
    }


    /**
     * 获取下一个用户任务信息
     * @param taskId
     * @return
     * @throws Exception
    public TaskDefinition getNextTaskInfo(String taskId) throws Exception {

    ProcessDefinitionEntity processDefinitionEntity = null;

    String id = null;

    TaskDefinition task = null;

    //获取流程实例Id信息
    String processInstanceId = taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();

    //获取流程发布Id信息
    String definitionId = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getProcessDefinitionId();

    processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
    .getDeployedProcessDefinition(definitionId);

    ExecutionEntity execution = (ExecutionEntity) runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

    //当前流程节点Id信息
    String activitiId = execution.getActivityId();

    //获取流程所有节点信息
    List<ActivityImpl> activitiList = processDefinitionEntity.getActivities();

    //遍历所有节点信息
    for(ActivityImpl activityImpl : activitiList){
    id = activityImpl.getId();
    if (activitiId.equals(id)) {
    //获取下一个节点信息
    task = nextTaskDefinition(activityImpl, activityImpl.getId(), null, processInstanceId);
    break;
    }
    }
    return task;
    }

     *//**
     * 下一个任务节点信息
     * @param activityImpl
     * @param activityId
     * @param elString
     * @param processInstanceId
     * @return
     *//*
    private TaskDefinition nextTaskDefinition(ActivityImpl activityImpl, String activityId, String elString, String processInstanceId){

        PvmActivity ac = null;

        Object s = null;

        // 如果遍历节点为用户任务并且节点不是当前节点信息
        if ("userTask".equals(activityImpl.getProperty("type")) && !activityId.equals(activityImpl.getId())) {
            // 获取该节点下一个节点信息
            TaskDefinition taskDefinition = ((UserTaskActivityBehavior) activityImpl.getActivityBehavior())
                    .getTaskDefinition();
            return taskDefinition;
        } else {
            // 获取节点所有流向线路信息
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            List<PvmTransition> outTransitionsTemp = null;
            for (PvmTransition tr : outTransitions) {
                ac = tr.getDestination(); // 获取线路的终点节点
                // 如果流向线路为排他网关
                if ("exclusiveGateway".equals(ac.getProperty("type"))) {
                    outTransitionsTemp = ac.getOutgoingTransitions();

                    // 如果网关路线判断条件为空信息
                    if (StringUtils.isEmpty(elString)) {
                        // 获取流程启动时设置的网关判断条件信息
                        elString = getGatewayCondition(ac.getId(), processInstanceId);
                    }

                    // 如果排他网关只有一条线路信息
                    if (outTransitionsTemp.size() == 1) {
                        return nextTaskDefinition((ActivityImpl) outTransitionsTemp.get(0).getDestination(), activityId,
                                elString, processInstanceId);
                    } else if (outTransitionsTemp.size() > 1) { // 如果排他网关有多条线路信息
                        for (PvmTransition tr1 : outTransitionsTemp) {
                            s = tr1.getProperty("conditionText"); // 获取排他网关线路判断条件信息
                            // 判断el表达式是否成立
                            if (isCondition(ac.getId(), StringUtils.trim(s.toString()), elString)) {
                                return nextTaskDefinition((ActivityImpl) tr1.getDestination(), activityId, elString,
                                        processInstanceId);
                            }
                        }
                    }
                } else if ("userTask".equals(ac.getProperty("type"))) {
                    return ((UserTaskActivityBehavior) ((ActivityImpl) ac).getActivityBehavior()).getTaskDefinition();
                } else {
                }
            }
            return null;
        }
    }

    *//**
     * 查询流程启动时设置排他网关判断条件信息
     * @param gatewayId
     * @param processInstanceId
     * @return
     *//*
    public String getGatewayCondition(String gatewayId, String processInstanceId) {
        Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
        Object object= runtimeService.getVariable(execution.getId(), gatewayId);
        return object==null? "":object.toString();
    }

    *//**
     * 根据key和value判断el表达式是否通过信息
     * @param key
     * @param el
     * @param value
     * @return
     *//*
    public boolean isCondition(String key, String el, String value) {
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();
        context.setVariable(key, factory.createValueExpression(value, String.class));
        ValueExpression e = factory.createValueExpression(context, el, boolean.class);
        return (Boolean) e.getValue(context);
    }*/

}