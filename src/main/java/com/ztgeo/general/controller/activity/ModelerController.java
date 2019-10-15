package com.ztgeo.general.controller.activity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.sun.jmx.snmp.internal.SnmpIncomingRequest;
import com.ztgeo.general.biz.chenbin.impl.StepManagerBizImpl;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Act_Step;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Act_Step_Gl;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.service.activity.ActivityService;
import com.ztgeo.general.util.CommonUtils;
import com.ztgeo.general.util.ReturnMsgUtil;
import com.ztgeo.general.util.ToWeb;
import io.swagger.annotations.ApiOperation;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.pvm.runtime.OutgoingExecution;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.explorer.util.XmlUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.util.*;

/**
 * activity增删改查
 */
@RestController
@RequestMapping("api/models")
public class ModelerController {

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private StepManagerBizImpl stepManagerBiz;


    @Autowired
    private RepositoryService repositoryService;



    @RequestMapping("/image")
    public  void viewImage(String deploymentId) throws Exception {
        Model modelData = repositoryService.getModel(deploymentId);
        //获取图片资源名称
        List<String> list = processEngine.getRepositoryService()
                .getDeploymentResourceNames(modelData.getDeploymentId());
        //定义图片资源的名称
        String resourceName = "";
        if(list!=null && list.size()>0){
            for(String name:list){
                if(name.indexOf(".png")>=0){
                    resourceName = name;
                }
            }
        }
        //获取图片的输入流
        InputStream in = processEngine.getRepositoryService()//
                .getResourceAsStream(deploymentId, resourceName);

        //将图片生成到D盘的目录下
        File file = new File("D:/"+resourceName);
        //将输入流的图片写到D盘下
        FileUtils.copyInputStreamToFile(in, file);
    }

    /**
     * 新建一个空模型
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping("/create")
    public Object newModel()
            throws UnsupportedEncodingException {
         RepositoryService repositoryService = processEngine.getRepositoryService();
         //初始化一个空模型
        Model model = repositoryService.newModel();
        //设置一些默认信息
        String name = "new-process";
        String description = "";
        int revision = 1;
        String key = "process";
        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, revision);
        model.setName(name);
        model.setKey(key);
        model.setMetaInfo(modelNode.toString());
        repositoryService.saveModel(model);
        String id = model.getId();
        //完善ModelEditorSource
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        repositoryService.addModelEditorSource(id,editorNode.toString().getBytes("utf-8"));
        return  ToWeb.buildResult().redirectUrl("/modeler.html?modelId="+id);
     }

     /**
      * 获取所有模型
      * @return
      */
     @GetMapping
     public Object modelList(){
         RepositoryService repositoryService = processEngine.getRepositoryService();
         List<Model> models = repositoryService.createModelQuery().list();
         return ReturnMsgUtil.setAndReturnDataAPP("models", models);
     }

    public Object getList(@RequestParam(value = "rowSize", defaultValue = "1000", required = false) Integer rowSize,
                          @RequestParam(value = "page", defaultValue = "1", required = false) Integer page) {
        List<Model> list = repositoryService.createModelQuery().listPage(rowSize * (page - 1)
                , rowSize);
        long count = repositoryService.createModelQuery().count();
        return ToWeb.buildResult().setRows(
                ToWeb.Rows.buildRows().setCurrent(page)
                        .setTotalPages((int) (count/rowSize+1))
                        .setTotalRows(count)
                        .setList(list)
                        .setRowSize(rowSize)
        );
    }

    /**
     * 流程图
     * @param id
     * @return
     */
    @PostMapping("flowShow")
    @ApiOperation("展示流程图,已完成节点，正在执行节点")
    public Object flowShow(String id,String processId) throws IOException{
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        return rv.data(activityService.getFlowchartPresentation(id,processId));
    }








     /**
      * 删除模型
      * @param id
      * @return
      */
     @DeleteMapping("{id}")
     public Object deleteModel(@PathVariable("id")String id){
         RepositoryService repositoryService = processEngine.getRepositoryService();
         repositoryService.deleteModel(id);
         return ToWeb.buildResult().refresh();
     }

    private List<SJ_Act_Step> getAllNodes(Collection<FlowElement> flowElements,Model modelData) {
        List<SJ_Act_Step> old = new ArrayList<>();
        SJ_Act_Step start = new SJ_Act_Step();
        start.setStepMouldId("start");
        start.setStepMouldName("开始");
        start.setStepType("开始");
        start.setProcessMouldId(modelData.getId());
        old.add(start);
        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof UserTask) {
                //获得上下级关系 userTask
                List<SequenceFlow> usertaskfj = ((UserTask) flowElement).getIncomingFlows();
                for (SequenceFlow s :
                        usertaskfj) {
                        UserTask userTask = (UserTask) flowElement;
                        SJ_Act_Step dq = new SJ_Act_Step();
                        dq.setStepMouldId(userTask.getId());
                        dq.setStepMouldName(userTask.getName());
                        dq.setStepType("UserTask");
                        dq.setProcessMouldId(modelData.getId());
                        old.add(dq);
                }
            }
            if (flowElement instanceof ParallelGateway) {
                ParallelGateway parallelGateway = (ParallelGateway) flowElement;
                SJ_Act_Step sj_act_step = new SJ_Act_Step();
                sj_act_step.setProcessMouldId(modelData.getId());//模板Id
                sj_act_step.setStepMouldName(parallelGateway.getName());//子级模板名称
                sj_act_step.setStepMouldId(parallelGateway.getId());//子级模板Id
                sj_act_step.setStepType("ParallelGateway");
                List<SequenceFlow> parrwayzj = ((ParallelGateway) flowElement).getOutgoingFlows();
                old.add(sj_act_step);
            }
        }
            return old;
    }



    /**
      * 发布模型为流程定义
      * @param id
      * @return
      * @throws Exception
      */
     @RequestMapping(value = "{id}/deployment",method = RequestMethod.POST)
     @ResponseBody
     @ApiOperation("发布模型为流程定义")
     public Object deploy(@PathVariable("id")String id) throws Exception {
         try {
             ObjectRestResponse<Object> r = new ObjectRestResponse<Object>();
             List<SJ_Act_Step_Gl> stepListlist=new ArrayList<>();
             Map<String,Object> map=new HashMap<>();
             SJ_Act_Step step=new SJ_Act_Step();
             //获取模型
             RepositoryService repositoryService = processEngine.getRepositoryService();
             Model modelData = repositoryService.getModel(id);
             if (modelData == null){
                 return ReturnMsgUtil.setAndReturn(false,MsgManager.MODEL_MB);
             }
             byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());
             if (bytes == null) {
                 return ReturnMsgUtil.setAndReturn(false,MsgManager.WORKFLOW_MODEL_IS_NULL);
             }
             JsonNode modelNode = new ObjectMapper().readTree(bytes);
             BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
             if(model.getProcesses().size()==0){
                 return ReturnMsgUtil.setAndReturn(false, MsgManager.WORKFLOW_MODEL_MAIN_ERROR);
             }
             byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);
             //发布流程
             String processName = modelData.getName() + ".bpmn20.xml";
             Deployment deployment = repositoryService.createDeployment()
                     .name(modelData.getName())
                     .addString(processName, new String(bpmnBytes, "UTF-8"))
                     .deploy();
             modelData.setDeploymentId(deployment.getId());
             repositoryService.saveModel(modelData);
             Process process=model.getProcesses().get(0);
             //获取
             Collection<FlowElement> flowElements=process.getFlowElements();
             for (FlowElement flowElement:flowElements){
                 if (flowElement instanceof UserTask) {
                     List<SJ_Act_Step> children = new ArrayList<>();//子级
                     List<SJ_Act_Step> parent = new ArrayList<>();//父级
                     UserTask userTask = (UserTask) flowElement;
                     List<SequenceFlow> usertaskzj = ((UserTask) flowElement).getOutgoingFlows();
                     List<SequenceFlow> usertaskfj = ((UserTask) flowElement).getIncomingFlows();
                         for (SequenceFlow sequenceFlow : usertaskzj) {
                             SJ_Act_Step zj = new SJ_Act_Step();
                             zj.setStepMouldId(sequenceFlow.getTargetRef());
                             children.add(zj);
                         }
                         for (SequenceFlow sequenceFlow:usertaskfj) {
                         SJ_Act_Step zj=new SJ_Act_Step();
                         zj.setProcessMouldId(sequenceFlow.getSourceRef());//模板Id
                         parent.add(zj);
                     }
                         //说明有多个子级
                         if (children.size()>1){
                             for (SJ_Act_Step sj:
                                  children) {
                                 SJ_Act_Step_Gl sj_act_step_gl=new SJ_Act_Step_Gl();
                                 SJ_Act_Step fj=parent.get(0);
                                 sj_act_step_gl.setChildStepId(userTask.getId());
                                 sj_act_step_gl.setParentStepId(fj.getProcessMouldId());
                                 sj_act_step_gl.setProcessMouldId(modelData.getId());
                                 stepListlist.add(sj_act_step_gl);
                             }
                         }else if (parent.size()>1) {
                             for (SJ_Act_Step sj :
                                     parent) {
                                 SJ_Act_Step_Gl sj_act_step_gl=new SJ_Act_Step_Gl();
                                 SJ_Act_Step fj=parent.get(0);
                                 sj_act_step_gl.setChildStepId(userTask.getId());
                                 sj_act_step_gl.setParentStepId(fj.getProcessMouldId());
                                 sj_act_step_gl.setProcessMouldId(modelData.getId());
                                 stepListlist.add(sj_act_step_gl);
                             }
                         }else  if (parent.size()==1 && children.size()==1){
                             SJ_Act_Step_Gl sj_act_step_gl=new SJ_Act_Step_Gl();
                             SJ_Act_Step fj=parent.get(0);
                             sj_act_step_gl.setChildStepId(userTask.getId());
                             sj_act_step_gl.setParentStepId(fj.getProcessMouldId());
                             sj_act_step_gl.setProcessMouldId(modelData.getId());
                             stepListlist.add(sj_act_step_gl);
                         }
                 }
                 if (flowElement instanceof ParallelGateway){
                     List<SJ_Act_Step> children=new ArrayList<>();//子级
                     List<SJ_Act_Step> parent=new ArrayList<>();//父级
                     ParallelGateway parallelGateway=(ParallelGateway)flowElement;
                     List<SequenceFlow> parrwayzj=((ParallelGateway) flowElement).getOutgoingFlows();
                     List<SequenceFlow> parrwayfj=((ParallelGateway) flowElement).getIncomingFlows();
                     for (SequenceFlow sequenceFlow : parrwayzj) {
                         SJ_Act_Step zj = new SJ_Act_Step();
                         zj.setStepMouldId(sequenceFlow.getTargetRef());
                         children.add(zj);
                     }
                     for (SequenceFlow sequenceFlow:parrwayfj) {
                         SJ_Act_Step zj=new SJ_Act_Step();
                         zj.setProcessMouldId(sequenceFlow.getSourceRef());//模板Id
                         parent.add(zj);
                     }
                     //说明有多个子级
                     if (children.size()>1){
                         for (SJ_Act_Step sj:
                                 children) {
                             SJ_Act_Step_Gl sj_act_step_gl=new SJ_Act_Step_Gl();
                             SJ_Act_Step fj=parent.get(0);
                             sj_act_step_gl.setChildStepId(parallelGateway.getId());
                             sj_act_step_gl.setParentStepId(fj.getProcessMouldId());
                             sj_act_step_gl.setProcessMouldId(modelData.getId());
                             stepListlist.add(sj_act_step_gl);
                         }
                     }else if (parent.size()>1) {
                         for (SJ_Act_Step sj :
                                 parent) {
                             SJ_Act_Step_Gl sj_act_step_gl=new SJ_Act_Step_Gl();
                             sj_act_step_gl.setChildStepId(parallelGateway.getId());
                             sj_act_step_gl.setParentStepId(sj.getProcessMouldId());
                             sj_act_step_gl.setProcessMouldId(modelData.getId());
                             stepListlist.add(sj_act_step_gl);
                         }
                     }else  if (parent.size()==1 && children.size()==1){
                         SJ_Act_Step_Gl sj_act_step_gl=new SJ_Act_Step_Gl();
                         SJ_Act_Step fj=parent.get(0);
                         sj_act_step_gl.setChildStepId(parallelGateway.getId());
                         sj_act_step_gl.setParentStepId(fj.getProcessMouldId());
                         sj_act_step_gl.setProcessMouldId(modelData.getId());
                         stepListlist.add(sj_act_step_gl);
                     }
                 }
             }
             for  ( int  i  =   0 ; i  <  stepListlist.size()  -   1 ; i ++ )  {
                 for  ( int  j  =  stepListlist.size()  -   1 ; j  >  i; j -- )  {
                     if  (stepListlist.get(j).getChildStepId().equals(stepListlist.get(i).getChildStepId()))  {
                         stepListlist.remove(j);
                     }
                 }
             }
             stepManagerBiz.addAndMergeSteps(getAllNodes(flowElements,modelData),stepListlist);
             r.data(map);
             r.setMessage("操作成功");
             return r;
         } catch (IOException e) {
             e.printStackTrace();
             logger.error(e.getMessage());
             return ReturnMsgUtil.setAndReturn(false,e.getMessage());
         }

     }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public void deployUploadedFile( @RequestParam("uploadfile") MultipartFile uploadfile) {
         InputStreamReader in = null;
         try {
             try {
                 boolean validFile = false;
                 String fileName = uploadfile.getOriginalFilename();
                 if (fileName.endsWith(".bpmn20.xml") || fileName.endsWith(".bpmn")) {
                     validFile = true;
                     XMLInputFactory xif = XmlUtil.createSafeXmlInputFactory();
                     in = new InputStreamReader(new ByteArrayInputStream(uploadfile.getBytes()), "UTF-8");
                     XMLStreamReader xtr = xif.createXMLStreamReader(in);
                     BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
                     if (bpmnModel.getMainProcess() == null || bpmnModel.getMainProcess().getId() == null) {
                         // notificationManager.showErrorNotification(Messages.MODEL_IMPORT_FAILED,
                         // i18nManager.getMessage(Messages.MODEL_IMPORT_INVALID_BPMN_EXPLANATION));
                         System.out.println("err1");
                     } else {
                         if (bpmnModel.getLocationMap().isEmpty()) {
                             // notificationManager.showErrorNotification(Messages.MODEL_IMPORT_INVALID_BPMNDI,
                             // i18nManager.getMessage(Messages.MODEL_IMPORT_INVALID_BPMNDI_EXPLANATION));
                             System.out.println("err2");
                         } else {
                             String processName = null;
                             if (StringUtils.isNotEmpty(bpmnModel.getMainProcess().getName())) {
                                 processName = bpmnModel.getMainProcess().getName();
                             } else {
                                 processName = bpmnModel.getMainProcess().getId();
                             }
                             Model modelData;
                             modelData = repositoryService.newModel();
                             ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
                             modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processName);
                             modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
                             modelData.setMetaInfo(modelObjectNode.toString());
                             modelData.setName(processName);
                             repositoryService.saveModel(modelData);
                             BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
                             ObjectNode editorNode = jsonConverter.convertToJson(bpmnModel);
                             repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
                         }
                     }
                 } else {
                     // notificationManager.showErrorNotification(Messages.MODEL_IMPORT_INVALID_FILE,
                     // i18nManager.getMessage(Messages.MODEL_IMPORT_INVALID_FILE_EXPLANATION));
                     System.out.println("err3");
                 }
             } catch (Exception e) {
                 String errorMsg = e.getMessage().replace(System.getProperty("line.separator"), "<br/>");
                 // notificationManager.showErrorNotification(Messages.MODEL_IMPORT_FAILED, errorMsg);
                 System.out.println("err4");
             }
         } finally {
             if (in != null) {
                 try {
                     in.close();
                 } catch (IOException e) {
                     // notificationManager.showErrorNotification("Server-side error", e.getMessage()); S
                     System.out.println("err5");
                 }
             }
         }
     }


}
