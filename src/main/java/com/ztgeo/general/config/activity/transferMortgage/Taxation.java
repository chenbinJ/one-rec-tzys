package com.ztgeo.general.config.activity.transferMortgage;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 税务部门
 */
public class Taxation implements TaskListener {


    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        System.out.println("aaaaaaaaaa"+eventName);
        String[] empLoyees = {"swrenyuan","swrenyuan1","swrenyuan2"};
        delegateTask.addCandidateUsers(Arrays.asList(empLoyees));
    }
}
