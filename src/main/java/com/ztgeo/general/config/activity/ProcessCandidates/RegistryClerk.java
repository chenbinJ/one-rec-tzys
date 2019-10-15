package com.ztgeo.general.config.activity.ProcessCandidates;

import com.ztgeo.general.component.penghao.PositionComponent;
import com.ztgeo.general.config.activity.SpringContextHolder;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 登记局直接设置审批人
 */
public class RegistryClerk implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        PositionComponent positionComponent= SpringContextHolder.getBean("positionComponent");
        String id=delegateTask.getTaskDefinitionKey();
        delegateTask.addCandidateUsers(positionComponent.getUserByPositionAndStepId(id));
    }

}
