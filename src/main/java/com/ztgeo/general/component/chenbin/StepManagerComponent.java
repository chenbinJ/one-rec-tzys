package com.ztgeo.general.component.chenbin;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.chenbin.StepManagerBiz;
import com.ztgeo.general.biz.service_biz.chenbin.RecieveSvrAndIntfBiz;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Act_Step;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Act_Step_Gl;
import com.ztgeo.general.util.chenbin.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StepManagerComponent {

    @Autowired
    private StepManagerBiz stepBiz;
    @Autowired
    private RecieveSvrAndIntfBiz recSvrIntfBiz;

    //新流程的部署与修改
    public ObjectRestResponse<String> addAndMergeSteps(List<SJ_Act_Step> steps, List<SJ_Act_Step_Gl> stepGls){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(stepBiz.addAndMergeSteps(steps,stepGls));
    }

    //新流程的删除
    public ObjectRestResponse<String> removeProcessSteps(String processId){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(stepBiz.removeProcessSteps(processId));
    }

    //流程的禁用
    public ObjectRestResponse<String> discardStep(String processId){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(stepBiz.discardSteps(processId));
    }

    //流程禁用的解除
    public ObjectRestResponse<String> enableStep(String processId){
        ObjectRestResponse<String> rv = new ObjectRestResponse<String>();
        return rv.data(stepBiz.enableSteps(processId));
    }
    /*
     * 流程业务代码
     */
    public ObjectRestResponse<SJ_Act_Step> getStepSettings(String stepId){
        ObjectRestResponse<SJ_Act_Step> rv = new ObjectRestResponse<SJ_Act_Step>();
        return rv.data(recSvrIntfBiz.findStepServicesByStepId(stepId));
    }
    /*
     * 通过用户的岗位获取发起流程摸板的Ids
     */
    public ObjectRestResponse<List<String>> getAllStartProcessIds(){
        ObjectRestResponse<List<String>> rv = new ObjectRestResponse<List<String>>();
        return rv;
    }
}
