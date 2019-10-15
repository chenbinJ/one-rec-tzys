package com.ztgeo.general.entity.service_data.sys_data;

import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.TaskInfo;

import java.util.Date;

public interface MyTask extends TaskInfo {
    void setHistory(String history);

    void setId(String id);

    void setExecutionId(String executionId);

    void setProcessDefinitionId(String processDefinitionId);

    void setTaskDefinitionKey(String taskDefinitionKey);

    void setProcessInstanceId(String processInstanceId);

    void setCreateTime(Date createTime);

    String getHistory();

    void setName(String var1);

    void setLocalizedName(String var1);

    void setDescription(String var1);

    void setLocalizedDescription(String var1);

    void setPriority(int var1);

    void setOwner(String var1);

    void setAssignee(String var1);

    DelegationState getDelegationState();

    void setDelegationState(DelegationState var1);

    void setDueDate(Date var1);

    void setCategory(String var1);

    void delegate(String var1);

    void setParentTaskId(String var1);

    void setTenantId(String var1);

    void setFormKey(String var1);

    boolean isSuspended();
}
