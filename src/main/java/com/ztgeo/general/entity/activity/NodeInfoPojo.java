package com.ztgeo.general.entity.activity;

import java.io.Serializable;
import java.util.List;

public class NodeInfoPojo implements Serializable {

    /*
     * 节点类型：exclusiveGateway排他网关、userTask用户活动项
     */
    private String nodeType;

    /*
     * 节点名称
     */
    private String nodeName;

    /*
     * 需要节点办理人标识
     */
    private boolean needAssignee;

    /*
     * 节点办理人名称
     */
    private String assigneeName;

    /*
     * 节点办理人值
     */
    private String assigneeValue;

    /*
     * 审批类型：onlyOne单人审批、multi多人审批
     */
    private String approveType;

    /*
     * 流向需要变量标识
     */
    private boolean needFlowDirection;

    /*
     * 流向变量
     */
    private String flowDirectionName;

    /*
     * 流向值
     */
    private String flowDirectionValue;

    /*
     * 自身循环自身
     */
    private NodeInfoPojo nodeInfoPojo;

    /*
     * 分支节点信息
     */
    private List<NodeInfoPojo> branchNodeInfoPojoList;

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public boolean getNeedAssignee() {
        return needAssignee;
    }

    public void setNeedAssignee(boolean needAssignee) {
        this.needAssignee = needAssignee;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getAssigneeValue() {
        return assigneeValue;
    }

    public void setAssigneeValue(String assigneeValue) {
        this.assigneeValue = assigneeValue;
    }

    public String getApproveType() {
        return approveType;
    }

    public void setApproveType(String approveType) {
        this.approveType = approveType;
    }

    public boolean getNeedFlowDirection() {
        return needFlowDirection;
    }

    public void setNeedFlowDirection(boolean needFlowDirection) {
        this.needFlowDirection = needFlowDirection;
    }

    public String getFlowDirectionName() {
        return flowDirectionName;
    }

    public void setFlowDirectionName(String flowDirectionName) {
        this.flowDirectionName = flowDirectionName;
    }

    public String getFlowDirectionValue() {
        return flowDirectionValue;
    }

    public void setFlowDirectionValue(String flowDirectionValue) {
        this.flowDirectionValue = flowDirectionValue;
    }

    public NodeInfoPojo getNodeInfoPojo() {
        return nodeInfoPojo;
    }

    public void setNodeInfoPojo(NodeInfoPojo nodeInfoPojo) {
        this.nodeInfoPojo = nodeInfoPojo;
    }

    public List<NodeInfoPojo> getBranchNodeInfoPojoList() {
        return branchNodeInfoPojoList;
    }

    public void setBranchNodeInfoPojoList(List<NodeInfoPojo> branchNodeInfoPojoList) {
        this.branchNodeInfoPojoList = branchNodeInfoPojoList;
    }
}
