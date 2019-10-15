package com.ztgeo.general.entity.service_data.pub_data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Table(name = "sj_act_step")
public class SJ_Act_Step implements Serializable {
    @Id
    private String stepId;                  //步骤id主键
    private String stepMouldId;			    //步骤模板中id
    private String stepMouldName;			//步骤模板中名称
    private String stepShowName;            //步骤显示名称
    private String stepType;                //是userTask还是gateway
    private String parentStepMouldId;		//上一级步骤id
    private String processMouldId;			//流程模板中id
    private String alarmTerm;              //预警期限
    private String overdueTerm;             //超期办理期限
    private String status;			        //当前步骤状态
    private String ext1;			        //扩展字段1
    private String ext2;			        //扩展字段2

    private List<SJ_Act_Step> parentStepVoList;  //父级步骤集合
    private List<SJ_Act_Step> childStepVoList;   //子级步骤集合
    private List<SJ_Power_Step_Service> servicePowerVoList;  //涉及服务权限集合
    private List<SJ_Power_Step_Interface> interfacePowerVoList;  //服务直接接口集合


    public String getStepShowName() {
        return stepShowName;
    }

    public void setStepShowName(String stepShowName) {
        this.stepShowName = stepShowName;
    }

    public String getAlarmTerm() {
        return alarmTerm;
    }

    public void setAlarmTerm(String alarmTerm) {
        this.alarmTerm = alarmTerm;
    }

    public String getOverdueTerm() {
        return overdueTerm;
    }

    public void setOverdueTerm(String overdueTerm) {
        this.overdueTerm = overdueTerm;
    }

    public List<SJ_Act_Step> getChildStepVoList() {
        return childStepVoList;
    }

    public void setChildStepVoList(List<SJ_Act_Step> childStepVoList) {
        this.childStepVoList = childStepVoList;
    }

    public String getStepMouldId() {
        return stepMouldId;
    }

    public void setStepMouldId(String stepMouldId) {
        this.stepMouldId = stepMouldId;
    }

    public String getStepMouldName() {
        return stepMouldName;
    }

    public void setStepMouldName(String stepMouldName) {
        this.stepMouldName = stepMouldName;
    }

    public String getStepType() {
        return stepType;
    }

    public void setStepType(String stepType) {
        this.stepType = stepType;
    }

    public String getParentStepMouldId() {
        return parentStepMouldId;
    }

    public void setParentStepMouldId(String parentStepMouldId) {
        this.parentStepMouldId = parentStepMouldId;
    }

    public String getProcessMouldId() {
        return processMouldId;
    }

    public void setProcessMouldId(String processMouldId) {
        this.processMouldId = processMouldId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public List<SJ_Act_Step> getParentStepVoList() {
        return parentStepVoList;
    }

    public void setParentStepVoList(List<SJ_Act_Step> parentStepVoList) {
        this.parentStepVoList = parentStepVoList;
    }

    public List<SJ_Power_Step_Service> getServicePowerVoList() {
        return servicePowerVoList;
    }

    public void setServicePowerVoList(List<SJ_Power_Step_Service> servicePowerVoList) {
        this.servicePowerVoList = servicePowerVoList;
    }

    public List<SJ_Power_Step_Interface> getInterfacePowerVoList() {
        return interfacePowerVoList;
    }

    public void setInterfacePowerVoList(List<SJ_Power_Step_Interface> interfacePowerVoList) {
        this.interfacePowerVoList = interfacePowerVoList;
    }

    @Override
    public String toString() {
        return "SJ_Act_Step{" +
                "stepId='" + stepId + '\'' +
                ", stepMouldId='" + stepMouldId + '\'' +
                ", stepMouldName='" + stepMouldName + '\'' +
                ", stepType='" + stepType + '\'' +
                ", parentStepMouldId='" + parentStepMouldId + '\'' +
                ", processMouldId='" + processMouldId + '\'' +
                ", status='" + status + '\'' +
                ", ext1='" + ext1 + '\'' +
                ", ext2='" + ext2 + '\'' +'}';
    }
}
