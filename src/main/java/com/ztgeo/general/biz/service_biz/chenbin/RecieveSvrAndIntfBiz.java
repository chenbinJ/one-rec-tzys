package com.ztgeo.general.biz.service_biz.chenbin;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Act_Step;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Service;
import org.activiti.engine.task.Task;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface RecieveSvrAndIntfBiz {
    SJ_Act_Step findStepServices(String taskId);
    SJ_Act_Step findStepServicesByStepId(String stepId);
    List<String> findCanWriteEntry(String mid);
    SJ_Service findOuterServiceById(String serviceId);
    List<Task> DealRecieveFromOuter5(Map<String,String> sjsq) throws ParseException;
}
