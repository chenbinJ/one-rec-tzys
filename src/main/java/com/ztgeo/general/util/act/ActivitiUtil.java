package com.ztgeo.general.util.act;

import com.google.common.collect.Maps;
import org.activiti.bpmn.BpmnAutoLayout;
import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
//import org.junit.Assert;  
  
/** 
 *  
 */

/**   
 * @ClassName: ActivitiTest01 
 * @Description: TODO(activiti 工作流程图自动生成) 
 * @author liang 
 * @date 2016年4月20日 上午8:32:56 
 * 
 */  
public class ActivitiUtil {

    @Autowired
    private ProcessEngine processEngine;

    /**
     * 测试
     * @param args
     */
    public static void main(String[] args) {
       ActivitiUtil activitiUtil=new ActivitiUtil();
        activitiUtil.test("admin");
    }  

    public void test(String assignee){
        TaskService taskService=processEngine.getTaskService();
        List<Task> taskList=taskService.createTaskQuery()//创建任务查询对象
                .taskAssignee(assignee)//指定个人任务查询，指定办理人
                .list();//获取该办理人下的事务列表
        if(taskList!=null&&taskList.size()>0){
            for(Task task:taskList){
                System.out.println("任务ID："+task.getId());
                System.out.println("任务名称："+task.getName());
                System.out.println("任务的创建时间："+task.getCreateTime());
                System.out.println("任务办理人："+task.getAssignee());
                System.out.println("流程实例ID："+task.getProcessInstanceId());
                System.out.println("执行对象ID："+task.getExecutionId());
                System.out.println("流程定义ID："+task.getProcessDefinitionId());
                System.out.println("#############################################");
            }
        }
    }

    public static void test01() throws IOException {  
        System.out.println(".........start...");  
        ProcessEngine processEngine=getProcessEngine();  
          
        // 1. Build up the model from scratch  
        BpmnModel model = new BpmnModel();    
        Process process=new Process();
        model.addProcess(process);
        final String PROCESSID ="process01";  
        final String PROCESSNAME ="测试01";  
        process.setId(PROCESSID);   
        process.setName(PROCESSNAME);  
          
        process.addFlowElement(createStartEvent());    
        process.addFlowElement(createUserTask("task1", "节点01", "candidateGroup1"));   
        process.addFlowElement(createExclusiveGateway("createExclusiveGateway1"));   
        process.addFlowElement(createUserTask("task2", "节点02", "candidateGroup2"));   
        process.addFlowElement(createExclusiveGateway("createExclusiveGateway2"));   
        process.addFlowElement(createUserTask("task3", "节点03", "candidateGroup3"));   
        process.addFlowElement(createExclusiveGateway("createExclusiveGateway3"));   
        process.addFlowElement(createUserTask("task4", "节点04", "candidateGroup4"));  
        process.addFlowElement(createEndEvent());    
          
        process.addFlowElement(createSequenceFlow("startEvent", "task1", "", ""));   
        process.addFlowElement(createSequenceFlow("task1", "task2", "", ""));   
        process.addFlowElement(createSequenceFlow("task2", "createExclusiveGateway1", "", ""));  
        process.addFlowElement(createSequenceFlow("createExclusiveGateway1", "task1", "不通过", "${pass=='2'}"));  
        process.addFlowElement(createSequenceFlow("createExclusiveGateway1", "task3", "通过", "${pass=='1'}"));   
        process.addFlowElement(createSequenceFlow("task3", "createExclusiveGateway2", "", ""));  
        process.addFlowElement(createSequenceFlow("createExclusiveGateway2", "task2", "不通过", "${pass=='2'}"));  
        process.addFlowElement(createSequenceFlow("createExclusiveGateway2", "task4", "通过", "${pass=='1'}"));  
        process.addFlowElement(createSequenceFlow("task4", "createExclusiveGateway3", "", ""));  
        process.addFlowElement(createSequenceFlow("createExclusiveGateway3", "task3", "不通过", "${pass=='2'}"));  
        process.addFlowElement(createSequenceFlow("createExclusiveGateway3", "endEvent", "通过", "${pass=='1'}"));  
          
        // 2. Generate graphical information    
        new BpmnAutoLayout(model).execute();  
          
        // 3. Deploy the process to the engine    
        Deployment deployment = processEngine.getRepositoryService().createDeployment().addBpmnModel(PROCESSID+".bpmn", model).name(PROCESSID+"_deployment").deploy();    
             
        // 4. Start a process instance    
        ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(PROCESSID);   
          
        // 5. Check if task is available    
        List<Task> tasks = processEngine.getTaskService().createTaskQuery().processInstanceId(processInstance.getId()).list();  
       // Assert.assertEquals(1, tasks.size());         
          
        // 6. Save process diagram to a file      
        InputStream processDiagram = processEngine.getRepositoryService().getProcessDiagram(processInstance.getProcessDefinitionId());    
        File pngFile = new File("PROCESSID.png");
        System.out.println("-----name:"+pngFile.getName());
        /*if(!pngFile.exists()){
        	System.out.println("png---------ok");
        	pngFile.createNewFile();
        }*/
        FileUtils.copyInputStreamToFile(processDiagram, pngFile);    
             
        // 7. Save resulting BPMN xml to a file    
        InputStream processBpmn = processEngine.getRepositoryService().getResourceAsStream(deployment.getId(), PROCESSID+".bpmn");    
        
        File bpmnFile = new File("deployments.bpmn");
        /*if(!bpmnFile.exists())
        	bpmnFile.createNewFile();*/
        FileUtils.copyInputStreamToFile(processBpmn,bpmnFile);  
          
        System.out.println(".........end...");  
    }  
      
    protected static ProcessEngine getProcessEngine(){  
        ProcessEngine processEngine=ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()  
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)  
                .setJdbcUrl("jdbc:mysql://192.168.1.124:3306/ag-admin")
                .setJdbcDriver("com.mysql.jdbc.Driver")  
                .setJdbcUsername("consignee")
                .setJdbcPassword("ztgeo")
                .setDatabaseSchemaUpdate("true")  
                .setJobExecutorActivate(false)  
                .buildProcessEngine();  
        return processEngine;  
                  
    }  
  
    /*任务节点*/  
    public static UserTask createUserTask(String id, String name, String candidateGroup) {  
        List<String> candidateGroups=new ArrayList<String>();  
        candidateGroups.add(candidateGroup);  
        UserTask userTask = new UserTask();  
        userTask.setName(name);  
        userTask.setId(id);  
        userTask.setCandidateGroups(candidateGroups);  
        return userTask;  
    }  
  
    /*连线*/  
    public static SequenceFlow createSequenceFlow(String from, String to,String name,String conditionExpression) {  
        SequenceFlow flow = new SequenceFlow();  
        flow.setSourceRef(from);  
        flow.setTargetRef(to);  
        flow.setName(name);  
        if(StringUtils.isNotEmpty(conditionExpression)){
            flow.setConditionExpression(conditionExpression);  
        }         
        return flow;  
    }  
      
    /*排他网关*/  
    public static ExclusiveGateway createExclusiveGateway(String id) {  
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();  
        exclusiveGateway.setName(id);
        exclusiveGateway.setId(id);  
        return exclusiveGateway;  
    }  
  
    /*开始节点*/  
    public static StartEvent createStartEvent() {  
        StartEvent startEvent = new StartEvent();  
        startEvent.setName("startEvent");
        startEvent.setId("startEvent");  
        return startEvent;  
    }  
  
    /*结束节点*/  
    public static EndEvent createEndEvent() {  
        EndEvent endEvent = new EndEvent();  
        endEvent.setName("endEvent");
        endEvent.setId("endEvent");  
        return endEvent;  
    }  
      
}  
