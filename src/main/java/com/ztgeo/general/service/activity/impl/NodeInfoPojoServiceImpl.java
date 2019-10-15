package com.ztgeo.general.service.activity.impl;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.ztgeo.general.entity.activity.NodeInfoPojo;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.exceptionmsg.WorkFlowManager;
import com.ztgeo.general.service.activity.NodeInfoPojoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("nodeInfoPojoService")
public class NodeInfoPojoServiceImpl implements NodeInfoPojoService {

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.DEFAULT
            , rollbackFor = RuntimeException.class)
    public NodeInfoPojo parseJsonTONodeInfo(JSONObject nodeInfoPojoJson) {
        if (nodeInfoPojoJson == null) {
            return null;
        }
        NodeInfoPojo nodeInfoPojo = new NodeInfoPojo();
        nodeInfoPojo.setNodeType((String) nodeInfoPojoJson.get("nodeType"));//节点类型：exclusiveGateway排他网关、userTask用户活动项
        nodeInfoPojo.setNodeName((String) nodeInfoPojoJson.get("nodeName"));//节点名称
        nodeInfoPojo.setNeedAssignee((Boolean) nodeInfoPojoJson.get("needAssignee"));//需要节点办理人标识
        nodeInfoPojo.setAssigneeName((String) nodeInfoPojoJson.get("assigneeName"));//节点办理人名称
        nodeInfoPojo.setAssigneeValue((String) nodeInfoPojoJson.get("assigneeValue"));//节点办理人值
        nodeInfoPojo.setApproveType((String) nodeInfoPojoJson.get("approveType"));//审批类型：onlyOne单人审批、multi多人审批
        nodeInfoPojo.setNeedFlowDirection((Boolean) nodeInfoPojoJson.get("needFlowDirection"));//流向需要变量标识
        nodeInfoPojo.setFlowDirectionName((String) nodeInfoPojoJson.get("flowDirectionName"));//流向变量
        nodeInfoPojo.setFlowDirectionValue((String) nodeInfoPojoJson.get("flowDirectionValue"));//流向值
        nodeInfoPojo.setNodeInfoPojo(parseJsonTONodeInfo((JSONObject) nodeInfoPojoJson.get("nodeInfoPojo")));//自身循环自身
        return nodeInfoPojo;
    }

    /**
     * 递归获取节点内容信息
     *
     * @param variables
     * @param nodeInfoPojo
     * @return
     */
    @Override
    public void getNextNodeInfoValues(List<Map<String, Object>> variables, NodeInfoPojo nodeInfoPojo) {
        if (WorkFlowManager.USER_TASK.equals(nodeInfoPojo.getNodeType())) {//为用户活动项
                    /*
                        需不需要设置节点办理人
                        1.需要：设置变量
                        2.不需要：不进行设置变量
                     */
            if (nodeInfoPojo.getNeedAssignee() && !StringUtils.isEmpty(nodeInfoPojo.getAssigneeName())) {
                if ("onlyOne".equals(nodeInfoPojo.getApproveType())) {
                    Map<String, Object> variable = new HashMap<String, Object>();
                    variable.put("name", nodeInfoPojo.getAssigneeName());
                    variable.put("value", nodeInfoPojo.getAssigneeValue());
                    variables.add(variable);
                } else if ("multi".equals(nodeInfoPojo.getApproveType())) {
                    Map<String, Object> variable = new HashMap<String, Object>();
                    variable.put("name", nodeInfoPojo.getAssigneeName());
                    variable.put("value", Arrays.asList(nodeInfoPojo.getAssigneeValue().split(",")));
                    variables.add(variable);
                }
                if (StringUtils.isEmpty(nodeInfoPojo.getAssigneeName()) || StringUtils.isEmpty(nodeInfoPojo.getAssigneeValue())) {
                    throw new RuntimeException("请填写" + nodeInfoPojo.getNodeName());
                }
            }
        } else if (WorkFlowManager.EXCLUSIVE_GATEWAY.equals(nodeInfoPojo.getNodeType())) {//为排他网关
            /*
                需不需要设置节点流向变量
                1.需要：设置变量
                2.不需要：不进行设置变量,并报错
             */
            if (nodeInfoPojo.getNeedFlowDirection()) {
                if (!StringUtils.isEmpty(nodeInfoPojo.getFlowDirectionName())) {
                    Map<String, Object> variable = new HashMap<String, Object>();
                    variable.put("name", nodeInfoPojo.getFlowDirectionName());
                    variable.put("value", nodeInfoPojo.getFlowDirectionValue());
                    variables.add(variable);
                    if (StringUtils.isEmpty(nodeInfoPojo.getFlowDirectionName()) || StringUtils.isEmpty(nodeInfoPojo.getFlowDirectionValue())) {
                        throw new RuntimeException("请填写流向信息!");
                    }
                    if (nodeInfoPojo.getNodeInfoPojo() != null) {
                        getNextNodeInfoValues(variables, nodeInfoPojo.getNodeInfoPojo());
                    }
                }
            } else {
                throw new RuntimeException(MsgManager.EXCLUSIVE_GATEWAY_IS_ONE);
            }
        }
    }

}
