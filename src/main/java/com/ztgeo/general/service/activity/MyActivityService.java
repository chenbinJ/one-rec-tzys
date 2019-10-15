package com.ztgeo.general.service.activity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.entity.activity.ActivityPojo;
import org.activiti.engine.repository.Model;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface MyActivityService {

    /**
     * 流程定义列表
     */
    Object processList(ActivityPojo activityPojo);

    /**
     * 流程定义列表--分页
     */
    Object processListForPage(ActivityPojo activityPojo);

    /**
     * 流程模型列表
     */
    Object modelList(ActivityPojo activityPojo);

    /**
     * 流程模型列表--分页
     */
    Object modelListForPage(ActivityPojo activityPojo);

    /**
     * 删除流程模型
     * @param id
     * @return
     */
    void deleteModel(String id);

    /**
     * 删除部署模板
     * @param arpDeploymentId
     */
    void deleteDeployeement(String arpDeploymentId);

    /**
     * 导出model的xml文件
     * @throws IOException
     * @throws JsonProcessingException
     */
    void exportModel(String id, HttpServletResponse response);

    /**
     * 创建model模型
     */
    Model create(String name, String key, String description);

    /**
     * 删除定义流程
     * @param id
     * @return
     */
    Object deleteDefinition(String id);

    /**
     * 挂起、激活流程实例
     * @param state active激活流程 suspend挂起
     * @param procDefId
     * @return
     */
    ObjectRestResponse<Object> updateState(String state, String procDefId);

    /**
     * 将部署的流程转换为模型
     * @param procDefId
     */
    Model convertToModel(String procDefId);

}
