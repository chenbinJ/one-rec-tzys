package com.ztgeo.general.component.penghao;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Act_Step;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.exceptionmsg.MsgManager;
import com.ztgeo.general.mapper.chenbin.StepManagerMapper;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StepComponent {

    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;

    @Autowired
    private StepManagerMapper stepManagerMapper;


    /**
     * 根据taskId查询所对应step
     * @param taskId
     * @return
     */
    public Object getSteps(String taskId){
        String stepId = null;
        String definitKey=null;
        //获得task对象
        Task task=taskService.createTaskQuery().taskId(taskId).singleResult();
        if (null == task){
            HistoricTaskInstance historicTaskInstance=historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
            definitKey=historicTaskInstance.getTaskDefinitionKey();
        }
        //查询所对应的stepId
        if(task!=null) {
            SJ_Act_Step sj_act_step = this.stepManagerMapper.selectStepByMouldId(task.getTaskDefinitionKey());
            if (sj_act_step != null) {
                stepId = sj_act_step.getStepId();
                return stepId;
            }
        }
        if (StringUtils.isEmpty(definitKey)){
            throw  new ZtgeoBizException(MsgManager.TASK_NULL);
        }
        return stepManagerMapper.selectStepByMouldId(definitKey).getStepId();
    }


   public  Object getStepByStartNode(String modelId){
        if (StringUtils.isEmpty(modelId) || modelId.equals("-1")){
            throw  new ZtgeoBizException(MsgManager.MODEL_ID_NULL);
        }
       SJ_Act_Step sjActStep=stepManagerMapper.findStepByModuleId(modelId);
        return sjActStep;
   }


}
