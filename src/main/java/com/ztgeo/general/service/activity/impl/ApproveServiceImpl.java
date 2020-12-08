package com.ztgeo.general.service.activity.impl;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.util.BooleanUtil;
import com.google.common.collect.Maps;
import com.ztgeo.general.biz.chenbin.impl.StepManagerBizImpl;
import com.ztgeo.general.biz.service_biz.chenbin.impl.ExceptionRecordBiz;
import com.ztgeo.general.component.chenbin.OtherComponent;
import com.ztgeo.general.component.chenbin.StartProcDataDealComponent;
import com.ztgeo.general.component.chenbin.StepManagerComponent;
import com.ztgeo.general.component.penghao.ActivitiComponent;
import com.ztgeo.general.component.penghao.ApproveComponent;
import com.ztgeo.general.component.penghao.SjFjtmComponent;
import com.ztgeo.general.component.penghao.StepComponent;
import com.ztgeo.general.component.pubComponent.ReceiptNumberComponent;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.Depart;
import com.ztgeo.general.entity.Position;
import com.ztgeo.general.entity.User;
import com.ztgeo.general.entity.activity.Approve;
import com.ztgeo.general.entity.activity.HistoryPojo;
import com.ztgeo.general.entity.activity.NodeInfoPojo;
import com.ztgeo.general.entity.extend.DataGridResult;
import com.ztgeo.general.entity.extend.Temporary;
import com.ztgeo.general.entity.service_data.pub_data.*;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.mapper.PositionUserMapper;
import com.ztgeo.general.mapper.UserMapper;
import com.ztgeo.general.mapper.activity.ApproveMapper;
import com.ztgeo.general.mapper.chenbin.StepManagerMapper;
import com.ztgeo.general.mapper.penghao.DepartUserMapper;
import com.ztgeo.general.mapper.penghao.SjPowerStepPositionMapper;
import com.ztgeo.general.mapper.penghao.TemplateCategoryMapper;
import com.ztgeo.general.service.activity.ActivityService;
import com.ztgeo.general.service.activity.ApproveService;
import com.ztgeo.general.service.activity.WorkFlowOperateService;
import com.ztgeo.general.util.chenbin.ErrorDealUtil;
import com.ztgeo.general.util.chenbin.IDUtil;
import com.ztgeo.general.util.chenbin.SysPubDataDealUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Slf4j
@Service
public class ApproveServiceImpl implements ApproveService {

    private Logger logger = Logger.getLogger(Approve.class);

    @Autowired
    private ApproveComponent approveComponent;
    @Autowired
    private ApproveMapper approveMapper;
    @Autowired
    private DepartUserMapper departUserMapper;
    @Autowired
    private StepManagerComponent stepManagerComponent;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private StepComponent stepComponent;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private StepManagerBizImpl stepManagerBiz;
    @Autowired
    private StepManagerMapper stepManagerMapper;
    @Autowired
    private WorkFlowOperateService workFlowOperateService;
    @Autowired
    private ReceiptNumberComponent receiptNumberComponent;
    @Autowired
    private SjFjtmComponent sjFjtmComponent;
    @Autowired
    private StartProcDataDealComponent startProcDataDealComponent;
    @Autowired
    private TemplateCategoryMapper templateCategoryMapper;
    @Autowired
    private ActivitiComponent activitiComponent;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ExceptionRecordBiz exceptionRecordBiz;
    @Autowired
    private OtherComponent otherComponent;
    @Autowired
    private SjPowerStepPositionMapper sjPowerStepPositionMapper;
    @Autowired
    private PositionUserMapper positionUserMapper;


    @Override
    public Object deleteByPrimaryKey(String id) {
        return null;
    }

    @Override
    public Object insert(Approve record) {
        return approveMapper.insert(record);
    }

    @Override
    public Object insertSelective(Approve record) {
        return approveMapper.insertSelective(record);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object updateApproveByTaskId(Approve approve) {
        return approveMapper.updateApproveByTaskId(approve);
    }

    @Override
    public Object findjudgeTaskAnduserName(String taskId) {
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        Approve approve = this.approveMapper.selectByTaskId(taskId);
        if (approveComponent.findUserByAdministrators(UserUtil.getUerId()) == true) {
            rv.data(approve);
            return rv;
        }
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        HistoricTaskInstance historicTaskInstance = null;
        //说明节点已经审批过了
        if (null == task) {
            //历史查询节点
            historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            if (null != historicTaskInstance) {
                if (null != historicTaskInstance.getEndTime() && historicTaskInstance.getAssignee().equals(UserUtil.checkAndGetUser())) {
                    //说明节点已经结束,查询登录人最后一步完成的节点
                    List<Approve> ywcApprove = approveMapper.selectApproveByYwcOrName(UserUtil.checkAndGetUser(), historicTaskInstance.getProcessInstanceId());
                    ywcApprove.get(0).setPermission(BizOrBizExceptionConstant.POWER_LEVEL_WRITE);//写权限
                    rv.data(ywcApprove.get(0));
                    return rv;
                }
                //查询是否含有只读权限
                SJ_Act_Step act_step=stepManagerMapper.selectStepByMouldId(historicTaskInstance.getTaskDefinitionKey());
                log.info("aaaaaaaa");
                List<SJ_Power_Step_Position> positionList=sjPowerStepPositionMapper.getPositionByStepIdOrQx(act_step.getStepId(),BizOrBizExceptionConstant.POWER_LEVEL_READ);
                if (positionList.size() != 0) {
                    log.info("v");
                    List<Position> positions = positionUserMapper.selectPositionByUid(UserUtil.getUerId());
                    for (SJ_Power_Step_Position stepPosition : positionList) {
                        for (Position position : positions) {
                            //判断是否相同  相同会有只读权限
                            if (stepPosition.getPositionId().equals(position.getId())) {
                                rv.data(approve);
                                return  rv;
                            }
                        }
                    }
                }
                if (null != historicTaskInstance.getAssignee() && null != historicTaskInstance.getEndTime()) {
                    //分割字符串进行判断
                    String[] participate = approve.getApprovePaticipant().split(",");
                    for (int i = 0; i < participate.length; i++) {
                        if (participate[i].equals(UserUtil.checkAndGetUser())) {
                            approve.setPermission(BizOrBizExceptionConstant.POWER_LEVEL_READ);
                            rv.data(approve);
                            return rv;
                        }
                    }
                }
            }
        } else {
            //判断是否在参与人里面
            if (null == task.getAssignee() && null == approve.getApproveTaskEndTime()) {
                //分割字符串进行判断
                String[] participate = approve.getApprovePaticipant().split(",");
                for (int i = 0; i < participate.length; i++) {
                    if (participate[i].equals(UserUtil.checkAndGetUser())) {
                        approve.setPermission(BizOrBizExceptionConstant.POWER_LEVEL_READ);
                        rv.data(approve);
                        return rv;
                    }
                }
            } else if (task.getAssignee().equals(UserUtil.checkAndGetUser())) {
                approve.setPermission(BizOrBizExceptionConstant.POWER_LEVEL_WRITE);
                return rv.data(approve);
            }
            //查询是否含有只读权限
            SJ_Act_Step act_step=stepManagerMapper.selectStepByMouldId(task.getTaskDefinitionKey());
            List<SJ_Power_Step_Position> positionList=sjPowerStepPositionMapper.getPositionByStepIdOrQx(act_step.getStepId(),BizOrBizExceptionConstant.POWER_LEVEL_READ);
            if (positionList.size() != 0) {
                List<Position> positions = positionUserMapper.selectPositionByUid(UserUtil.getUerId());
                for (SJ_Power_Step_Position stepPosition : positionList) {
                    for (Position position : positions) {
                        //判断是否相同  相同会有只读权限
                        if (stepPosition.getPositionId().equals(position.getId())) {
                            rv.data(approve);
                            return  rv;
                        }
                    }
                }
            }
        }
        throw new ZtgeoBizException(MsgManager.USER_FLOW_QXNULL);
    }


    @Override
    @Transactional
    public List<Task> approvalProcess(String processId, String taskId, String username, String approveResult, HttpServletRequest request) throws ParseException {
        Map<String, String> mapParmeter = new HashMap<>();
        String receiptData = request.getParameter("receiptData");
        String acceptanceId = request.getParameter("acceptanceId");
        SJ_Sjsq sjsq = SysPubDataDealUtil.parseReceiptData(receiptData, null, null, acceptanceId);//处理收件数据
        startProcDataDealComponent.dealSaveDataOnProcessStart(sjsq, taskId);
        //设置查询条件
        List<Task> taskCompletes = approveComponent.SubmittedFlow(taskId, processId, approveResult, username, mapParmeter);
        return taskCompletes;
    }

    @Override
    public Object checkAndDealThisTasks(List<Task> taskCompletes) {
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        Map<String, Map<String, Object>> rvv = new HashMap<String, Map<String, Object>>();
        if (taskCompletes != null && taskCompletes.size() > 0) {
            Map<String, Object> zdMap = new HashMap<>();
            for (Task taskCompleteObj : taskCompletes) {
                if(org.apache.commons.lang3.StringUtils.isBlank(taskCompleteObj.getAssignee())) {//未签收的任务
                    try {
                        zdMap = approveComponent.getActivityAutoInterface(taskCompleteObj.getId(), new HashMap<String, String>());
                        rvv.put(taskCompleteObj.getId(), zdMap);
                        if (zdMap == null) {
                            log.info("主键为：" + taskCompleteObj.getId() + "的任务暂无可执行的自动接口");
                        }
                    } catch (ZtgeoBizException e) {
                        log.error(ErrorDealUtil.getErrorInfo(e));
                        doExcAdd(taskCompleteObj.getId(), e.getMessage());//写入异常表
                    } catch (Exception e) {
                        log.error(ErrorDealUtil.getErrorInfo(e));
                        doExcAdd(taskCompleteObj.getId(), e.getMessage());//写入异常表
                    }
                }
            }
        }
        rv.setData(rvv);
        return rv;
    }

    private void doExcAdd(String taskId, String msg) {
        SJ_Exception_Record exc = otherComponent.getNewExceptionRecord(taskId);
        if (exc == null) {
            throw new ZtgeoBizException("等待执行自动接口的步骤任务的taskId为空");
        }
        exceptionRecordBiz.insertSelective(
                StringUtils.isEmpty(exc.getExcMsg()) ? exc.excMsgg(msg) : exc
        );
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object findAgencyNumber(String username) {
        Approve approve = new Approve();
        List<Approve> approveList = approveMapper.selectByUserDb(UserUtil.checkAndGetUser(), approve);
        return approveList.size();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object findParticipationIncompleteNumber(String username) {
        List<Approve> list = new ArrayList<>();
        //获得自己已完成的节点
        List<Approve> notFinished = this.approveMapper.findCompletedByNotFinished(username);
        if (notFinished == null || notFinished.size() == 0) {
            return "0";
        }
        //遍历根据实例Id查询流程是否已结束
        for (Approve not : notFinished) {
            String proceessId = not.getApproveProcessinstanceid();
            Boolean isEndFlag = workFlowOperateService.processIsEnd(proceessId);
            if (isEndFlag == false) {
                list.add(not);
            } else {
                continue;
            }
        }
        return list.size();
    }


    /**
     * 根据实例task获取服务接口数据
     *
     * @param taskId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object findServiceByTakeId(String taskId) {
//        //获得正在执行task
//        List<Task> taskList=taskService.createTaskQuery().processInstanceId(processId).list();
//        //获取流程id信息
//        for (int i = 0; i<taskList.size();i++ ){
//            Task task = taskList.get(i);
//            if(task.isSuspended()){
//                continue;
//            }
//            if (task.getAssignee().equals(UserUtil.checkAndGetUser())){
        String stepId = (String) stepComponent.getSteps(taskId);
        if (StringUtils.isEmpty(stepId)) {
            throw new ZtgeoBizException(MsgManager.TASK_STEP_NULL);
        }
        return stepManagerComponent.getStepSettings(stepId);
    }
//        //根据实例获得我已审批
//        List<Approve> approveList=approveMapper.getApproveByProcessId(UserUtil.checkAndGetUser(),processId);
//        if (approveList==null || approveList.size()==0){
//            throw  new ZtgeoBizException(MsgManager.USER_FLOW_NULL);
//        }
//        //得到审批最后一个节点
//        Approve approve=approveList.get(0);
//        //获得taskId
//        String stepId= (String) stepComponent.getSteps(approve.getApproveTaskId());
//        if (StringUtils.isEmpty(stepId)){
//            throw  new ZtgeoBizException(MsgManager.TASK_STEP_NULL);
//        }
//        return stepManagerComponent.getStepSettings(stepId);


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object startFlow(HttpServletRequest request, @RequestBody List<SjHandleFlow> sjHandleFlows) throws IOException {
        String mid = request.getParameter("mid");
        String receiptData = request.getParameter("receiptData");
        String acceptanceId = receiptNumberComponent.getNextReceiptNumber();
        //启动流程实例
        Map<String, Object> map = startProcess(mid, receiptData, acceptanceId);
        Approve approve = (Approve) map.get("approve");
        SJ_Sjsq sjsq = (SJ_Sjsq) map.get("sjsq");
        //查模板可用条目，加载完插入fjinst
        sjFjtmComponent.uploadCharacterComparison(mid, approve.getApproveProcessinstanceid(), sjHandleFlows);
        startProcDataDealComponent.dealSaveDataOnProcessStart(sjsq, approve.getApproveTaskId());
        return approve;
    }

    /***
     * 挂失接口
     * @param rows
     * @param page
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object findHangUpFlow(String rows, String page, HttpServletRequest request) {
        if (StringUtils.isEmpty(page)) {
            page = "1";
        }
        if (StringUtils.isEmpty(rows)) {
            rows = "10";
        }
        String approveType = request.getParameter("approveType");
        PageHelper.startPage(Integer.parseInt(page),
                Integer.parseInt(rows));//分页条件
        //权限判断
        boolean flag = approveComponent.findUserByAdministrators(UserUtil.getUerId());
        if (flag) {

            List<Approve> approveList = approveMapper.findHangUpFlow(null, approveType);
            PageInfo<Approve> pageInfo = new PageInfo<Approve>(approveList);
            return new DataGridResult(pageInfo.getTotal(), pageInfo.getList());
        }
        List<Approve> approveList = approveMapper.findHangUpFlow(UserUtil.checkAndGetUser(), approveType);
        PageInfo<Approve> pageInfo = new PageInfo<Approve>(approveList);
        return new DataGridResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object findInstanceQueryandReadOnly(String processId) throws IOException {
        String mid = activitiComponent.getActivityByProcessId(processId);//获取模板id

        ObjectRestResponse rv = new ObjectRestResponse();
        List<HistoryPojo> DbHistoryPojo = new ArrayList<>();//代办
        List<HistoryPojo> ReadHistoryList = new ArrayList<>();//只读
        List<HistoryPojo> HandledHistoryList = new ArrayList<>();//已办理
        Map<String, Object> map = Maps.newHashMap();
        Map<String, Object> stepMap = Maps.newHashMap();
        stepMap = (Map<String, Object>) activityService.getFlowchartPresentation(mid, processId);
        //历史节点倒序排列
        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(processId).
                orderByHistoricTaskInstanceEndTime().desc().list();
        //先判断是否管理员登录,管理员展示已结束节点
        if (approveComponent.findUserByAdministrators(UserUtil.getUerId())) {
            map.put("glyHistoryPojo", workFlowOperateService.getComments(processId));//管理员list
        } else {
            for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
                if (historicTaskInstance.getEndTime() == null) {
                    Task task = taskService.createTaskQuery().taskId(historicTaskInstance.getId()).singleResult();
                    if (null != task.getAssignee() && task.getAssignee().equals(UserUtil.checkAndGetUser())) {
                        HistoryPojo historyPojo = new HistoryPojo();
                        historyPojo.setApprovePerson(historicTaskInstance.getAssignee());
                        historyPojo.setApproveNode(historicTaskInstance.getName());
                        historyPojo.setTaskId(historicTaskInstance.getId());
                        historyPojo.setApproveDefinitionKey(historicTaskInstance.getTaskDefinitionKey());
                        historyPojo.setProcessInstanceId(processId);
                        DbHistoryPojo.add(historyPojo);
                    }
                } else {
                    map = (Map<String, Object>) approveComponent.findLaststep(historicTaskInstance, map);
                    if (null != map.get("ReadHistoryPojo")) {
                        ReadHistoryList.add((HistoryPojo) map.get("ReadHistoryPojo"));
                    }
                    if (null != map.get("HandledHistoryPojo")) {
                        HandledHistoryList.add((HistoryPojo) map.get("HandledHistoryPojo"));
                    }
                }
            }
        }
        map.put("DbHistoryPojo", DbHistoryPojo);
        map.put("ReadHistoryPojo", ReadHistoryList);
        map.put("HandledHistoryPojo", HandledHistoryList);
        map.put("act_steps", stepMap.get("act_steps"));
        return rv.data(map);
    }


    @Override
    public Object findHandUpFlowNumber(String id, String username) {
        List<Approve> approveList = new ArrayList<>();
        //权限判断
        boolean flag = approveComponent.findUserByAdministrators(id);
        if (flag == true) {
            approveList = approveMapper.findHangUpFlow(null, null);
            if (approveList.size() == 0 || approveList == null) {
                return "0";
            }
            return approveList.size();
        }
        List<Approve> approves = approveMapper.findHangUpFlow(username, null);
        return approves.size();
    }

//    private List<Approve> historicTaskInstance(List<Approve> approveList){
//        List<Approve> approves=new ArrayList<>();
//        for (Approve process:
//                approveList) {
//            List<HistoricTaskInstance> historicTaskInstanceList = ProcessEngines.getDefaultProcessEngine().
//                    getHistoryService().createHistoricTaskInstanceQuery().
//                    processInstanceId(process.getApproveProcessinstanceid()).
//                    orderByTaskCreateTime().desc().list();
//            Integer variable=0;
//            for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
//                if (variable.equals(0)) {
//                    if (org.apache.commons.lang3.StringUtils.isNotEmpty(historicTaskInstance.getAssignee())) {
//                        if (historicTaskInstance.getAssignee().equals(UserUtil.checkAndGetUser())) {
//                            variable++;
//                            approves.add(process);
//                        }
//                    }
//                }
//            }
//        }
//        return approves;
//    }


    private List<Approve> historicTaskInstancee(List<Approve> approveList, String username) {
        List<Approve> approves = new ArrayList<>();
        for (Approve process :
                approveList) {
            List<HistoricTaskInstance> historicTaskInstanceList = ProcessEngines.getDefaultProcessEngine().
                    getHistoryService().createHistoricTaskInstanceQuery().
                    processInstanceId(process.getApproveProcessinstanceid()).
                    orderByTaskCreateTime().desc().list();
            Integer variable = 0;
            for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(historicTaskInstance.getAssignee())) {
                    if (historicTaskInstance.getAssignee().equals(username)) {
                        variable++;
                        approves.add(process);
                    }
                }
            }
        }
        return approves;
    }

    public Map<String, Object> runProcess(String mid, String receiptData, String acceptanceId) {
        return startProcess(mid, receiptData, acceptanceId);
    }

    /**
     * 启动流程，进行
     *
     * @param mid          模板id
     * @param acceptanceId 受理编号
     * @return
     */
    private Map<String, Object> startProcess(String mid, String receiptData, String acceptanceId) {
        SJ_Sjsq sjsq = null;
        Map<String, Object> map = new HashMap<>();
        try {
            String userId = UserUtil.getUerId();
            Model model = repositoryService.createModelQuery().modelId(mid).singleResult();
            ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery().deploymentId(model.getDeploymentId());
            if (query == null) {
                throw new ZtgeoBizException(MsgManager.DEPLOMENT_MODULE_NULL);
            }
            ProcessDefinition definition = query.singleResult();
            List<Depart> departList = departUserMapper.getDepartByUserId(userId);
            String departName = "";
            if (null == departList || departList.size() == 0) {
                if (approveComponent.findUserByAdministrators(UserUtil.getUerId()) == true) {
                    departName = "管理员特殊部门,";
                }
            } else {
                for (Depart depart : departList) {
                    departName += depart.getName() + ",";
                }
            }
            String departname = departName.substring(0, departName.length() - 1);
            sjsq = SysPubDataDealUtil.parseReceiptData(receiptData, null, null, acceptanceId);//处理收件数据
            System.out.println(JSONObject.toJSONString(sjsq));
            //查询名称和大类
            TemplateCategory templateCategory = templateCategoryMapper.findTemplateByModel(mid);
            if (templateCategory == null) {
                throw new ZtgeoBizException(MsgManager.TEMPLATE_CATEGORY_MODLE_NULL);
            }
            //设置大类和业务类型
            sjsq.setBusinessType(templateCategory.getTemplateLargeName());
            sjsq.setRegistrationCategory(templateCategory.getTemplateLarge());

            Map<String, Object> conditionWorkFlow = Maps.newHashMap();
            String uname = UserUtil.checkAndGetUser();
            //找父级节点为开始的步骤
            SJ_Act_Step sjActStep = stepManagerMapper.findStepByModuleId(mid);
            if (sjActStep == null) {
                throw new ZtgeoBizException(MsgManager.MODULE_START_PARENT_NULL);
            }
            conditionWorkFlow.put(sjActStep.getStepMouldId(), uname);
            System.out.println("key1:" + definition.getKey());
            System.out.println("key2:" + JSONObject.toJSONString(conditionWorkFlow));
//            conditionWorkFlow.put("dIntegratedReceiving",uname); //测试数据
            //开启工作流
//            definition.getKey()
            ProcessInstance processInstance = workFlowOperateService
                    .startWorkFLowByProcessDefinitionKey(definition.getKey(), conditionWorkFlow);
            //获取实列开始节点信息
            HistoricActivityInstance historicActivityInstance = historyService.createHistoricActivityInstanceQuery().
                    activityId("start").processInstanceId(processInstance.getProcessInstanceId()).singleResult();
            //新增开始节点数据
            Approve startApprove = new Approve();
            startApprove.setId(IDUtil.getProce());
            startApprove.setApproveProcessinstanceid(historicActivityInstance.getProcessInstanceId());
            startApprove.setApproveTaskStartTime(historicActivityInstance.getStartTime());
            startApprove.setApproveTaskEndTime(historicActivityInstance.getEndTime());
            startApprove.setApproveStatus(historicActivityInstance.getActivityName());
            startApprove.setApproveType(acceptanceId + sjsq.getNotifiedPersonName() + definition.getName());//流程名称
            startApprove.setApproveFkId(acceptanceId);//受理编号绑定实例
            startApprove.setApplyDepartment(departname);//申请部门
            if (approveMapper.insertSelective(startApprove) < 1) {
                throw new ZtgeoBizException(MsgManager.APPROVE_INSERT_BAD);
            }
            //流程跟受理编号绑定
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId()).list();
            Task task = tasks.get(0);
            Approve approve = new Approve();
            approve.setId(IDUtil.getProce());//id
            approve.setApproveFkId(acceptanceId);//受理编号id
            approve.setApproveProcessinstanceid(processInstance.getProcessInstanceId());//实例id
            approve.setApproveStartTime(new Date());//开始时间
            approve.setApproveTaskId(task.getId());//taskId
            approve.setApproveTaskStartTime(new Date());//节点开始时间
            approve.setApproveStatus(task.getName());//节点名称
            approve.setApproveType(acceptanceId + sjsq.getNotifiedPersonName() + definition.getName());//流程名称
            approve.setApprovePerson(uname);
            //预警时间
            //超时时间
            //申请部门
            approve.setApplyDepartment(departname);
            if (approveMapper.insertSelective(approve) < 1) {
                throw new ZtgeoBizException(MsgManager.APPROVE_INSERT_BAD);
            }

//            webSocket.SendMsagge(uname);
            map.put("approve", approve);
            map.put("sjsq", sjsq);
            return map;
        } catch (ParseException e) {
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_DATE_FORMAT_ERROR_MSG);
        } catch (JSONException eeee) {
            eeee.printStackTrace();
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_DATE_FORMAT_ERROR_MSG);
        } catch (ZtgeoBizException ee) {
            throw ee;
        } catch (Exception eee) {
            eee.printStackTrace();
            throw new ZtgeoBizException(BizOrBizExceptionConstant.DATA_JSON_FORMAT_ERROR_MSG);
        }
    }


    @Override
    public Approve getApproveInfoByTaskId(String taskId) {
        return approveMapper.selectByTaskId(taskId);
    }

    @Override
    public List<Approve> getCurrentUserActivity(User employee) {
        return null;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object getCurrentUserActivityByPageAndRows(String uname, String rows, String page, Approve approve) {
        List<String> list = getProcessInstanceIds(uname);
        //根据流程实例ID查询流程信息
        if (list == null || list.size() == 0) {
            return new DataGridResult(0L, new ArrayList<Object>());
        }
        if (StringUtils.isEmpty(page)) {
            page = "1";
        }
        if (StringUtils.isEmpty(rows)) {
            rows = "10";
        }
        PageHelper.startPage(Integer.parseInt(page),
                Integer.parseInt(rows));//分页条件
        List<Approve> approveList = approveMapper.selectByIds2(list, approve);
        if (approveList.size() == 0 || approveList == null) {
            return new DataGridResult((long) 0, new ArrayList<>());
        }
        PageInfo<Approve> pageInfo = new PageInfo<Approve>(approveList);
        return new DataGridResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public Object getAgencyAproveByUname(Approve approve, String rows, String page) {

        if (StringUtils.isEmpty(page)) {
            page = "1";
        }
        if (StringUtils.isEmpty(rows)) {
            rows = "20";
        }
        //获得已签收代办
        PageHelper.startPage(Integer.parseInt(page),
                Integer.parseInt(rows));//分页条件
        //判断管理员
        List<Approve> approveList = approveMapper.selectByUserDb(UserUtil.checkAndGetUser(), approve);
        PageInfo<Approve> pageInfo = new PageInfo<Approve>(approveList);
        return new DataGridResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Override
    public Object getParticipationIncomplete(String rows, String page, String approveType) {
        //获得自己已完成的节点
        List<String> stringList = new ArrayList<>();
        List<Approve> notFinished = this.approveMapper.findCompletedByNotFinished(UserUtil.checkAndGetUser());
        if (notFinished.size() == 0 || notFinished == null) {
            return new DataGridResult((long) 0, new ArrayList<>());
        }
        for (Approve approve : notFinished) {
            String proceessId = approve.getApproveProcessinstanceid();
            Boolean isEndFlag = workFlowOperateService.processIsEnd(proceessId);
            if (isEndFlag == false) {
                stringList.add(approve.getApproveProcessinstanceid());
            } else {
                continue;
            }
        }
        if (StringUtils.isEmpty(page)) {
            page = "1";
        }
        if (StringUtils.isEmpty(rows)) {
            rows = "20";
        }
        PageHelper.startPage(Integer.parseInt(page),
                Integer.parseInt(rows));//分页条件
        List<Approve> approveList = new ArrayList<Approve>();
        if(stringList!=null && stringList.size()>0) {
            approveList = approveMapper.getUnfinishedProcess(approveType, MsgManager.END, UserUtil.checkAndGetUser(), stringList);
        }
        PageInfo<Approve> pageInfo = new PageInfo<Approve>(approveList);
        return new DataGridResult(pageInfo.getTotal(), pageInfo.getList());
    }

    private List<Approve> getWcDEnd(List<Approve> notFinished) {
        List<Approve> approveList = new ArrayList<>();
        //遍历根据实例Id查询流程是否已结束
        for (Approve not : notFinished) {
            Boolean isEndFlag = workFlowOperateService.processIsEnd(not.getApproveProcessinstanceid());
            if (isEndFlag == false) {
                approveList.add(not);
            } else {
                continue;
            }
        }
        return approveList;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object findCompletedByNotFinished(String uname, String rows, String page, Approve approve) {
        ObjectRestResponse objectRestResponse = new ObjectRestResponse();
        List<Approve> list = new ArrayList<>();
        //获得已签收代办
        List<Approve> approveList = approveMapper.selectByUserDb(UserUtil.checkAndGetUser(), approve);
        //获得自己已完成的节点
        List<Approve> notFinished = this.approveMapper.findCompletedByNotFinished(uname);
        //遍历根据实例Id查询流程是否已结束
        for (Approve not : notFinished) {
            String proceessId = not.getApproveProcessinstanceid();
            Boolean isEndFlag = workFlowOperateService.processIsEnd(proceessId);
            if (isEndFlag == false) {
                list.add(not);
            } else {
                continue;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("myApproval", approveList);
        map.put("nnFinished", list);
        return objectRestResponse.data(map);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object findModelNameAndIdByFlow(List<String> list, String uname) {
        List<Temporary> temporaries = new ArrayList<>();
        if (BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(UserUtil.getUerId()).getIsSuperAdmin())) {
            List<Model> modelList = repositoryService.createModelQuery().list();
            for (Model model :
                    modelList) {
                if (!StringUtils.isEmpty(model.getDeploymentId())) {
                    ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery().deploymentId(model.getDeploymentId());//
                    //查询
                    ProcessDefinition definition = query.singleResult();
                    TemplateCategory templateCategory = templateCategoryMapper.findTemplateByModel(model.getId());
                    if (null != templateCategory) {
                        Temporary temporary = new Temporary();
                        temporary.setModeId(model.getId());
                        temporary.setFlowId(definition.getKey());
                        temporary.setIdentiFication(templateCategory.getTemplateProcess());
                        temporary.setFlowname(definition.getName());
                        temporaries.add(temporary);
                    }
                }
            }
            return temporaries;
        }
        list = (List<String>) stepManagerBiz.selectStepByPosition();
        if (list != null && list.size() != 0) {
            for (String modelId : list) {
                Model model = repositoryService.createModelQuery().modelId(modelId).singleResult();
                System.err.println("modelId:"+modelId);
                if (!StringUtils.isEmpty(model.getDeploymentId())) {
                    ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery().deploymentId(model.getDeploymentId());//
                    if (query == null) {
                        throw new ZtgeoBizException(MsgManager.DEPLOMENT_MODULE_NULL);
                    }
                    TemplateCategory templateCategory = templateCategoryMapper.findTemplateByModel(model.getId());
                    if (null == templateCategory) {
                        throw new ZtgeoBizException(MsgManager.TEMPLATE_CATEGORY_MODLE_NULL);
                    }
                    //查询
                    ProcessDefinition definition = query.singleResult();
                    Temporary temporary = new Temporary();
                    temporary.setModeId(model.getId());
                    temporary.setIdentiFication(templateCategory.getTemplateProcess());
                    temporary.setFlowId(definition.getKey());
                    temporary.setFlowname(definition.getName());
                    temporaries.add(temporary);
                }
            }
        }
        return temporaries;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public List<Approve> findQdTimeByName(String processId) {
        return this.approveMapper.findQdTimeByName(processId);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object getCurrentUserHadActivityByPageAndRows(String username, String rows, String page, Approve approve) {
        if (StringUtils.isEmpty(page)) {
            page = "1";
        }
        if (StringUtils.isEmpty(rows)) {
            rows = "20";
        }
        PageHelper.startPage(Integer.parseInt(page),
                Integer.parseInt(rows));//分页条件
        List<Approve> list = this.approveMapper.findCompletedByOwn(username, approve);
        PageInfo<Approve> pageInfo = new PageInfo<Approve>(list);
        return new DataGridResult(pageInfo.getTotal(), pageInfo.getList());

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object getApprovedNodeAndActiveNode(String proceessId) {
        return workFlowOperateService.getApprovedNodeAndActiveNode(proceessId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object selectByTaskId(String taskId) {
        return this.approveMapper.selectByTaskId(taskId);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public Object getProcessInstanceIdByWaitingReceipt(String uname, String rows, String page, Approve approve) {
        List<String> list = getWaitingReceiptList(uname);
        if (list.size() == 0 || list == null) {
            return new DataGridResult((long) 0, new ArrayList<>());
        }
        if (StringUtils.isEmpty(page)) {
            page = "1";
        }
        if (StringUtils.isEmpty(rows)) {
            rows = "10";
        }
        PageHelper.startPage(Integer.parseInt(page),
                Integer.parseInt(rows));//分页条件
        List<Approve> approveList = approveMapper.selectByIds2(list, approve);
        PageInfo<Approve> pageInfo = new PageInfo<Approve>(approveList);
        return new DataGridResult(pageInfo.getTotal(), pageInfo.getList());

    }

    @Override
    public NodeInfoPojo getNextTaskInfo(String processInstanceId) {
        return workFlowOperateService.getNextTaskInfo(processInstanceId);
    }


    /**
     * 获取处于当前用户工作流实例Id集合
     *
     * @param uname
     * @return
     */
    private List<String> getWaitingReceiptList(String uname) {
        TaskService taskService = processEngine.getTaskService();
        //查询待签收集合
        List<Task> taskList = taskService.createTaskQuery().taskCandidateUser(uname).list();
        //获取流程id信息
        List<String> list = new ArrayList<String>();
        //循环存取taskId
        for (Task task : taskList) {
            if (task.isSuspended()) {
                continue;
            }
            list.add(task.getId());
        }
        return list;
    }


    /**
     * 首页获取已签收task集合
     *
     * @param uname
     * @return
     */
    private List<String> getTaskIds(String uname) {
        TaskService taskService = processEngine.getTaskService();
        //1.用户编码查询
        List<Task> userTasks = taskService.createTaskQuery()
                .taskAssigneeLike("%" + uname + "%")
                .list();
        //获取流程id信息
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < userTasks.size(); i++) {
            Task task = userTasks.get(i);
            if (task.isSuspended()) {
                continue;
            }
            list.add(task.getId());
        }
        return list;
    }


    /**
     * 查询待签收集合
     *
     * @param uname
     * @return
     */
    private List<String> getProcessInstanceIds(String uname) {
        TaskService taskService = processEngine.getTaskService();
        //1.用户编码查询
        List<Task> userTasks = taskService.createTaskQuery()
                .taskAssigneeLike("%" + uname + "%")
                .list();
        List<Task> taskList = taskService.createTaskQuery().taskCandidateUser(uname).list();
        //获取流程id信息
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < userTasks.size(); i++) {
            Task task = userTasks.get(i);
            if (task.isSuspended()) {
                continue;
            }
            list.add(task.getId());
        }
        for (Task task : taskList) {
            if (task.isSuspended()) {
                continue;
            }
            list.add(task.getId());
        }
        return list;
    }


}
