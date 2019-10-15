package com.ztgeo.general.config.activity.transferMortgage;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.Arrays;

/**
 * 抵押合同监听器
 */
public class MortgageContract implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        //名称
        String element=delegateTask.getEventName();
        //Id
        String id=delegateTask.getTaskDefinitionKey();
        System.out.println(element);
        String[] empLoyees = {"admin","admin1","admin2"};
        delegateTask.addCandidateUsers(Arrays.asList(empLoyees));
    }
}
