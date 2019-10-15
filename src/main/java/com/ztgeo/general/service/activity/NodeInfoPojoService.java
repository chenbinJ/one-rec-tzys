package com.ztgeo.general.service.activity;

import com.alibaba.fastjson.JSONObject;
import com.ztgeo.general.entity.activity.NodeInfoPojo;

import java.util.List;
import java.util.Map;

public interface NodeInfoPojoService {

    /**
     * 节点递归
     * @param nodeInfoPojoJson
     * @return
     */
    NodeInfoPojo parseJsonTONodeInfo(JSONObject nodeInfoPojoJson);


    /**
     * 递归获取节点内容信息
     * @param variables
     * @param nodeInfoPojo
     * @return
     */
    void getNextNodeInfoValues(List<Map<String, Object>> variables, NodeInfoPojo nodeInfoPojo);

}
