package com.ztgeo.general.config.activity.transferMortgage;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 转移抵押  审核节点监听器
 */
public class Examine implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
        String eventName = delegateTask.getEventName();
        System.out.println("aaaaaaaaaa"+eventName);
        String[] empLoyees = {"ztgeo","ztgeo1","ztgeo2"};
        delegateTask.addCandidateUsers(Arrays.asList(empLoyees));
    }
}
