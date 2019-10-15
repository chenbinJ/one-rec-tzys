package com.ztgeo.general.entity.service_data.bdc;

import com.ztgeo.general.entity.service_data.bdc.*;

import java.io.Serializable;
import java.util.List;

public class SynNewApplyEntity implements Serializable {
    private String pid;
    private String bizType;
    private boolean submitFlow;
    private String platform;
    private String operatorName;
    private String contacts;
    private String contactsPhone;
    private String contactsAdress;
    private String businessAreas;
    private String note;
    private SynNewBizInfoEntityAdvance advanceBizInfo;      //预告登记
    private SynNewBizInfoEntityMort mortgageBizInfo;        //抵押登记
    private SynNewBizInfoEntityRevoke revokeBizInfo;        //抵押注销
    private SynNewBizInfoEntityTransfer transferBizInfo;    //转移登记
    private List<SynNewFileEntity> fileInfoVoList;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public boolean isSubmitFlow() {
        return submitFlow;
    }

    public void setSubmitFlow(boolean submitFlow) {
        this.submitFlow = submitFlow;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public String getContactsAdress() {
        return contactsAdress;
    }

    public void setContactsAdress(String contactsAdress) {
        this.contactsAdress = contactsAdress;
    }

    public String getBusinessAreas() {
        return businessAreas;
    }

    public void setBusinessAreas(String businessAreas) {
        this.businessAreas = businessAreas;
    }

    public SynNewBizInfoEntityAdvance getAdvanceBizInfo() {
        return advanceBizInfo;
    }

    public void setAdvanceBizInfo(SynNewBizInfoEntityAdvance advanceBizInfo) {
        this.advanceBizInfo = advanceBizInfo;
    }

    public List<SynNewFileEntity> getFileInfoVoList() {
        return fileInfoVoList;
    }

    public void setFileInfoVoList(List<SynNewFileEntity> fileInfoVoList) {
        this.fileInfoVoList = fileInfoVoList;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public SynNewBizInfoEntityMort getMortgageBizInfo() {
        return mortgageBizInfo;
    }

    public void setMortgageBizInfo(SynNewBizInfoEntityMort mortgageBizInfo) {
        this.mortgageBizInfo = mortgageBizInfo;
    }

    public SynNewBizInfoEntityRevoke getRevokeBizInfo() {
        return revokeBizInfo;
    }

    public void setRevokeBizInfo(SynNewBizInfoEntityRevoke revokeBizInfo) {
        this.revokeBizInfo = revokeBizInfo;
    }

    public SynNewBizInfoEntityTransfer getTransferBizInfo() {
        return transferBizInfo;
    }

    public void setTransferBizInfo(SynNewBizInfoEntityTransfer transferBizInfo) {
        this.transferBizInfo = transferBizInfo;
    }
}
