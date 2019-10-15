package com.ztgeo.general.biz.chenbin;

import com.ztgeo.general.entity.service_data.pub_data.*;

import java.util.List;

public interface StepManagerBiz {
    public String addAndMergeSteps(List<SJ_Act_Step> steps, List<SJ_Act_Step_Gl> stepGls);

    public List<SJ_Act_Step> findProcessSteps(String processId);

    public String removeProcessSteps(String processId);

    public String modifyStep(SJ_Act_Step step);

    public String discardSteps(String processId);

    public String enableSteps(String processId);

    public SJ_Act_Step findStepByStepId(String stepId);

    public Object selectStepByPosition();

    //权限配置
    public List<SJ_Power_Step_Interface> searchStepIntfByStepId(String stepId);

    public List<SJ_Power_Step_Service> searchStepSvrByStepId(String stepId);

    public List<SJ_Power_Step_Position> searchStepPosionsByStepId(String stepId);

    public SJ_Power_Step_Interface findStepIntfByPowerId(String powerId);

    public SJ_Power_Step_Service findStepSvrByPowerId(String powerId);

    public SJ_Power_Step_Position findStepPositionByPowerId(String powerId);

    public String addStepIntf(SJ_Power_Step_Interface stepIntf);

    public String addStepSvr(SJ_Power_Step_Service stepSvr);

    public String addStepPosition(SJ_Power_Step_Position stepPosition);

    public String modifyStepIntf(SJ_Power_Step_Interface stepIntf);

    public String modifyStepSvr(SJ_Power_Step_Service stepSvr);

    public String modifyStepPosition(SJ_Power_Step_Position stepPosition);

    public String removeStepIntf(String powerId);

    public String removeStepSvr(String powerId);

    public String removeStepPosition(String powerId);

    public String findStepIdByStepMouldId(String stepMouldId);
}
