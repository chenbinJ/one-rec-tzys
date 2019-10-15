package com.ztgeo.general.config.activity.transferMortgage;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.Arrays;

/**
 *  缴费领证
 */
public class paymentCard implements TaskListener{


    @Override
    public void notify(DelegateTask delegateTask) {
        String element=delegateTask.getEventName();
        System.out.println(element);
        String[] empLoyees = {"tenant","tenant1","tenant2"};
        delegateTask.addCandidateUsers(Arrays.asList(empLoyees));
    }
}
