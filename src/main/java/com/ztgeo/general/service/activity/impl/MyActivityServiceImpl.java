package com.ztgeo.general.service.activity.impl;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.config.webscoket.WebSocket;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.activity.ActivityPojo;
import com.ztgeo.general.entity.activity.Approve;
import com.ztgeo.general.entity.extend.DataGridResult;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.mapper.activity.ApproveMapper;
import com.ztgeo.general.service.activity.MyActivityService;
import com.ztgeo.general.util.ReturnMsgUtil;
import com.ztgeo.general.util.chenbin.UserUtil;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.*;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MyActivityServiceImpl implements MyActivityService {

    private Logger logger = Logger.getLogger(this.getClass());


    @Autowired
    private ApproveMapper approveMapper;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private WebSocket webSocket;
    protected ObjectMapper objectMapper = new ObjectMapper();


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.DEFAULT,
            rollbackFor = RuntimeException.class)
    public Object processList(ActivityPojo activityPojo) {
        try {
            ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                    .latestVersion().orderByProcessDefinitionKey().asc();
            Long count = processDefinitionQuery.count();//查询数量
            List<ProcessDefinition> processDefinitionList = processDefinitionQuery.list();
            return ReturnMsgUtil.setAndReturnData(true, "操作成功!", processDefinitionList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.DEFAULT,
            rollbackFor = RuntimeException.class)
    public Object processListForPage(ActivityPojo activityPojo) {
        try {
            ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery()
                    .latestVersion().orderByProcessDefinitionKey().asc();
            Long count = processDefinitionQuery.count();//查询数量
            if (StringUtils.isEmpty(activityPojo.getPage())) {
                activityPojo.setPage("1");
            }
            if (StringUtils.isEmpty(activityPojo.getRows())) {
                activityPojo.setRows("20");
            }
            List<ProcessDefinition> processDefinitionList =
                    processDefinitionQuery.listPage(
                            (Integer.parseInt(activityPojo.getPage()) - 1) * Integer.parseInt(activityPojo.getRows()),
                            Integer.parseInt(activityPojo.getPage()) * Integer.parseInt(activityPojo.getRows()));
            List<ActivityPojo> activityPojoList = new ArrayList<ActivityPojo>();
            for (ProcessDefinition processDefinition : processDefinitionList) {
                String deploymentId = processDefinition.getDeploymentId();//流程部署ID
                Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
                Model model = repositoryService.createModelQuery().deploymentId(deploymentId).singleResult();//流程模板ID
                ActivityPojo activityPojoObj = new ActivityPojo();
                activityPojoObj.setArpDeploymentId(deploymentId);//部署ID
                activityPojoObj.setArpCategory(model == null ? null : model.getCategory());//类别
                activityPojoObj.setArdDeployTime(deployment == null ? null : deployment.getDeploymentTime());//部署时间
                activityPojoObj.setArpId(processDefinition == null ? null : processDefinition.getId());//流程ID
                activityPojoObj.setArpKey(processDefinition == null ? null : processDefinition.getKey());//流程标识
                activityPojoObj.setArpName(processDefinition == null ? null : processDefinition.getName());//流程名称
                activityPojoObj.setArpVersion(String.valueOf(processDefinition == null ? "" : processDefinition.getVersion()));//流程版本
                activityPojoObj.setArpResourceName(processDefinition == null ? null : processDefinition.getResourceName());//流程XML
                activityPojoObj.setArpDgrmResourceName(processDefinition == null ? null : processDefinition.getDiagramResourceName());//流程图片
                activityPojoObj.setArpSuspensionState(processDefinition == null ? null : processDefinition.isSuspended() ? "1" : "2");//挂起标识1.激活2.挂起
                activityPojoList.add(activityPojoObj);
            }
            return new DataGridResult(count, activityPojoList);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            logger.error(e.toString());
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.DEFAULT,
            rollbackFor = RuntimeException.class)
    public Object modelList(ActivityPojo activityPojo) {
        try {
            ModelQuery modelQuery = repositoryService.createModelQuery().latestVersion().orderByLastUpdateTime().desc();
            Long count = modelQuery.count();//查询数量
            List<Model> modelList = modelQuery.list();
            return ReturnMsgUtil.setAndReturnData(true, "操作成功!", modelList);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.DEFAULT,
            rollbackFor = RuntimeException.class)
    public Object modelListForPage(ActivityPojo activityPojo) {
        try {
            ModelQuery modelQuery = repositoryService.createModelQuery().latestVersion().orderByLastUpdateTime().desc();

            Long count = modelQuery.count();//查询数量
            if (StringUtils.isEmpty(activityPojo.getPage())) {
                activityPojo.setPage("1");
            }
            if (StringUtils.isEmpty(activityPojo.getRows())) {
                activityPojo.setRows("20");
            }
            List<Model> modelList =
                    modelQuery.listPage(
                            (Integer.parseInt(activityPojo.getPage()) - 1) * Integer.parseInt(activityPojo.getRows()),
                            Integer.parseInt(activityPojo.getPage()) * Integer.parseInt(activityPojo.getRows()));
            return new DataGridResult(count, modelList);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            logger.error(e.toString());
            throw new RuntimeException(e.getMessage());
        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.DEFAULT,
            rollbackFor = RuntimeException.class)
    public void deleteModel(String id) {
        try {
            Model model = repositoryService.createModelQuery().modelId(id).singleResult();
            if (StringUtils.isEmpty(model.getDeploymentId())) {
                repositoryService.deleteModel(id);
            } else {
                throw new ZtgeoBizException(BizOrBizExceptionConstant.ACTIVITI_MODEL_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.DEFAULT,
            rollbackFor = RuntimeException.class)
    public void deleteDeployeement(String arpDeploymentId) {
        try {
            repositoryService.deleteDeployment(arpDeploymentId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true, isolation = Isolation.DEFAULT,
            rollbackFor = RuntimeException.class)
    public void exportModel(String id, HttpServletResponse response) {
        try {
            Model modelData = repositoryService.getModel(id);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
            IOUtils.copy(in, response.getOutputStream());
            String filename = bpmnModel.getMainProcess().getId() + ".bpmn20.xml";
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.flushBuffer();
        } catch (Exception e) {
            throw new ActivitiException("导出model的xml文件失败，模型ID=" + id, e);
        }

    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, isolation = Isolation.DEFAULT,
            rollbackFor = RuntimeException.class)
    public Model create(String name, String key, String description) {
        try {
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode properties = objectMapper.createObjectNode();
            properties.put("process_author", "jeesite");
            editorNode.put("properties", properties);
            ObjectNode stencilset = objectMapper.createObjectNode();
            stencilset.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.put("stencilset", stencilset);

            Model modelData = repositoryService.newModel();
            description = org.apache.commons.lang3.StringUtils.defaultString(description);
            modelData.setKey(org.apache.commons.lang3.StringUtils.defaultString(key));
            modelData.setName(name);
            modelData.setVersion(Integer.parseInt(String.valueOf(repositoryService.createModelQuery().modelKey(modelData.getKey()).count() + 1)));

            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, modelData.getVersion());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
            modelData.setMetaInfo(modelObjectNode.toString());

            repositoryService.saveModel(modelData);
            repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
            return modelData;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RuntimeException(e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new RuntimeException(e.toString());
        }
    }

    /**
     * 删除流程，正在执行的不得删除
     *
     * @param id
     * @return
     */
    @Override
    public Object deleteDefinition(String id) {
        try {
            repositoryService.deleteDeployment(id);//加true代表全删
            return ReturnMsgUtil.setAndReturn(true, MsgManager.DELETE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ObjectRestResponse<Object> updateState(String taskId, String procDefId) {
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        runtimeService.activateProcessInstanceById(procDefId);
        //  runtimeService.activateProcessDefinitionById(procDefId, true, null);
        Approve approve = this.approveMapper.selectByTaskId(taskId);
        approve.setApproveState(1);
        approve.setApproveTaskEndTime(null);
        approveMapper.updateByPrimaryKeySelective(approve);
        webSocket.sendMessage(UserUtil.checkAndGetUser(), webSocket.SendResultMsagge(UserUtil.checkAndGetUser(), UserUtil.getUerId()));
        rv.setMessage(String.format(MsgManager.WORKFLOW_PROCESS_DEFINITON_FLAG, "激活", procDefId));
        return rv;
//        return ReturnMsgUtil.setAndReturn(true,String.format(MsgManager.WORKFLOW_PROCESS_DEFINITON_FLAG,"激活",procDefId));
    }

    @Override
    public Model convertToModel(String procDefId) {
        try {
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId).singleResult();
            InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
                    processDefinition.getResourceName());
            XMLInputFactory xif = XMLInputFactory.newInstance();
            InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
            XMLStreamReader xtr = xif.createXMLStreamReader(in);
            BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

            BpmnJsonConverter converter = new BpmnJsonConverter();
            ObjectNode modelNode = converter.convertToJson(bpmnModel);
            Model modelData = repositoryService.newModel();
            modelData.setKey(processDefinition.getKey());
            modelData.setName(processDefinition.getResourceName());
            modelData.setCategory(processDefinition.getCategory());//.getDeploymentId());
            modelData.setDeploymentId(processDefinition.getDeploymentId());
            modelData.setVersion(Integer.parseInt(String.valueOf(repositoryService.createModelQuery().modelKey(modelData.getKey()).count() + 1)));

            ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
            modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, modelData.getVersion());
            modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
            modelData.setMetaInfo(modelObjectNode.toString());

            repositoryService.saveModel(modelData);

            repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));

            return modelData;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.error(e.toString());
            throw new RuntimeException(e.getMessage());
        } catch (XMLStreamException e) {
            e.printStackTrace();
            logger.error(e.toString());
            throw new RuntimeException(e.getMessage());
        }
    }


}
