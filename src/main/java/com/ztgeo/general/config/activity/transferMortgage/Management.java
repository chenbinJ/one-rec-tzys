package com.ztgeo.general.config.activity.transferMortgage;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.Arrays;

/**
 * 房管部门
 */
public class Management implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        String element=delegateTask.getEventName();
        System.out.println(element);
        String[] empLoyees = {"liuxiaomei","liuxiaomei1","liuxiaomei2"};
        delegateTask.addCandidateUsers(Arrays.asList(empLoyees));
    }
}
