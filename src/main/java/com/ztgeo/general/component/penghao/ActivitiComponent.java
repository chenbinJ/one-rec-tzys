package com.ztgeo.general.component.penghao;


import com.ztgeo.general.entity.activity.Approve;
import com.ztgeo.general.entity.activity.HistoryPojo;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Act_Step;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Step_Interface;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Sjsq;
import com.ztgeo.general.mapper.activity.ApproveMapper;
import com.ztgeo.general.mapper.chenbin.StepManagerMapper;
import com.ztgeo.general.service.activity.WorkFlowOperateService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public class ActivitiComponent {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private WorkFlowOperateService workFlowOperateService;

    @Autowired
    private HistoryService historyService;











    /**
     * 根据流程实列获得模板Id
     * @param processId
     * @return
     */
    public  String getActivityByProcessId(String processId){
        Deployment deployment=null;
        boolean flag=workFlowOperateService.processIsEnd(processId);
        if (flag==true){
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processId).singleResult();
            deployment=repositoryService.createDeploymentQuery().deploymentId(historicProcessInstance.getDeploymentId()).singleResult();
        }else {
            //获得流程对象
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();
            //根据流程对象获得部署对象
             deployment=repositoryService.createDeploymentQuery().deploymentId(processInstance.getDeploymentId()).singleResult();
        }
        Model model= repositoryService.createModelQuery().modelName(deployment.getName()).singleResult();
        return model.getId();
    }



    /**
     * 根据流程实列获得大类Id
     * @param processId
     * @return
     */
    public  String getTemplateByModel(String processId){
        String model=getActivityByProcessId(processId);
        //根据模板
        return null;
    }


}
