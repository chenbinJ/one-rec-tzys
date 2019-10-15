package com.ztgeo.general.config.activity.ProcessCandidates.chenbin;

import com.ztgeo.general.component.chenbin.OtherComponent;
import com.ztgeo.general.component.chenbin.WorkManagerComponent;
import com.ztgeo.general.component.penghao.PositionComponent;
import com.ztgeo.general.config.activity.SpringContextHolder;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.util.chenbin.ErrorDealUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
public class TaskStartLicenser implements TaskListener{

    @Override
    @Transactional
    public void notify(DelegateTask delegateTask){
        PositionComponent positionComponent= SpringContextHolder.getBean("positionComponent");
        String id=delegateTask.getTaskDefinitionKey();
        delegateTask.addCandidateUsers(positionComponent.getUserByPositionAndStepId(id));
        System.out.println("进入监听器2333");
//        OtherComponent otherComponent = SpringContextHolder.getBean("otherComponent");;
//        WorkManagerComponent workManagerComponent = SpringContextHolder.getBean("workManagerComponent");;
//        String stepModelId = delegateTask.getTaskDefinitionKey();
//        String taskId = delegateTask.getId();
//        String processInstId = delegateTask.getProcessInstanceId();
//        List<String> usernames = otherComponent.getExeDepartEmp(stepModelId,taskId,processInstId,delegateTask);
//        System.out.println("已获取"+delegateTask.getName()+"的签收人列表："+(usernames==null?"null":usernames.toString()));
//        if(usernames!=null){
//            if(usernames.size()<=0){//管理员跳过该步骤
//                System.out.println("管理员跳过");
////                List<Task> tasks = workManagerComponent.letSomeoneSubmit(taskId,processInstId,"admin");
////                try {
////                    workManagerComponent.dealAuto(tasks);//判断有无自动接口并执行
////                } catch (Exception e){
////                    e.printStackTrace();
////                    log.error("系统异常，异常信息为："+ ErrorDealUtil.getErrorInfo(e));
////                }
//                usernames.add("admin");
//                delegateTask.addCandidateUsers(usernames);
////                delegateTask.setAssignee("admin");
//            } else if(usernames.size()>0){//候选人待签收
//                System.out.println("执行签收");
//                delegateTask.addCandidateUsers(usernames);
//            }
//        }else{
//            throw new ZtgeoBizException("任务监听器“TaskStartLicenser”执行过程中出现不正常的情况，可能是由于步骤执行权限未配置导致，请排查");
//        }
    }
}
