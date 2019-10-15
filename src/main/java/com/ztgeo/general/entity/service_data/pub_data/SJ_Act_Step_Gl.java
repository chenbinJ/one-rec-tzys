package com.ztgeo.general.entity.service_data.pub_data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SJ_Act_Step_Gl implements Serializable {
    private String relationId;         //主键
    private String parentStepId;       //父级步骤id
    private String childStepId;        //子级步骤id
    private String processMouldId;		//流程模板中id
    private String status;             //状态
    private List<SJ_Act_Step_Gl> children=new ArrayList<>();//存放子级集合


    public List<SJ_Act_Step_Gl> getChildren() {
        return children;
    }

    public void setChildren(List<SJ_Act_Step_Gl> children) {
        this.children = children;
    }

    public String getProcessMouldId() {
        return processMouldId;
    }

    public void setProcessMouldId(String processMouldId) {
        this.processMouldId = processMouldId;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getParentStepId() {
        return parentStepId;
    }

    public void setParentStepId(String parentStepId) {
        this.parentStepId = parentStepId;
    }

    public String getChildStepId() {
        return childStepId;
    }

    public void setChildStepId(String childStepId) {
        this.childStepId = childStepId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    @Override
    public String toString() {
        return "SJ_Act_Step_Gl{" +
                "relationId='" + relationId + '\'' +
                ", parentStepId='" + parentStepId + '\'' +
                ", childStepId='" + childStepId + '\'' +
                ", processMouldId='" + processMouldId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
