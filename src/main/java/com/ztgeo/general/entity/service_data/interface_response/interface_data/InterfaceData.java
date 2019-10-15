package com.ztgeo.general.entity.service_data.interface_response.interface_data;

import java.io.Serializable;

public class InterfaceData implements Serializable {
    private String id;
    private String interfaceCode;
    private String serviceCode;
    private String outerIdOrNo;
    private String pdfPath;
    private String reqParams;
    private String respBody;
    private String witchOuter;
    private String executor;
    private String checkTime;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInterfaceCode() {
        return interfaceCode;
    }

    public void setInterfaceCode(String interfaceCode) {
        this.interfaceCode = interfaceCode;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getOuterIdOrNo() {
        return outerIdOrNo;
    }

    public void setOuterIdOrNo(String outerIdOrNo) {
        this.outerIdOrNo = outerIdOrNo;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public String getReqParams() {
        return reqParams;
    }

    public void setReqParams(String reqParams) {
        this.reqParams = reqParams;
    }

    public String getRespBody() {
        return respBody;
    }

    public void setRespBody(String respBody) {
        this.respBody = respBody;
    }

    public String getWitchOuter() {
        return witchOuter;
    }

    public void setWitchOuter(String witchOuter) {
        this.witchOuter = witchOuter;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
