package com.ztgeo.general.service.activity;

import com.ztgeo.general.entity.User;
import com.ztgeo.general.entity.activity.Approve;
import com.ztgeo.general.entity.activity.NodeInfoPojo;

import com.ztgeo.general.entity.service_data.pub_data.SjHandleFlow;
import org.activiti.engine.task.Task;
import org.apache.ibatis.annotations.Param;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ApproveService {
    Object deleteByPrimaryKey(@Param("id") String id);

    Object insert(Approve record);

    Object insertSelective(Approve record);

    public Object findHandUpFlowNumber(String id, String username);//id为userId

    Object updateApproveByTaskId(Approve approve);

    Object findjudgeTaskAnduserName(String taskId);//根据taskId和用户判断节点是否已提交状态

    List<Task> approvalProcess(String process, String taskId, String username, String approveResult, HttpServletRequest request) throws ParseException;

    Object checkAndDealThisTasks(List<Task> taskCompletes);

    Object findAgencyNumber(String username);

    Object findParticipationIncompleteNumber(String username);

    Object findServiceByTakeId(String taskId);

    Object startFlow(HttpServletRequest request, List<SjHandleFlow> sjHandleFlows) throws IOException;

    Object findHangUpFlow(String rows, String page, HttpServletRequest request);//挂起业务查询

    Map<String, Object> runProcess(String mid, String receiptData, String acceptanceId);

    Object findInstanceQueryandReadOnly(String processId) throws IOException;//通过实例查询代办和已签收

    /*Object selectByPrimaryKey(@Param("id") String id);

    Object updateByPrimaryKeySelective(Approve record);

    Object updateByPrimaryKey(Approve record);*/

    /**
     * 根据taskid获取信息
     *
     * @param taskId
     * @return
     */
    Approve getApproveInfoByTaskId(String taskId);

    /**
     * app获取流程
     *
     * @param employee
     * @return
     */
    List<Approve> getCurrentUserActivity(User employee);

    /* */

    /**
     * app获取流程
     *
     * @param page
     * @return
     *//*
    List<Approve> getCurrentUserActivityByUserCode(String assignee);

    /**
     * 获取当前对象所有流程--分页
     * @param employee
     * @param rows
     * @param page
     * @param approve
     * @return
     */
    Object getCurrentUserActivityByPageAndRows(String uname,
                                               String rows,
                                               String page, Approve approve);


    /**
     * 代办节点
     *
     * @return
     */
    Object getAgencyAproveByUname(Approve approve, String rows, String page);

    /**
     * 审批过没有结束的流程
     *
     * @param rows
     * @param page
     * @return
     */
    Object getParticipationIncomplete(String rows, String page, String approveType);


    /**
     * 首页
     * 我参与过但未完成的流程
     * 待我审批节点
     *
     * @param uname
     * @return
     */
    Object findCompletedByNotFinished(String uname, String rows, String page, Approve approve);


    /**
     * 根据模板id查询流程名称Id
     *
     * @param list
     * @return
     */
    Object findModelNameAndIdByFlow(List<String> list, String uname);

    /**
     * 查看流程的启动时间和名称
     *
     * @param processId
     * @return
     */
    List<Approve> findQdTimeByName(String processId);

    /**
     * 获取当前对象审批完成的节点--分页
     *
     * @param username 当前用户
     * @param rows
     * @param page
     * @param approve
     * @return
     */
    Object getCurrentUserHadActivityByPageAndRows(String username,
                                                  String rows,
                                                  String page, Approve approve);


    /**
     * 查询已审批过的节点以及正在审批节点信息
     *
     * @param proceessId
     * @return
     */
    Object getApprovedNodeAndActiveNode(String proceessId);


    /**
     * 通过taskId查询
     *
     * @param taskId
     * @return
     */
    Object selectByTaskId(String taskId);

    /**
     * 查询待签收集合
     *
     * @param uname
     * @param rows
     * @param page
     * @param approve
     * @return
     */
    Object getProcessInstanceIdByWaitingReceipt(String uname,
                                                String rows,
                                                String page, Approve approve);

    /*//**
     * 获取当前对象审批完成的所有流程App--分页
     * @param userPhone
     * @param rows
     * @param page
     * @return
     *//*
    Object getCurrentUserHadActivityAppByPageAndRows(String userPhone,
                                                     String rows,
                                                     String page);

    *//**
     * 根据流程ID获取流程信息
     * @param taskId
     * @return
     *//*
    Approve getApproveInfoById(String taskId);

    *//**
     * 根据审批ID获取流程信息
     * @param approveId
     * @return
     *//*
    Approve getApproveInfoByIdNoOperating(String approveId);

    *//**
     * 根据流程ID获取流程历史
     * @param processInstanceId 流程ID
     * @return
     *//*
    List<HistoryPojo> getApproveHistory(String processInstanceId);

    *//**
     * 流程监控
     * @param approve
     * @return
     *//*
    Object monitorProcess(Approve approve);

    */

    /**
     * 流程监控
     *
     * @param
     * @return
     *//*
    Object monitorProcessApp(Approve approve);


     /* 根据流程ID流程下个节点流向信息
     * @param processInstanceId 流程ID
     * @return
     */
    NodeInfoPojo getNextTaskInfo(String processInstanceId);

//    //**
//     * 流程激活
//     * @param processInstanceId
//     *//*
//    void activationWorkFlow(String processInstanceId);
//
//    *//**
//     * 流程挂起
//     * @param processInstanceId
//     *//*
//    void hangWorkFlow(String processInstanceId);
//
//    DataGridResult findPrintList(Map<String, Object> map)throws Exception;
//
//    DataGridResult findPrintApplyList(Map<String, Object> map)throws Exception;
//
//    DataGridResult findReceiptRegisterList(Map<String, Object> map)throws Exception;
//
//    DataGridResult findPostRegisterList(Map<String, Object> map)throws Exception;
//
//    DataGridResult findOaLeaveList(Map<String, Object> map)throws Exception;
//
//    DataGridResult findRoomLaunchList(Map<String, Object> map)throws Exception;*/
}
