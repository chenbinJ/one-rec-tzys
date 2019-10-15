package com.ztgeo.general.config.activity.transferMortgage;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.Arrays;

/**
 * 登记局受理
 */
public class ReceivingOffice implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        String element=delegateTask.getEventName();
        System.out.println(element);
        String[] empLoyees = {"admin","admin1","admin2"};
        delegateTask.addCandidateUsers(Arrays.asList(empLoyees));
    }
}
