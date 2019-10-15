package com.ztgeo.general.config.activity.ProcessCandidates.chenbin;

import com.ztgeo.general.component.penghao.ApproveComponent;
import com.ztgeo.general.config.activity.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class TaskInitLicenser implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {

    }
}
