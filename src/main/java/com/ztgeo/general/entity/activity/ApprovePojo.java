package com.ztgeo.general.entity.activity;

import java.io.Serializable;

/**
 * 审批提交映射表
 */
public class ApprovePojo implements Serializable {

    /**
     * 审批ID
     */
    private String approveId;

    /**
     * 审批结果：agree同意、disagree不同意、turnToDo转办、discard废弃
     */
    private String approveResult;

    /**
     * 审批意见
     */
    private String comment;

    /**
     * 审批表信息修改标识: update修改、none无操作
     */
    private String approveObjFlag;

    private String approveCreateDapartment;

    /**
     * 需要节点信息
     */
    private boolean nextNode;

    public boolean isNextNode() {
        return nextNode;
    }

    public String getApproveCreateDapartment() {
        return approveCreateDapartment;
    }

    public void setApproveCreateDapartment(String approveCreateDapartment) {
        this.approveCreateDapartment = approveCreateDapartment;
    }

    public String getApproveId() {
        return approveId;
    }

    public void setApproveId(String approveId) {
        this.approveId = approveId;
    }

    public String getApproveResult() {
        return approveResult;
    }

    public void setApproveResult(String approveResult) {
        this.approveResult = approveResult;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getApproveObjFlag() {
        return approveObjFlag;
    }

    public void setApproveObjFlag(String approveObjFlag) {
        this.approveObjFlag = approveObjFlag;
    }

    public boolean getNextNode() {
        return nextNode;
    }

    public void setNextNode(boolean nextNode) {
        this.nextNode = nextNode;
    }
}
