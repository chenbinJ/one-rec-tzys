package com.ztgeo.general.component.penghao;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.util.BooleanUtil;
import com.google.common.collect.Maps;
import com.ztgeo.general.component.chenbin.InterfaceRequestHandleComponent;
import com.ztgeo.general.config.webscoket.WebSocket;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.Depart;
import com.ztgeo.general.entity.Position;
import com.ztgeo.general.entity.User;
import com.ztgeo.general.entity.activity.Approve;
import com.ztgeo.general.entity.activity.HistoryPojo;
import com.ztgeo.general.entity.service_data.pub_data.*;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.mapper.PositionUserMapper;
import com.ztgeo.general.mapper.UserMapper;
import com.ztgeo.general.mapper.activity.ApproveMapper;
import com.ztgeo.general.mapper.chenbin.IntfManagerMapper;
import com.ztgeo.general.mapper.chenbin.StepManagerMapper;
import com.ztgeo.general.mapper.penghao.DepartUserMapper;
import com.ztgeo.general.mapper.penghao.SjPowerStepPositionMapper;
import com.ztgeo.general.service.activity.ApproveService;
import com.ztgeo.general.service.activity.WorkFlowOperateService;
import com.ztgeo.general.util.CommonUtils;
import com.ztgeo.general.util.chenbin.IDUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
@Slf4j
public class ApproveComponent {

    @Autowired
    private TaskService taskService;
    @Autowired
    private ApproveMapper approveMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StepManagerMapper stepManagerMapper;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private InterfaceRequestHandleComponent interfaceRequestHandleComponent;
    @Autowired
    private WorkFlowOperateService workFlowOperateService;
    @Autowired
    private PositionUserMapper positionUserMapper;
    @Autowired
    private SjPowerStepPositionMapper sjPowerStepPositionMapper;
    @Autowired
    private WebSocket webSocket;
    @Autowired
    private IntfManagerMapper intfManagerMapper;
    @Autowired
    private DepartUserMapper departUserMapper;
    @Autowired
    private ApproveService approveService;

    /**
     * 通过收件编号,审批人查询taskId(approve)
     * @param approveFkId
     * @param
     * @return
     */
    public Approve getApproveByPersonOrFkId(String approveFkId){
        Approve approve=approveMapper.getApproveByPersonOrFkId(approveFkId,UserUtil.checkAndGetUser());
        if (null==approve){
            throw new ZtgeoBizException(MsgManager.APPROVE_IS_NULL);
        }
        return approve;
    }

    /**
     * 1.通过实例id username查询是否有代办
     * 2.返回最后一步数据
     * @param processId 实例id
     * @return
     */
    public Object findLaststepData(String processId){
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        //根据实例查询正在执行的任务节点
        List<Task> taskList=taskService.createTaskQuery().processInstanceId(processId).list();
        for (int i=0;i<taskList.size();i++){
            Task task=taskList.get(i);
            if (StringUtils.isNotEmpty(task.getAssignee())) {
                if (task.getAssignee().equals(UserUtil.checkAndGetUser())) {
                    Approve approve=approveMapper.selectByTaskId(task.getId());
                    return rv.data(approve.getApproveTaskId());
                }
            }
        }
        HistoricTaskInstance oldaskInstance=null;
        //通过实例id查询历史数据根据taskId找到stepId对应positionId权限列表
        List<HistoricTaskInstance> historicTaskInstanceList = ProcessEngines.getDefaultProcessEngine().
                getHistoryService().createHistoricTaskInstanceQuery().
                processInstanceId(processId).
                orderByTaskCreateTime().desc().list();
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
            if (historicTaskInstance.getEndTime() == null) {
                if (StringUtils.isNotEmpty(historicTaskInstance.getAssignee())) {
                    //先判断审批人是不是自己如果是自己直接返回信息
                    if (historicTaskInstance.getAssignee().equals(UserUtil.checkAndGetUser())) {
                        return rv.data(historicTaskInstance.getId());
                    }
                }
            }
            if (historicTaskInstance.getEndTime() != null){
                    if (historicTaskInstance.getAssignee().equals(UserUtil.checkAndGetUser())){
                        return rv.data(historicTaskInstance.getId());
                    }
                    //查找步骤表数据
                    SJ_Act_Step sjActStep = stepManagerMapper.selectStepByMouldId(historicTaskInstance.getTaskDefinitionKey());
                    //根据步骤表和岗位查询ReadOnly数据
                    //根据userId获得职务
                    List<String> list = new ArrayList<>();
                    //用户可以有多个职务
                    List<Position> positionByUid = positionUserMapper.selectPositionByUid(UserUtil.getUerId());
                    if (positionByUid == null || positionByUid.size() == 0) {
                        return new ZtgeoBizException(MsgManager.POSITION_USER_NULL);
                    }
                    for (Position tion : positionByUid) {
                        list.add(tion.getId());
                    }
                    Integer writeInteger = sjPowerStepPositionMapper.
                            findPowerStepByReadOnly(sjActStep.getStepId(), BizOrBizExceptionConstant.POWER_LEVEL_WRITE, list);
                    if (writeInteger < 1) {
                        Integer readyInteger = sjPowerStepPositionMapper.
                                findPowerStepByReadOnly(sjActStep.getStepId(), BizOrBizExceptionConstant.POWER_LEVEL_READ, list);
                        if (readyInteger > 0) {
                            oldaskInstance = historicTaskInstance;
                            return rv.data(oldaskInstance.getId());
                        }
                    } else {
                        continue;
                    }
            }
        }
        throw new ZtgeoBizException(MsgManager.USER_FLOW_QXNULL);
    }


    /**
     * 1.通过实例id username查询是否有代办
     * @param historicTaskInstance
     * @return
     */
    public Object  findLaststep(HistoricTaskInstance historicTaskInstance,Map<String,Object> map){
        map=Maps.newHashMap();//初始化
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        //根据实例查询正在执行的任务节点
        HistoryPojo ReadHistoryPojo=new HistoryPojo();
        HistoryPojo HandledHistoryPojo=new HistoryPojo();
            HistoricTaskInstance oldaskInstance=null;
            if (historicTaskInstance.getEndTime() != null) {
                if (StringUtils.isNotEmpty(historicTaskInstance.getAssignee())) {
                    if (historicTaskInstance.getAssignee().equals(UserUtil.checkAndGetUser())) {
                        List<Comment> taskList = taskService.getTaskComments(historicTaskInstance.getId());//对用历史完成后的任务ID
                        for (Comment com : taskList) {
                            HandledHistoryPojo.setApprovePerson(historicTaskInstance.getAssignee());
                            HandledHistoryPojo.setApproveNode(historicTaskInstance.getName());
                            HandledHistoryPojo.setTaskId(historicTaskInstance.getId());
                            HandledHistoryPojo.setApproveDefinitionKey(historicTaskInstance.getTaskDefinitionKey());
                            String myComment = com.getFullMessage();
                            if (com instanceof CommentEntity) {//由于乱码读取问题
                                myComment = ((CommentEntity) com).getMessage();
                            }
//                        String[] myCommentArg = myComment.split("##");
                            //审批结果##审批人##审批时间##审批备注
                            HandledHistoryPojo.setApproveState(myComment);
                            HandledHistoryPojo.setProcessInstanceId(historicTaskInstance.getProcessInstanceId());
                            map.put("HandledHistoryPojo", HandledHistoryPojo);
                            return map;
                        }
                    }
                }
                //查找步骤表数据
                SJ_Act_Step sjActStep = stepManagerMapper.selectStepByMouldId(historicTaskInstance.getTaskDefinitionKey());
                //根据步骤表和岗位查询ReadOnly数据
                //根据userId获得职务
                List<String> list = new ArrayList<>();
                //用户可以有多个职务
                List<Position> positionByUid = positionUserMapper.selectPositionByUid(UserUtil.getUerId());
                if (positionByUid == null || positionByUid.size() == 0) {
                    return new ZtgeoBizException(MsgManager.POSITION_USER_NULL);
                }
                for (Position tion : positionByUid) {
                    list.add(tion.getId());
                }
                Integer writeInteger = sjPowerStepPositionMapper.
                        findPowerStepByReadOnly(sjActStep.getStepId(), BizOrBizExceptionConstant.POWER_LEVEL_WRITE, list);
                if (writeInteger < 1) {
                    Integer readyInteger = sjPowerStepPositionMapper.
                            findPowerStepByReadOnly(sjActStep.getStepId(), BizOrBizExceptionConstant.POWER_LEVEL_READ, list);
                    if (readyInteger > 0) {
                        oldaskInstance = historicTaskInstance;
                        //只读状态
                        ReadHistoryPojo.setApprovePerson(oldaskInstance.getAssignee());
                        ReadHistoryPojo.setApproveNode(oldaskInstance.getName());
                        ReadHistoryPojo.setTaskId(oldaskInstance.getId());
                        ReadHistoryPojo.setApproveDefinitionKey(historicTaskInstance.getTaskDefinitionKey());
                        ReadHistoryPojo.setProcessInstanceId(oldaskInstance.getProcessInstanceId());
                        map.put("ReadHistoryPojo", ReadHistoryPojo);
                        return map;
                    }
                }
            }
        return  map;
    }











    /**
     * 设置签收人通过受理编号查询实例
     * @param username
     * @param acceptanceNumber
     * @return
     */
    public  Object getTaskSettingupSignature(String username,String acceptanceNumber){
        System.out.println(username+","+acceptanceNumber);
        List<Approve> approveList=this.approveMapper.getApproveByApproveFkId(acceptanceNumber);
        //获得实例
        ProcessInstance processInstance=runtimeService.createProcessInstanceQuery().processInstanceId(approveList.get(0).getApproveProcessinstanceid()).singleResult();
        //获得正在执行
        List<Task> taskList=taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();
        Task userTask=null;
        for (int i=0;i<taskList.size();i++){
            Task taskCompleteObj = taskList.get(i);
            List identityLinkList = taskService.getIdentityLinksForTask(taskCompleteObj.getId());
            //遍历待签收集合
            for (Iterator iterator = identityLinkList.iterator(); iterator
                    .hasNext(); ) {
                IdentityLink identityLink = (IdentityLink) iterator.next();
                //判断待签收人是否包含username
                if (identityLink.getUserId().equals(username)){
                    userTask=taskCompleteObj;
                    taskService.claim(userTask.getId(),username);
                }
            }
        }
        //二手房水电气默认受理自动完成
        if (null != approveList && taskList.size()==1){
                userTask = taskList.get(0);
                userTask.setAssignee(username);
        }
        //设置签收人
        if (StringUtils.isBlank(userTask.getAssignee())||userTask.getAssignee().equals(username)){
            userTask.setAssignee(username);
        }
        //查询task对应approve
        Approve approve=this.approveMapper.selectByTaskId(userTask.getId());
        approve.setApprovePerson(username);
        approveMapper.updateByPrimaryKeySelective(approve);
        return  approve;

    }

    public Object AutoSubmittedFlow(String taskId,HttpServletRequest request){
        Map<String,String> mapParmeter=new HashMap<>();
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
        ProcessInstance processInstance=runtimeService.createProcessInstanceQuery().
                processInstanceId(task.getProcessInstanceId()).singleResult();
        SubmittedFlow(taskId,processInstance.getProcessInstanceId(),"agree",UserUtil.checkAndGetUser(),mapParmeter);
         rv.setMessage(MsgManager.OPERATING_SUCCESS);
        return rv;
    }


    public List<Task> SubmittedFlow(String taskId,String processId,String approveResult,String username,Map<String,String> mapParmeter){
        List<Depart> departList = departUserMapper.getDepartByUserId(UserUtil.getUerId());
        String departName="";
        if (null == departList || departList.size()==0) {
            if (this.findUserByAdministrators(UserUtil.getUerId())==true){
                departName="管理员特殊部门,";
            }
        }else {
            for (Depart depart:departList) {
                departName+=depart.getName()+",";
            }
        }
        List<Task> taskCompletes = null;
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
        if (null==processInstance){
            throw  new ZtgeoBizException(MsgManager.PROCESS_DX_NULL);
        }
        String comment="";
        if (approveResult.equals("agree")){
            comment="同意";
        }else {
            comment="不同意";
        }
        Map<String, Object> map = (Map<String, Object>) workFlowOperateService.taskSingleWorkFLowByProcessInstanceId(processId, approveResult, taskId, username, null, comment, null);
        String ageree="";
        List<Approve> list=this.approveMapper.findQdTimeByName(processId);
        if ("discard".equals(approveResult)) {//废弃
            Approve approve=this.approveMapper.selectByTaskId(taskId);//查询审批对象
            approve.setApproveTaskEndTime(new Date());//结束时间
            approve.setApprovePerson(username);//设置审批人
            approve.setApproveState(2);//设置状态挂起
            approve.setApproveCreateDapartment(departName);
            approveMapper.updateByPrimaryKeySelective(approve);
            webSocket.sendMessage(UserUtil.checkAndGetUser(),webSocket.SendResultMsagge(username,UserUtil.getUerId()));
        } else if ("agree".equals(approveResult) /*|| "disagree".equals(approvePojo.getApproveResult())*/) {//同意或驳回修改
            //如果当前节点为领导批示，并且操作成功、审批agree则进行流程完成操作
            Boolean isEndFlag = workFlowOperateService.processIsEnd(processId);
            if (isEndFlag) {//流程完成
                Approve approve = new Approve();
                approve.setId(IDUtil.getProce());
                approve.setApproveFkId(list.get(0).getApproveFkId());
                approve.setApproveType(list.get(0).getApproveType());
                approve.setApproveProcessinstanceid(processId);
                approve.setApproveStatus("结束");
                approve.setApproveTaskEndTime(new Date());//结束时间
                approveService.insertSelective(approve);
            } else {
                //获取完成后的任务项
                taskCompletes = workFlowOperateService.getTaskByProcessInstanceId(processId);
                //根据完成后的任务项ID查询库里是否存在,存在就return
                Task taskComplete = null;
                String taskAssignee = null;
                if (taskCompletes.size() > 1) {//表示两个活动项，判断哪一个为当前人
                    for (int j = 0; j < taskCompletes.size(); j++) {
                        Task taskCompleteObj = taskCompletes.get(j);
                        initApproveDataWhenNewTaskInit(taskCompleteObj.getId(),processId);//生成approve数据
                        initParticipantMen(username,taskCompleteObj.getId(),false);//更新参与者数据
//                        Approve selectapprove = approveService.getApproveInfoByTaskId(taskCompleteObj.getId());
//                        if (selectapprove == null) {
//                            List identityLinkList = taskService.getIdentityLinksForTask(taskCompleteObj.getId());
//                            ageree = "";
//                            for (Iterator iterator = identityLinkList.iterator(); iterator
//                                    .hasNext(); ) {
//                                IdentityLink identityLink = (IdentityLink) iterator.next();
//                                User user=userMapper.selectUserByName(identityLink.getUserId());
//                                webSocket.sendMessage(username,webSocket.SendResultMsagge(identityLink.getUserId(),user.getId()));
//                                ageree += identityLink.getUserId() + ",";
//                            }
//                            Approve approve = new Approve();
//                            if (null != taskCompleteObj.getAssignee()){
//                                approve.setApprovePerson(taskComplete.getAssignee());
//                            }
//                            approve.setId(CommonUtils.getUUID());
//                            String aggreeName=ageree.substring(0, ageree.length() - 1);
//                            approve.setApprovePaticipant(aggreeName);
//                            approve.setApproveType(list.get(0).getApproveType());
//                            approve.setApproveFkId(list.get(0).getApproveFkId());
//                            approve.setApproveCreateDapartment(list.get(0).getApproveCreateDapartment());
//                            approve.setApproveProcessinstanceid(processId);
//                            approve.setApproveStartTime(list.get(0).getApproveStartTime());
//                            approve.setApproveTaskId(taskCompleteObj.getId());
//                            approve.setApproveTaskStartTime(new Date());
//                            approve.setApproveStatus(taskCompleteObj.getName());
//                            approveMapper.insertSelective(approve);
//                        }
                    }
                    //循环新增
                } else if (taskCompletes.size() == 1) {
                    initApproveDataWhenNewTaskInit(taskCompletes.get(0).getId(),processId);//生成approve数据
                    initParticipantMen(username,taskCompletes.get(0).getId(),false);//更新参与者数据
//                    Approve selectapprove = approveService.getApproveInfoByTaskId(taskCompletes.get(0).getId());
//                    if (selectapprove == null) {
//                        List identityLinkList = taskService.getIdentityLinksForTask(taskCompletes.get(0).getId());
//                            for (Iterator iterator = identityLinkList.iterator(); iterator
//                                    .hasNext(); ) {
//                                IdentityLink identityLink = (IdentityLink) iterator.next();
//                                User user=userMapper.selectUserByName(identityLink.getUserId());
//                                webSocket.sendMessage(username,webSocket.SendResultMsagge(identityLink.getUserId(),user.getId()));
//                                ageree += identityLink.getUserId() + ",";
//                            }
//                            Approve approve = new Approve();
//                            if (null !=taskCompletes.get(0).getAssignee()){
//                                approve.setApprovePerson(taskComplete.getAssignee());
//                            }
//                            approve.setId(CommonUtils.getUUID());
//                            approve.setApprovePaticipant(ageree.substring(0, ageree.length() - 1));
//                            approve.setApproveProcessinstanceid(processId);
//                            approve.setApproveType(list.get(0).getApproveType());
//                            approve.setApproveFkId(list.get(0).getApproveFkId());
//                            approve.setApproveStartTime(list.get(0).getApproveStartTime());
//                            approve.setApproveCreateDapartment(list.get(0).getApproveCreateDapartment());
//                            approve.setApproveTaskId(taskCompletes.get(0).getId());
//                            approve.setApproveTaskStartTime(new Date());
//                            approve.setApproveStatus(taskCompletes.get(0).getName());
//                            approveMapper.insertSelective(approve);
//                        }
                }
            }
        }
        return taskCompletes;
    }


    public void initApproveDataWhenNewTaskInit(String taskId,String processId){
        Task approveTask = taskService.createTaskQuery().taskId(taskId).singleResult();
        insertApprove(approveTask.getAssignee(),approveTask.getId(),approveTask.getName(),processId);
    }

    public void initApproveDataWhenNewTaskInit(DelegateTask approveTask, String processId){
        insertApprove(approveTask.getAssignee(),approveTask.getId(),approveTask.getName(),processId);
    }

    public void initApproveDataWhenNewTaskInit(Task task, String processId){
        insertApprove(task.getAssignee(),task.getId(),task.getName(),processId);
    }

    public void insertApprove(String assignee,String taskId,String taskName,String processId){
        List<Approve> list=this.approveMapper.findQdTimeByName(processId);
        System.err.println("=="+assignee);
        Approve selectapprove = approveService.getApproveInfoByTaskId(taskId);
        if (selectapprove == null) {
            Approve approve = new Approve();
            if(StringUtils.isNotBlank(assignee)){
                approve.setApprovePerson(assignee);
            }
            approve.setId(IDUtil.getProce());
            approve.setApproveType(list.get(0).getApproveType());
            approve.setApproveFkId(list.get(0).getApproveFkId());
            approve.setApplyDepartment(list.get(0).getApplyDepartment());
            approve.setApproveProcessinstanceid(processId);
            approve.setApproveStartTime(list.get(0).getApproveStartTime());
            approve.setApproveTaskId(taskId);
            approve.setApproveTaskStartTime(new Date());
            approve.setApproveStatus(taskName);
            approveMapper.insertSelective(approve);
        }
    }

    public void initParticipantMen(String username,String taskId,boolean isUseThisName){
        List identityLinkList = taskService.getIdentityLinksForTask(taskId);
        Approve approve = approveService.getApproveInfoByTaskId(taskId);
        if(approve!=null) {
            String ageree = "";
            if(identityLinkList!=null && identityLinkList.size()>0) {
                for (Iterator iterator = identityLinkList.iterator(); iterator
                        .hasNext(); ) {
                    IdentityLink identityLink = (IdentityLink) iterator.next();
                    User user = userMapper.selectUserByName(identityLink.getUserId());
//                    webSocket.sendMessage(username, webSocket.SendResultMsagge(user.getUsername(), user.getId()));
                    ageree += identityLink.getUserId() + ",";
                }
            }else if(isUseThisName){
                User user = userMapper.selectUserByName(username);
//                webSocket.sendMessage(username, webSocket.SendResultMsagge(user.getUsername(), user.getId()));
                ageree = username;
            }
            String aggreeName = null;
            if(StringUtils.isNotBlank(ageree) && ageree.contains(",")) {
                aggreeName = ageree.substring(0, ageree.length() - 1);
            } else if(StringUtils.isNotBlank(ageree)){
                aggreeName = ageree;
            }
            approve.setApprovePaticipant(aggreeName);
            approveMapper.updateByPrimaryKeySelective(approve);
        }
    }



    /**
     * 发送不动产抵押数据
     * @param taskId
     * @param mapParmeter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW , isolation = Isolation.DEFAULT
            ,rollbackFor = RuntimeException.class)
    public Map<String,Object> getActivityAutoInterface(String taskId, Map<String,String> mapParmeter){
        Task task=taskService.createTaskQuery().taskId(taskId).singleResult();//获得task对象
        String stepId=stepManagerMapper.selectStepIdByStepMouldId(task.getTaskDefinitionKey());//获得步骤id
        List<SJ_Power_Step_Interface> sjPowerStepInterface=stepManagerMapper.findstepIntfsByStepId(stepId);//获取自动接口
        Approve approve=approveMapper.selectByTaskId(taskId);
        Map<String,Object> map=new HashMap<>();
        //为空说明没有自动执行接口
        if (null==sjPowerStepInterface || sjPowerStepInterface.size()==0){
            log.error("没有自动接口");
            System.out.println("没有自动接口");
            return null;
        }
        //自动接口只可以有一个
        if (sjPowerStepInterface.size()==1){
            SJ_Interface intf = intfManagerMapper.selectIntfById(sjPowerStepInterface.get(0).getInterfaceId());
            System.err.println("intf为："+ JSONObject.toJSONString(intf));
            //参数必须有一个
            if (intf.getParamVoList().size()==1){
                //通过taskId 调用sjsq收件申请
                SJ_Sjsq sjSjsq= interfaceRequestHandleComponent.getSjsqInfoFromTask(approve.getApproveFkId(),taskId);
                mapParmeter.put("commonInterfaceAttributer",JSONObject.toJSONString(sjSjsq));
                mapParmeter.put("interfaceId",sjPowerStepInterface.get(0).getInterfaceId());
                mapParmeter.put("nextTaskId",taskId);
                mapParmeter.put("stepId",stepId);
                map=interfaceRequestHandleComponent.requestHandle(mapParmeter);
            } else {
                log.error("该接口有多个参数");
            }
        }else {
            log.error("该节点下面有多个节点（只允许有一个）");
        }
        return  map;
    }




    public boolean findPowerByTask(String taskId){
        if (BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(UserUtil.getUerId()).getIsSuperAdmin())) {
            return  true;
        }
        //1.根据task是否审批人为userName
        Approve approve=approveMapper.findApproveByTask(UserUtil.checkAndGetUser(),taskId);
        if (null == approve){
            throw  new ZtgeoBizException(MsgManager.FLOW_PROCESSNAME_NOT);
        }
        if (approve.getApproveTaskEndTime()!=null) {
            return false;
        }
        return  true;
    }

    public boolean findUserByAdministrators(String userId){
        if (BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(userId).getIsSuperAdmin())) {
            return  true;
        }
        return false;
    }


}
