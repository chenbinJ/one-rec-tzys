package com.ztgeo.general.util.chenbin;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Act_Step;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Act_Step_Gl;

import java.util.ArrayList;
import java.util.List;

public class StepRelationUtil {
    //写入的步骤去除重复
    public static void killRepeat(List<SJ_Act_Step_Gl> gls) {
        //标记集合
        List<Integer> indexs = new ArrayList<Integer>();
        //执行判断
        for(int i=0; i<gls.size();i++) {
            boolean isContinue = false;
            for(Integer k:indexs) {
                if(i==k) {
                    isContinue=true;
                    break;
                }
            }
            if(isContinue) continue;

            SJ_Act_Step_Gl gli = gls.get(i);
            for(int j= gls.size()-1;j>i;j--) {
                SJ_Act_Step_Gl glj = gls.get(j);
                if(
                        gli.getChildStepId().equals(glj.getChildStepId()) &&
                        gli.getParentStepId().equals(glj.getParentStepId()) &&
                        gli.getProcessMouldId().equals(glj.getProcessMouldId())
                ) {
                    indexs.add(j);
                }
            }
        }

        //去除重复
        for(Integer k:indexs) {
            gls.remove(gls.get(k));
        }
    }

    //给查到的步骤配置子步骤集合
    public static void setChildSteps(List<SJ_Act_Step> steps_temp,List<SJ_Act_Step_Gl> step_gls){
        SJ_Act_Step step_start = null;
        for (SJ_Act_Step step_temp:steps_temp){
            List<SJ_Act_Step> childSteps = new ArrayList<SJ_Act_Step>();
            for(SJ_Act_Step_Gl step_gl:step_gls){
                if(step_gl.getParentStepId().equals(step_temp.getStepId())){
                    String childStepId = step_gl.getChildStepId();
                    for(SJ_Act_Step child_step:steps_temp){
                        if(child_step.getStepId().equals(childStepId)){
                            SJ_Act_Step childStep = new SJ_Act_Step();
                            childStep.setStepId(child_step.getStepId());
                            childStep.setStepMouldName(child_step.getStepMouldName());
                            childStep.setStepType(child_step.getStepType());
                            childSteps.add(childStep);
                            break;
                        }
                    }
                }
            }
            step_temp.setChildStepVoList(childSteps);
        }
    }

    //排序步骤集合
    public static List<SJ_Act_Step> sortSteps(List<SJ_Act_Step> steps_temp){
        List<SJ_Act_Step> steps = new ArrayList<SJ_Act_Step>();
        List<SJ_Act_Step> step_childs = new ArrayList<SJ_Act_Step>();;
        for(SJ_Act_Step step_temp:steps_temp){
            if(step_temp.getStepMouldName().equals("开始")){
                step_childs.add(step_temp);
                break;
            }
        }
        if(step_childs == null || step_childs.size()<1){
            return null;
        }
        doSort(step_childs,steps_temp,steps);
        return steps;
    }
    private static void doSort(List<SJ_Act_Step> step_childs,List<SJ_Act_Step> steps_temp,List<SJ_Act_Step> steps){
        for (SJ_Act_Step step_child:step_childs){
            boolean isCanDo = isCanDo(step_child,steps);
            if(isCanDo){//防止重复插入集合
                steps.add(step_child);
            }
        }

        List<SJ_Act_Step> start_steps = new ArrayList<SJ_Act_Step>();
        for(SJ_Act_Step step_child:step_childs){
            List<SJ_Act_Step> next_child_steps = step_child.getChildStepVoList();
            for(SJ_Act_Step next_child_step:next_child_steps){
                for (SJ_Act_Step step_temp:steps_temp){
                    if(next_child_step.getStepId().equals(step_temp.getStepId())){
                        if(isCanDo(step_temp,start_steps)){//去除重复
                            boolean isCan = true;
                            if(step_temp.getStepType()==null){
                                step_temp.setStepType("UserTask");
                            }
                            if(step_temp.getStepType().equals("ParallelGateway")){
                                String gatewayId = step_temp.getStepId();
                                for(SJ_Act_Step step_a:steps_temp){
                                    if(step_a.getChildStepVoList()!=null && step_a.getChildStepVoList().size()>0){
                                        if(step_a.getChildStepVoList().get(0).getStepId().equals(gatewayId)){//下一步为这个gateway
                                            if(isCanDo(step_a,steps)){//gateway之前的步骤是否全部在列表中,不在则不执行
                                                isCan = false;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            if(isCan){
                                start_steps.add(step_temp);
                            }
                        }
                        break;
                    }
                }
            }
        }
        if(start_steps!=null && start_steps.size()>0){
            doSort(start_steps,steps_temp,steps);
        }
    }

    private static boolean isCanDo(SJ_Act_Step step_temp,List<SJ_Act_Step> steps){
        boolean isCanDo = true;
        for(SJ_Act_Step step:steps){
            if(step.getStepId().equals(step_temp.getStepId())){
                isCanDo = false;
                break;
            }
        }
        return isCanDo;
    }
}
