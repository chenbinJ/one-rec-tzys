package com.ztgeo.general.exceptionmsg;

public class WorkFlowManager {

    /**
     * 节点
     */
    public static String USER_TASK = "userTask";//用户活动项
    public static String EXCLUSIVE_GATEWAY = "exclusiveGateway";//排他网关


    public static String PARALLEL_GATEWAY="ParallelGateway";//并行网关

    /**
     *
     */
    public static String PROCESS="process";
    public static String CESHI="ceshi";


    /**
     * 转移加登记流程key
     */
    public static String TRANSFER_MORTGAGE="transferMortgage";
    public static String TRANSFER_MORTGAGE_TYPE="转移抵押";


    /**
     * 收文流程KEY
     */
    public static String SIMPLE_RECEIVED_MANAGER = "SimpleReceivedManager";
    public static String SIMPLE_RECEIVED_MANAGER_TYPE = "收文流程";

    public static String RECEIVED_MANAGER = "receivedWorkFlow";
    public static String RECEIVED_MANAGER_TYPE = "收文流程";

    public static String SIMPLE_SEAL_MANAGER="SimpleSeal";
    public static String SIMPLE_SEAL_MANAGER_TYPE="用印申请";

    public static String LEAVE_MANAGER="Leave";
    public static String LEAVE_MANAGER_TYPE="请假";

    public static String MEETING_MANAGER="meetingapprove";
    public static String MEETING_MANAGER_TYPE="会议审批";


    /**
     * 发文流程KEY
     */
    public static String SIMPLE_POST_MANAGER = "SimplePostManager";
    public static String SIMPLE_POST_MANAGER_TYPE = "发文流程";
    public static String POST_MANAGER = "postManager";
    public static String POST_MANAGER_TYPE = "发文流程";

    /**
     * 材料印刷申请流程KEY
     */
    public static String SIMPLE_PRINT_MANAGER = "applyPrinting";//simplePrint
    public static String SIMPLE_PRINT_MANAGER_TYPE = "材料印刷申请";

}
