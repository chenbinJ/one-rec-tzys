package com.ztgeo.general.service.activity.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.util.BooleanUtil;
import com.google.common.collect.Maps;
import com.ztgeo.general.config.webscoket.WebSocket;
import com.ztgeo.general.entity.User;
import com.ztgeo.general.entity.activity.Approve;
import com.ztgeo.general.entity.activity.HistoryPojo;
import com.ztgeo.general.entity.activity.NodeInfoPojo;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.exceptionmsg.WorkFlowManager;
import com.ztgeo.general.mapper.UserMapper;
import com.ztgeo.general.mapper.activity.ApproveMapper;
import com.ztgeo.general.mapper.penghao.DepartUserMapper;
import com.ztgeo.general.service.activity.WorkFlowOperateService;
import com.ztgeo.general.util.ReturnMsgUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.javax.el.ExpressionFactory;
import org.activiti.engine.impl.javax.el.ValueExpression;
import org.activiti.engine.impl.juel.ExpressionFactoryImpl;
import org.activiti.engine.impl.juel.SimpleContext;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceBuilder;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class WorkFlowOperateServiceImpl implements WorkFlowOperateService {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApproveMapper approveMapper;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private DepartUserMapper departUserMapper;
    @Autowired
    private WebSocket webSocket;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private UserMapper userMapper;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public ProcessInstance startWorkFLowByProcessDefinitionKey(String processId, Map<String, Object> inputMap) {
        System.out.println(processId + "  ==  " + JSONObject.toJSONString(inputMap));
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey(processId, inputMap);
        simpleLoggerForProcessInstance(processInstance);
        return processInstance;
    }

    @Override
    public String getActivityByProcessId(String processId) {

        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId())
                .singleResult();

        return "";
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public String startWorkFLowProcessInstanceBuilder(String processId, Map<String, Object> inputMap) {
        ProcessInstanceBuilder builder = runtimeService.createProcessInstanceBuilder();
        ProcessInstance processInstance = builder
                .processDefinitionKey(processId)
                .start();
        simpleLoggerForProcessInstance(processInstance);
        return processInstance.getProcessInstanceId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object taskSingleWorkFLowByProcessInstanceId(String processInstanceId, String approveResult, String taskId,
                                                        String userCode, String turnToUser, String comment, List<Map<String, Object>> variables) {
        //根据流程实例ID获取taskId
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Task newTask = null;
        //管理员的情况（管理员提交一切）
        User u = userMapper.selectUserByName(userCode);
        if (u == null) {
            throw new ZtgeoBizException("任务指定的执行用户不存在");
        }
        if (BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(u.getId()).getIsSuperAdmin())) {
            taskService.addCandidateUser(taskId, userCode);//增加管理员为签收人
            taskService.setAssignee(taskId, userCode);//设置管理员为处理人
            //二次查询在同一个事务中不知道有没有效果（若没效，执行最后的if条件）
            task = taskService.createTaskQuery().taskId(taskId).singleResult();
            if (!task.getAssignee().equals(userCode))
                task.setAssignee(userCode);
        }
        if (StringUtils.isBlank(task.getAssignee())) {
            task.setAssignee(userCode);
        }
        //设置过的处理人
        if (userCode.equals(task.getAssignee())) {
            newTask = task;
        } else {
            //候选人集合
            List identityLinkList = taskService.getIdentityLinksForTask(task.getId());
            if (null != identityLinkList || identityLinkList.size() != 0) {
                for (Iterator iterator = identityLinkList.iterator(); iterator
                        .hasNext(); ) {
                    IdentityLink identityLink = (IdentityLink) iterator.next();
                    //判断是否为当前登录人
                    if (userCode.equals(identityLink.getUserId())) {
                        newTask = task;
                    }
                }
            }
            if (StringUtils.isEmpty(task.getAssignee())) {
                task.setAssignee(userCode);
                newTask = task;
            }
        }
        String gettask = newTask.getId();
        Approve approve = this.approveMapper.selectByTaskId(gettask);//查询审批对象
        if ("agree".equals(approveResult)) {//同意
            //监听器不需要设置流程变量
                /*for (Map<String, Object> variable : variables) {
                    taskService.setVariable(gettask, (String) variable.get("name"), variable.get("value"));//手动设置流程变量
                }*/
            Authentication.setAuthenticatedUserId(userCode);//批注人的名称  一定要写，不然查看的时候不知道人物信息
            taskService.addComment(taskId, null, comment);//添加审批意见
            taskService.complete(taskId, null);//审批通过
            //通过之后设置审核表节点的结束时间
            approve.setApproveTaskEndTime(new Date());
            approve.setApprovePerson(userCode);
//                approve.setApproveCreateDapartment(departName);
            approveMapper.updateByPrimaryKeySelective(approve);
            //发送webscoket
            webSocket.sendMessage(UserUtil.checkAndGetUser(), webSocket.SendResultMsagge(userCode, UserUtil.getUerId()));
            return ReturnMsgUtil.setAndReturn(true, MsgManager.OPERATING_SUCCESS);
        } else if ("disagree".equals(approveResult)) {//不同意
            ActivityImpl currActivity = findActivitiImpl(taskId, null);// 当前节点
            List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);// 清空当前流向
            TransitionImpl newTransition = currActivity.createOutgoingTransition();// 创建新流向
            ActivityImpl pointActivity = findActivitiImpl(taskId, "start");// 目标节点
            newTransition.setDestination(pointActivity);// 设置新流向的目标节点
            //暂时不需要打回修改
               /* for (Task taskObj : tasks) {
                    String currentTaskComment = approveResult + "##system##"
                            + DateUtils.dateString(new Date(), DateUtils.JAVA_YYYY_MM_DD_HH_MM_SS)
                            + "##系统默认";//"系统默认打回";
                    if (taskId.equals(taskObj.getId())) {
                        currentTaskComment = comment;
                    }
                    taskService.addComment(taskObj.getId(), null, currentTaskComment);//添加审批意见
                    taskService.complete(taskObj.getId(), null);// 执行转向任务*/
            /*   }*/

                /*pointActivity.getIncomingTransitions().remove(newTransition);// 删除目标节点新流入
                restoreTransition(currActivity, oriPvmTransitionList);// 还原以前流向
                return ReturnMsgUtil.setAndReturn(true, MsgManager.OPERATING_SUCCESS);*/
        } else if ("turnToDo".equals(approveResult)) {//转办
            taskService.addComment(gettask, null, comment);//添加审批意见
            taskService.setAssignee(gettask, turnToUser);
            return ReturnMsgUtil.setAndReturn(true, MsgManager.OPERATING_SUCCESS);
        } else if ("discard".equals(approveResult)) {//废弃(挂起)
            taskService.addComment(gettask, null, comment);//添加审批意见
            runtimeService.suspendProcessInstanceById(processInstanceId);//挂起流程
            webSocket.sendMessage(UserUtil.checkAndGetUser(), webSocket.SendResultMsagge(userCode, UserUtil.getUerId()));
            return ReturnMsgUtil.setAndReturn(true, MsgManager.OPERATING_SUCCESS);
        } else {//审批结果不存在，异常
            return ReturnMsgUtil.setAndReturn(false, MsgManager.WORKFLOW_APPROVE_RESULT_IS_NULL);
        }
        return null;
    }

    /**
     * 判断流程实例对象所处流程是否结束
     *
     * @param processInstanceId 流程实例ID
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Boolean processIsEnd(String processInstanceId) {
        ProcessInstance singleResult = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        if (singleResult == null) {//执行完毕
            return true;
        } else {//正在执行
            return false;
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public List<HistoryPojo> getComments(String processInstanceId) {
        List<HistoryPojo> list = new ArrayList();
        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()//历史任务表查询
                .processInstanceId(processInstanceId)//使用流程实例ID查询
                .list();
        //遍历集合，获取每个任务ID
        if (htiList != null && htiList.size() > 0) {
            for (HistoricTaskInstance hti : htiList) {
                String htaskId = hti.getId();//任务ID
                String htaskName = hti.getName();//任务名
                //获取批注信息
                List<Comment> taskList = taskService.getTaskComments(htaskId);//对用历史完成后的任务ID
                for (Comment com : taskList) {
                    HistoryPojo historyPojo = new HistoryPojo();
                    historyPojo.setProcessInstanceId(com.getProcessInstanceId());
                    historyPojo.setTaskId(com.getTaskId());//审批id
                    historyPojo.setApproveNode(htaskName);//审批节点
                    historyPojo.setApproveDefinitionKey(hti.getTaskDefinitionKey());
                    String myComment = com.getFullMessage();
                    if (com instanceof CommentEntity) {//由于乱码读取问题
                        myComment = ((CommentEntity) com).getMessage();
                    }
                    historyPojo.setApproveState(myComment);
                    historyPojo.setApprovePerson(hti.getAssignee());
                    list.add(historyPojo);
                }
            }
        }
        return list;
    }

    @Override
    public Object getApprovedNodeAndActiveNode(String proceessId) {
        List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()//历史任务表查询
                .processInstanceId(proceessId)//使用流程实例ID查询
                .list();
        Map<String, Object> map = new HashMap<>();
        List<String> approveNode = new ArrayList<>();
        List<String> activeNode = new ArrayList<>();
        //遍历集合，获取每个任务ID
        if (htiList != null && htiList.size() > 0) {
            for (HistoricTaskInstance hti : htiList) {
                if (StringUtils.isNotBlank(hti.getDeleteReason())) {
                    String htaskId = hti.getId();//任务ID
                    String htaskName = hti.getName();//任务名
                    approveNode.add(hti.getId() + "," + hti.getTaskDefinitionKey());
                }
            }
        }
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(proceessId).list();
        for (Task task : taskList) {
            activeNode.add(task.getId() + "," + task.getTaskDefinitionKey());
        }
        map.put("approveNode", approveNode);
        map.put("activeNode", activeNode);
        return map;
    }


    /**
     * 还原指定活动节点流向
     *
     * @param activityImpl         活动节点
     * @param oriPvmTransitionList 原有节点流向集合
     */
    private void restoreTransition(ActivityImpl activityImpl,
                                   List<PvmTransition> oriPvmTransitionList) {
        // 清空现有流向
        List<PvmTransition> pvmTransitionList = activityImpl
                .getOutgoingTransitions();
        pvmTransitionList.clear();
        // 还原以前流向
        for (PvmTransition pvmTransition : oriPvmTransitionList) {
            pvmTransitionList.add(pvmTransition);
        }
    }

    /**
     * 清空指定活动节点流向
     *
     * @param activityImpl 活动节点
     * @return 节点流向集合
     */
    private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
        // 存储当前节点所有流向临时变量
        List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
        // 获取当前节点所有流向，存储到临时变量，然后清空
        List<PvmTransition> pvmTransitionList = activityImpl
                .getOutgoingTransitions();
        for (PvmTransition pvmTransition : pvmTransitionList) {
            oriPvmTransitionList.add(pvmTransition);
        }
        pvmTransitionList.clear();
        return oriPvmTransitionList;
    }

    /**
     * 工作流单个审批--任务ID
     *
     * @param taskId        任务ID
     * @param approveResult 流程结果：agree同意、disagree不同意、turnToDo转办、discard废弃
     * @param userCode      审批人
     * @param comment       审批意见(审批结果##审批人##审批备注##审批时间)
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object taskSingleWorkFLowByTaskId(String taskId, String approveResult, String userCode, String comment) {
        if ("agree".equals(approveResult)) {//同意
            // 由于流程用户上下文对象是线程独立的，所以要在需要的位置设置，要保证设置和获取操作在同一个线程中
            Authentication.setAuthenticatedUserId(userCode);//批注人的名称  一定要写，不然查看的时候不知道人物信息
            /*审批通过*/
            /*添加审批意见*/
            taskService.addComment(taskId, null, comment);
            /*审批提交*/
            taskService.complete(taskId, null);
            return ReturnMsgUtil.setAndReturn(true, MsgManager.OPERATING_SUCCESS);
        } else if ("disagree".equals(approveResult)) {//不同意
            /*添加审批意见*/
            taskService.setVariableLocal(taskId, "shzt", "打回修改");
            taskService.addComment(taskId, null, comment);
            /*流程挂起*/
//            runtimeService.suspendProcessInstanceById(processInstanceId);
            /*打回修改*/
            return null;
        } else if ("turnToDo".equals(approveResult)) {//转办
            return null;
        } else if ("discard".equals(approveResult)) {//废弃
            return null;
        } else {//审批结果不存在，异常
            return ReturnMsgUtil.setAndReturn(false, MsgManager.WORKFLOW_APPROVE_RESULT_IS_NULL);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public List<Task> getTaskByProcessInstanceId(String ProcessInstanceId) {
        List<Task> taskes = taskService.createTaskQuery().processInstanceId(ProcessInstanceId).list();
        return taskes;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public void completeTask(String taskId, String comment, List<Map<String, Object>> variables) {
        try {
            taskService.addComment(taskId, null, comment);//审批意见添加
            //设置变量
            for (Map<String, Object> variable : variables) {
                taskService.setVariable(taskId, (String) variable.get("name"), variable.get("value"));
            }
            taskService.complete(taskId, null);//任务结束
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取需要执行的task对象
     *
     * @param username
     * @param processId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Task implementTask(String username, String processId) {
        Task task = null;
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processId).list();
        if (taskList.size() > 0) {
            for (Task taskObj : taskList) {
                List identityLinkList = taskService.getIdentityLinksForTask(taskObj.getId());
                if (identityLinkList.size() > 1) {
                    for (Iterator iterator = identityLinkList.iterator(); iterator
                            .hasNext(); ) {
                        IdentityLink identityLink = (IdentityLink) iterator.next();
                        if (username.equals(identityLink.getUserId())) {
                            task = taskObj;
                        }
                    }
                } else {
                    task = taskObj;
                }
            }
        }
        if (StringUtils.isEmpty(task.getAssignee())) {
            task.setAssignee(username);
        }
        return task;
    }

    /**
     * 签收任务
     *
     * @param processId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object signfor(String processId) {
        //拆分字符串
        Set users = new HashSet();
        Task task = null;
        String[] process = processId.split(",");
        for (String id : process) {
            List<Task> taskList = taskService.createTaskQuery().processInstanceId(id).list();
            if (taskList.size() > 1) {
                for (Task taskObj : taskList) {
                    List identityLinkList = taskService.getIdentityLinksForTask(taskObj.getId());
                    if (identityLinkList.size() > 1) {
                        for (Iterator iterator = identityLinkList.iterator(); iterator
                                .hasNext(); ) {
                            IdentityLink identityLink = (IdentityLink) iterator.next();
                            if ("a12".equals(identityLink.getUserId())) {
                                task = taskObj;
                            }
                        }
                    }
                }
            } else {
                task = taskList.get(0);
            }
            users.add(task);
        }
        //遍历集合签收task对象
        Iterator<Task> iterator = users.iterator();
        while (iterator.hasNext()) {
            Task task1 = iterator.next();
            taskService.claim(task1.getId(), "a12");
        }
        return ReturnMsgUtil.setAndReturn(true, "操作成功");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public List<Map<String, Object>> getNodes(String procInstanceId) {
        List<Map<String, Object>> nodeList = new ArrayList<>();
        //根据流程ID获取当前任务
        Task task = taskService.createTaskQuery().processInstanceId(procInstanceId).singleResult();
        Map<String, Object> map = new HashMap<>();
        boolean isLocal = true;

        //根据当前任务获取当前流程的流程定义，然后根据流程定义获得所有的节点
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity)
                ((RepositoryServiceImpl) repositoryService)
                        .getDeployedProcessDefinition(task.getProcessDefinitionId());
        List<ActivityImpl> activitiList = processDefinitionEntity.getActivities();  //repositoryService是指RepositoryService的实例
        //根据任务获取当前流程执行ID，执行实例以及当前流程节点的ID
        String excId = task.getExecutionId();
        ExecutionEntity execution = (ExecutionEntity) runtimeService
                .createExecutionQuery().executionId(excId).singleResult();
        String activitiId = execution.getActivityId();
        /*
            循环activitiList 并判断出当前流程所处节点，
            然后得到当前节点实例，根据节点实例获取所有从当前节点出发的路径，
            然后根据路径获得下一个节点实例
        */
        for (ActivityImpl activityImpl : activitiList) {
            String id = activityImpl.getId();
            if (activitiId.equals(id)) {
                System.out.println("当前任务：" + activityImpl.getProperty("name")); //输出某个节点的某种属性
                List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();//获取从某个节点出来的所有线路
                for (PvmTransition tr : outTransitions) {
                    PvmActivity pvmActivity = tr.getDestination(); //获取线路的终点节点
                    System.out.println("下一步任务任务：" + pvmActivity.getProperty("name"));
                    //nodeList.add((String)pvmActivity.getProperty("name"));
                    Map<String, Object> activitiMap = Maps.newHashMap();
                    activitiMap.put("id", pvmActivity.getProcessDefinition().getDeploymentId());
                    activitiMap.put("name", (String) pvmActivity.getProperty("name"));
                    nodeList.add(activitiMap);
                }
                break;
            }
        }
        return nodeList;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public NodeInfoPojo getNextTaskInfo(String procInstanceId) {
        List<Map<String, Object>> nodeList = new ArrayList<>();
        //根据流程ID获取当前任务
        List<Task> currentTasks = taskService.createTaskQuery().processInstanceId(procInstanceId).list();
        //只有并发网关的时候会产生多个
        Task currentTask = currentTasks.get(0);
        String taskId = currentTask.getId();
        ProcessDefinitionEntity processDefinitionEntity = null;
        String id = null;
        //获取流程实例Id信息
        String processInstanceId = taskService.createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();
        //获取流程发布Id信息
        String definitionId = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getProcessDefinitionId();
        processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(definitionId);
        ExecutionEntity execution = (ExecutionEntity) runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        //当前流程节点Id信息
        String activitiId = execution.getActivityId();
        if (StringUtils.isEmpty(activitiId)) {
            String executionId = currentTask.getExecutionId();
            ExecutionEntity executionByTask = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(executionId).singleResult();
            activitiId = executionByTask.getActivityId();
        }
        //获取流程所有节点信息
        List<ActivityImpl> activitiList = processDefinitionEntity.getActivities();
        //遍历所有节点信息
        for (ActivityImpl activityImpl : activitiList) {
            id = activityImpl.getId();
            if (activitiId.equals(id)) {
                //获取下一个节点信息
                return nextMyTaskDefinition(activityImpl, activityImpl.getId(), null, processInstanceId);
            }
        }
        return null;
    }

    /**
     * 获取候选人集合
     *
     * @param identityLinkList
     * @return
     */
    private String getIdentityLinkList(List identityLinkList) {
        List<String> list = new ArrayList<>();
        String strResult = "";
        if (identityLinkList != null && identityLinkList.size() > 0) {
            for (Iterator iterator = identityLinkList.iterator(); iterator
                    .hasNext(); ) {
                IdentityLink identityLink = (IdentityLink) iterator.next();
                list.add(identityLink.getUserId());
                strResult += identityLink.getUserId() + ",";
            }
        }
        strResult = strResult.substring(0, strResult.length());
        return strResult;
    }


    /**
     * 并行网关查询所有子节点信息
     *
     * @param processInstanceId
     * @return
     */
    private NodeInfoPojo getParallelGateway(ActivityImpl activityImpl, String processInstanceId) {
        // 如果排他网关有多条线路信息
        NodeInfoPojo nodeInfoPojo = new NodeInfoPojo();
        nodeInfoPojo.setNodeType("ParallelGaeway");//并行网关
        nodeInfoPojo.setNodeName("并行网关");//并行网关
        nodeInfoPojo.setNeedFlowDirection(false);//需要网关变量标识
        ActivityBehavior activityBehavior = activityImpl.getActivityBehavior();
        List<NodeInfoPojo> nodeInfoPojoList = new ArrayList<NodeInfoPojo>();
        List<Task> currentTasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        for (Task task : currentTasks) {
            List identityLinkList = taskService.getIdentityLinksForTask(task.getId());
            NodeInfoPojo nodeInfoPojoObj = new NodeInfoPojo();
            nodeInfoPojoObj.setAssigneeValue(getIdentityLinkList(identityLinkList));
            nodeInfoPojoObj.setNodeType(WorkFlowManager.USER_TASK);
            nodeInfoPojoObj.setNodeName(task.getName());//设置名称
            nodeInfoPojoObj.setNeedAssignee(true);
            //nextMyTaskDefinition();
            nodeInfoPojoList.add(nodeInfoPojoObj);
            //

        }
        nodeInfoPojo.setBranchNodeInfoPojoList(nodeInfoPojoList);
        return nodeInfoPojo;
    }


    /**
     * 下一个任务节点信息
     * 如果下一个节点为用户任务则直接返回,
     * 如果下一个节点为排他网关, 获取排他网关Id信息, 根据排他网关Id信息和execution获取流程实例排他网关Id为key的变量值,
     * 根据变量值分别执行排他网关后线路中的el表达式, 并找到el表达式通过的线路后的用户任务
     *
     * @param activityImpl      流程节点信息
     * @param activityId        当前流程节点Id信息
     * @param elString          排他网关顺序流线段判断条件
     * @param processInstanceId 流程实例Id信息
     * @return
     */
    private NodeInfoPojo nextMyTaskDefinition(ActivityImpl activityImpl, String activityId, String elString, String processInstanceId) {
        PvmActivity ac = null;
        Object s = null;
        // 如果遍历节点为用户任务并且节点不是当前节点信息
        if ("userTask".equals(activityImpl.getProperty("type"))
                && !activityId.equals(activityImpl.getId())) {
            return getUserTask(activityImpl);
        } else if ("exclusiveGateway".equals(activityImpl.getProperty("type"))) {// 当前节点为exclusiveGateway
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            elString = ac.getId();//当前排他网关变量
            // 如果排他网关只有一条线路信息
            if (outTransitions.size() == 1) {
                return getExclusiveGatewayOnlyOne((ActivityImpl) outTransitions.get(0).getDestination(),
                        activityId, elString, processInstanceId);
            } else if (outTransitions.size() > 1) {
                return getExclusiveGatewayMore(outTransitions, activityId, elString, processInstanceId);
            }
        } else if ("parallelGateway".equals(activityImpl.getProperty("type"))) {
            return getParallelGateway(activityImpl, processInstanceId);
        } else { // 获取节点所有流向线路信息
            String actId = activityImpl.getId();
            if ("end".equals(actId)) {//结束节点
                NodeInfoPojo nodeInfoPojo = new NodeInfoPojo();
                nodeInfoPojo.setNodeType("end");//事件
                nodeInfoPojo.setApproveType("none");//结束审批
                nodeInfoPojo.setNeedAssignee(false);//设置用户标识
                nodeInfoPojo.setNodeName((String) activityImpl.getProperty("name"));//当前节点名称
                return nodeInfoPojo;
            }
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            List<PvmTransition> outTransitionsTemp = null;
            for (PvmTransition tr : outTransitions) {
                ac = tr.getDestination();
                // 获取线路的终点节点
                // 如果流向线路为排他网关
                if ("exclusiveGateway".equals(ac.getProperty("type"))) {
                    outTransitionsTemp = ac.getOutgoingTransitions();
                    // 如果网关路线判断条件为空信息
                    if (StringUtils.isEmpty(elString)) {
                        // 获取流程启动时设置的网关判断条件信息
                        elString = ac.getId();//当前排他网关变量
                    }
                    // 如果排他网关只有一条线路信息
                    if (outTransitionsTemp.size() == 1) {
                        return getExclusiveGatewayOnlyOne((ActivityImpl) outTransitionsTemp.get(0).getDestination(),
                                activityId, elString, processInstanceId);
                    } else if (outTransitionsTemp.size() > 1) {
                        return getExclusiveGatewayMore(outTransitionsTemp, activityId, elString, processInstanceId);
                    }
                } else if ("userTask".equals(ac.getProperty("type"))) {
                    return getUserTask((ActivityImpl) ac);
                } else if ("ParallelGaeway".equals(ac.getProperty("type"))) {
                    outTransitionsTemp = ac.getOutgoingTransitions();
                    return getParallelGateway((ActivityImpl) ac, processInstanceId);
                }
            }
            return null;
        }
        return null;
    }

    /**
     * 用户节点
     *
     * @param activityImpl
     * @return
     */
    private NodeInfoPojo getUserTask(ActivityImpl activityImpl) {
        NodeInfoPojo nodeInfoPojo = new NodeInfoPojo();
        nodeInfoPojo.setNodeName((String) activityImpl.getProperty("name"));//当前节点名称
        nodeInfoPojo.setNodeType("userTask");//用户任务
        // 获取该节点下一个节点信息
        ActivityBehavior activityBehavior = activityImpl.getActivityBehavior();
        if (activityBehavior instanceof UserTaskActivityBehavior) {
            nodeInfoPojo.setApproveType("onlyOne");//单人审批
            TaskDefinition taskDefinition = ((UserTaskActivityBehavior) activityBehavior).getTaskDefinition();
            Task task = (Task) taskDefinition.getAssigneeExpression();
            if (null == task) {
                nodeInfoPojo.setNeedAssignee(false);
            } else {
                String assignee = taskDefinition.getAssigneeExpression().getExpressionText();
                //判断当前用户是否需要设置变量
                if (assignee.indexOf("$") > -1) {//表示需要设置用户信息
                    nodeInfoPojo.setNeedAssignee(true);//设置用户标识
                    nodeInfoPojo.setAssigneeName(
                            assignee.replace("$", "")
                                    .replace("{", "")
                                    .replace("}", "")
                                    .trim());//当前节点用户
                } else {//不需要设置用户信息
                    nodeInfoPojo.setNeedAssignee(false);//设置用户标识
                }
            }
            return nodeInfoPojo;
        } else if (activityBehavior instanceof ParallelMultiInstanceBehavior) {
            nodeInfoPojo.setApproveType("multi");//多人审批
            ParallelMultiInstanceBehavior parallelMultiInstanceBehavior =
                    (ParallelMultiInstanceBehavior) activityBehavior;
            if (null == parallelMultiInstanceBehavior.getCollectionExpression().getExpressionText()) {
                nodeInfoPojo.setNeedAssignee(false);
            } else {
                String assignee = parallelMultiInstanceBehavior.getCollectionExpression().getExpressionText();//变量值
                //判断当前用户是否需要设置变量
                if (assignee.indexOf("$") > -1) {//表示需要设置用户信息
                    nodeInfoPojo.setNeedAssignee(true);//设置用户标识
                    nodeInfoPojo.setAssigneeName(
                            assignee.replace("$", "")
                                    .replace("{", "")
                                    .replace("}", "")
                                    .trim());//当前节点用户
                } else {//不需要设置用户信息
                    nodeInfoPojo.setNeedAssignee(false);//设置用户标识
                }
            }
        }
        return nodeInfoPojo;
    }

    /**
     * 排他网关仅有一条流向
     *
     * @param activityImpl
     * @param activityId
     * @param elString
     * @param processInstanceId
     * @return
     */
    private NodeInfoPojo getExclusiveGatewayOnlyOne(ActivityImpl activityImpl, String activityId, String elString, String processInstanceId) {
        NodeInfoPojo nodeInfoPojo = new NodeInfoPojo();
        nodeInfoPojo.setNodeType("exclusiveGateway");//排他网关
        nodeInfoPojo.setNeedFlowDirection(false);//需要网关变量标识
        List<NodeInfoPojo> nodeInfoPojoList = new ArrayList<NodeInfoPojo>();
        nodeInfoPojoList.add(nextMyTaskDefinition(activityImpl, activityId, elString, processInstanceId));
        nodeInfoPojo.setBranchNodeInfoPojoList(nodeInfoPojoList);
        return nodeInfoPojo;
    }

    /**
     * 排他网关有多条流向
     *
     * @param pvmTransitionList
     * @param activityId
     * @param elString
     * @param processInstanceId
     * @return
     */
    private NodeInfoPojo getExclusiveGatewayMore(List<PvmTransition> pvmTransitionList, String activityId, String elString, String processInstanceId) {
        // 如果排他网关有多条线路信息
        NodeInfoPojo nodeInfoPojo = new NodeInfoPojo();
        nodeInfoPojo.setNodeType("exclusiveGateway");//排他网关
        nodeInfoPojo.setNodeName("排他网关");//排他网关
        nodeInfoPojo.setNeedFlowDirection(true);//需要网关变量标识
        nodeInfoPojo.setFlowDirectionName(elString);//分支映射值
        List<NodeInfoPojo> nodeInfoPojoList = new ArrayList<NodeInfoPojo>();
        for (PvmTransition tr1 : pvmTransitionList) {
            //分支el表达式判断条件
            String branchCondition = tr1.getProperty("conditionText").toString();
            //获取"=="和"!="和值信息
            NodeInfoPojo nodeInfoPojoObj = nextMyTaskDefinition((ActivityImpl) tr1.getDestination(), activityId, elString, processInstanceId);//节点信息
            if (branchCondition.indexOf("==") > -1) {
                nodeInfoPojoObj.setFlowDirectionName(elString);//分支映射值
                nodeInfoPojoObj.setFlowDirectionValue(
                        branchCondition.replace("$", "")
                                .replace("==", "")
                                .replace("{", "")
                                .replace("}", "")
                                .replace(elString, "")
                                .trim());//分支值
            } else {
                nodeInfoPojoObj.setFlowDirectionName(elString);//分支映射值
                nodeInfoPojoObj.setFlowDirectionValue("-10000");//分支值
            }
            //nextMyTaskDefinition();
            //获取分支后下一节点信息
            nodeInfoPojoList.add(nodeInfoPojoObj);
        }
        nodeInfoPojo.setBranchNodeInfoPojoList(nodeInfoPojoList);
        return nodeInfoPojo;
    }

    /**
     * 下一个任务节点信息,
     * 如果下一个节点为用户任务则直接返回,
     * 如果下一个节点为排他网关, 获取排他网关Id信息, 根据排他网关Id信息和execution获取流程实例排他网关Id为key的变量值,
     * 根据变量值分别执行排他网关后线路中的el表达式, 并找到el表达式通过的线路后的用户任务
     *
     * @param activityImpl      流程节点信息
     * @param activityId        当前流程节点Id信息
     * @param elString          排他网关顺序流线段判断条件
     * @param processInstanceId 流程实例Id信息
     * @return
     */
    private TaskDefinition nextTaskDefinition(ActivityImpl activityImpl, String activityId, String elString, String processInstanceId) {
        PvmActivity ac = null;
        Object s = null;
        // 如果遍历节点为用户任务并且节点不是当前节点信息
        if ("userTask".equals(activityImpl.getProperty("type"))
                && !activityId.equals(activityImpl.getId())) {
            // 获取该节点下一个节点信息
            TaskDefinition taskDefinition = ((UserTaskActivityBehavior) activityImpl.getActivityBehavior()).getTaskDefinition();
            return taskDefinition;
        } else if ("exclusiveGateway".equals(activityImpl.getProperty("type"))) {// 当前节点为exclusiveGateway
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            //outTransitionsTemp = ac.getOutgoingTransitions();
            // 如果网关路线判断条件为空信息
//          if (StringUtils.isEmpty(elString)) {
            // 获取流程启动时设置的网关判断条件信息
            elString = getGatewayCondition(activityImpl.getId(), processInstanceId);
//          }
            // 如果排他网关只有一条线路信息
            if (outTransitions.size() == 1) {
                return nextTaskDefinition((ActivityImpl) outTransitions.get(0).getDestination(), activityId, elString, processInstanceId);
            } else if (outTransitions.size() > 1) {
                // 如果排他网关有多条线路信息
                for (PvmTransition tr1 : outTransitions) {
                    s = tr1.getProperty("conditionText");
                    // 获取排他网关线路判断条件信息
                    // 判断el表达式是否成立
                    if (isCondition(activityImpl.getId(), StringUtils.trim(s.toString()), elString)) {
                        return nextTaskDefinition((ActivityImpl) tr1.getDestination(), activityId, elString, processInstanceId);
                    }
                }
            }
        } else { // 获取节点所有流向线路信息
            List<PvmTransition> outTransitions = activityImpl.getOutgoingTransitions();
            List<PvmTransition> outTransitionsTemp = null;
            for (PvmTransition tr : outTransitions) {
                ac = tr.getDestination();
                // 获取线路的终点节点
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
                        return nextTaskDefinition((ActivityImpl) outTransitionsTemp.get(0).getDestination(), activityId, elString, processInstanceId);
                    } else if (outTransitionsTemp.size() > 1) {
                        // 如果排他网关有多条线路信息
                        for (PvmTransition tr1 : outTransitionsTemp) {
                            s = tr1.getProperty("conditionText");
                            // 获取排他网关线路判断条件信息
                            // 判断el表达式是否成立
                            if (isCondition(ac.getId(), StringUtils.trim(s.toString()), elString)) {
                                return nextTaskDefinition((ActivityImpl) tr1.getDestination(), activityId, elString, processInstanceId);
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
        return null;
    }

    /**
     * 查询流程启动时设置排他网关判断条件信息
     *
     * @param gatewayId         排他网关Id信息, 流程启动时设置网关路线判断条件key为网关Id信息
     * @param processInstanceId 流程实例Id信息
     * @return
     */
    private String getGatewayCondition(String gatewayId, String processInstanceId) {
        Execution execution = runtimeService.createExecutionQuery().processInstanceId(processInstanceId).singleResult();
        Object object = runtimeService.getVariable(execution.getId(), gatewayId);
        return object == null ? "" : object.toString();
    }

    /**
     * 根据key和value判断el表达式是否通过信息
     *
     * @param key   el表达式key信息
     * @param el    el表达式信息
     * @param value el表达式传入值信息
     * @return
     */
    private boolean isCondition(String key, String el, String value) {
        ExpressionFactory factory = new ExpressionFactoryImpl();
        SimpleContext context = new SimpleContext();
        context.setVariable(key, factory.createValueExpression(value, String.class));
        ValueExpression e = factory.createValueExpression(context, el, boolean.class);
        return (Boolean) e.getValue(context);
    }


    private Map<String, Object> getVariableByTaskId(String taskId, boolean isLocal) {
        Map<String, Object> variablesMap = new HashMap<String, Object>();
        if (isLocal) {
            variablesMap = taskService.getVariablesLocal(taskId);
        } else {
            variablesMap = taskService.getVariables(taskId);
        }
        return variablesMap;
    }

    /**
     * 根据任务ID和节点ID获取活动节点 <br>
     *
     * @param taskId     任务ID
     * @param activityId 活动节点ID  如果为null或""，则默认查询当前活动节点  如果为"end"，则查询结束节点 <br>
     * @return
     */
    private ActivityImpl findActivitiImpl(String taskId, String activityId) {
        // 取得流程定义
        ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);
        // 获取当前活动节点ID
        if (StringUtils.isEmpty(activityId)) {
            activityId = findTaskById(taskId).getTaskDefinitionKey();
        }
        // 根据流程定义，获取该流程实例的结束节点
        if (activityId.toUpperCase().equals("END")) {
            for (ActivityImpl activityImpl : processDefinition.getActivities()) {
                List<PvmTransition> pvmTransitionList = activityImpl
                        .getOutgoingTransitions();
                if (pvmTransitionList.isEmpty()) {
                    return activityImpl;
                }
            }
        }
        // 根据节点ID，获取对应的活动节点
        ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition)
                .findActivity(activityId);
        return activityImpl;
    }

    /**
     * 根据任务ID获取流程定义
     *
     * @param taskId 任务ID
     * @return
     */
    private ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(
            String taskId) {
        // 取得流程定义
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(findTaskById(taskId)
                        .getProcessDefinitionId());
        if (processDefinition == null) {
            throw new RuntimeException("流程定义未找到!");
        }
        return processDefinition;
    }

    /**
     * 根据任务ID获得任务实例
     *
     * @param taskId 任务ID
     * @return
     */
    private TaskEntity findTaskById(String taskId) {
        TaskEntity task = (TaskEntity) taskService.createTaskQuery().taskId(
                taskId).singleResult();
        if (task == null) {
            throw new RuntimeException("任务实例未找到!");
        }
        return task;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public void activationWorkFlow(String processInstanceId) {
        runtimeService.activateProcessInstanceById(processInstanceId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public void hangWorkFlow(String processInstanceId) {
        //根据流程实例ID获取taskId
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        for (Task task : tasks) {
            String taskId = task.getId();
            taskService.addComment(taskId, null, "挂起");//添加审批意见
            runtimeService.suspendProcessInstanceById(processInstanceId);//挂起流程
        }
    }

    /**
     * 流程实例对象简单打印
     *
     * @param processInstance
     */
    public void simpleLoggerForProcessInstance(ProcessInstance processInstance) {
        logger.info("#################流程start#######################");
        logger.info("流程梳理所属流程定义id：" + processInstance.getProcessDefinitionId());
        logger.info("流程实例的id：" + processInstance.getProcessInstanceId());
        logger.info("流程实例的执行id：" + processInstance.getId());
        logger.info("流程当前的活动（结点）id：" + processInstance.getActivityId());
        logger.info("业务标识：" + processInstance.getBusinessKey());
        logger.info("#################流程End#########################");
    }
}
