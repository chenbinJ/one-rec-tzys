package com.ztgeo.general.config.activity.ProcessCandidates;

import com.ztgeo.general.component.penghao.PositionComponent;
import com.ztgeo.general.config.activity.SpringContextHolder;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 设置候选人
 */
public class Candidates implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        PositionComponent positionComponent= SpringContextHolder.getBean("positionComponent");
        String id=delegateTask.getTaskDefinitionKey();
        delegateTask.addCandidateUsers(positionComponent.getUserByPositionAndStepId(id));
    }
}
