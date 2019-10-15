package com.ztgeo.general.entity.service_data.bdc;

import java.io.Serializable;

public class SynNewDLREntity implements Serializable{
    private String agentName;
    private String agentId;
    private String agentIdType;

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentIdType() {
        return agentIdType;
    }

    public void setAgentIdType(String agentIdType) {
        this.agentIdType = agentIdType;
    }
}
