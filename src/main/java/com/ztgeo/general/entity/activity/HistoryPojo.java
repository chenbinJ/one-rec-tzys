package com.ztgeo.general.entity.activity;

public class HistoryPojo {

    private String id;//ID

    private String taskId;//taskID

    private String processInstanceId;//任务id

    private String approveDate;//审批时间

    private String approvePerson;//审批人

    private String approveState;//审批结果

    private String approveNode;//审批节点

    private String approveMsg;//审批意见

    private String approveDefinitionKey;//taskId  key标识

    public String getApproveDefinitionKey() {
        return approveDefinitionKey;
    }

    public void setApproveDefinitionKey(String approveDefinitionKey) {
        this.approveDefinitionKey = approveDefinitionKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(String approveDate) {
        this.approveDate = approveDate;
    }

    public String getApprovePerson() {
        return approvePerson;
    }

    public void setApprovePerson(String approvePerson) {
        this.approvePerson = approvePerson;
    }

    public String getApproveState() {
        return approveState;
    }

    public void setApproveState(String approveState) {
        this.approveState = approveState;
    }

    public String getApproveNode() {
        return approveNode;
    }

    public void setApproveNode(String approveNode) {
        this.approveNode = approveNode;
    }

    public String getApproveMsg() {
        return approveMsg;
    }

    public void setApproveMsg(String approveMsg) {
        this.approveMsg = approveMsg;
    }
}
