package com.ztgeo.general.service.activity;

import com.ztgeo.general.entity.activity.HistoryPojo;
import com.ztgeo.general.entity.activity.NodeInfoPojo;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.List;
import java.util.Map;

public interface WorkFlowOperateService {

    /**
     * 开启工作流--根据processDefinitionKey启动
     * @Param processId 模板ID
     * @Param inputMap 条件查询Map
     * @return
     */
    ProcessInstance startWorkFLowByProcessDefinitionKey(String processId, Map<String, Object> inputMap);


    /**
     * 获得模板Id
     * @param processId
     * @return
     */
    String getActivityByProcessId(String processId);

    /**
     * 开启工作流--根据processInstanceBuilder启动
     * @Param processId 模板ID
     * @Param inputMap 条件查询Map
     * @return
     */
    String startWorkFLowProcessInstanceBuilder(String processId, Map<String, Object> inputMap);

    /**
     * 工作流单个审批--流程ID
     * @param processInstanceId 流程ID
     * @param approveResult 流程结果：agree同意、disagree不同意、turnToDo转办、discard废弃
     * @param userCode 用户编码
     * @param turnToUser 转办用户name:流程变量value:用户code
     * @param comment 审批意见 同意##审批人##时间(yyyy-MM-dd HH:mm:ss)##意见
     * @param variables 变量参数 name:变量名称 value:变量值
     * @return
     */
    Object taskSingleWorkFLowByProcessInstanceId(String processInstanceId, String approveResult,String taskId,
                                                 String userCode, String turnToUser, String comment, List<Map<String, Object>> variables);


    /**
     * 工作流单个审批--任务ID
     * @param taskId 任务ID
     * @param approveResult 流程结果：agree同意、disagree不同意、turnToDo转办、discard废弃
     * @param userCode 审批人
     * @param comment 审批意见 同意##审批人名称##时间(yyyy-MM-dd HH:mm:ss)##意见
     * @return
     */
    Object taskSingleWorkFLowByTaskId(String taskId, String approveResult, String userCode, String comment);


    /**
     * 根据流程实例ID获取任务项
     * @param ProcessInstanceId
     * @return
     */
    List<Task> getTaskByProcessInstanceId(String ProcessInstanceId);

    /**
     * 完成任务项
     * @param taskId
     * @param comment
     * @param variables
     */
    void completeTask(String taskId, String comment, List<Map<String, Object>> variables);

    /**
     *
     * @param username
     * @return
     */
    Task implementTask(String username,String processId);



    /**
     * 签收待审核任务
     * @param processId
     * @return
     */
    Object signfor(String processId);

    /**
     * 判断流程实例对象所处流程是否结束
     * @param processInstanceId 流程实例ID
     * @return
     */
    Boolean processIsEnd(String processInstanceId);

    /**
     * 获取当前流程历史审批信息
     * @param processInstanceId 流程实例ID
     * @return
     */
    List<HistoryPojo> getComments(String processInstanceId);

    /**
     * 查询已审批过的节点以及正在审批节点信息
     * @param proceessId
     * @return
     */
    Object getApprovedNodeAndActiveNode(String proceessId);




    /**
     * 获取当前流程节点下一节点实例
     * @param procInstanceId
     */
    List<Map<String,Object>> getNodes(String procInstanceId);

    /**
     * 获取下一个用户任务信息
     * @param procInstanceId     工作流Id信息
     * @return  下一个用户任务用户组信息
     */
    NodeInfoPojo getNextTaskInfo(String procInstanceId);

    /**
     * 流程激活
     * @param processInstanceId
     */
    void activationWorkFlow(String processInstanceId);

    /**
     * 流程挂起
     * @param processInstanceId
     */
    void hangWorkFlow(String processInstanceId);

}
