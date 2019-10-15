package com.ztgeo.general.entity.service_data.sys_data;

import com.ztgeo.general.entity.service_data.page.PageRequestBean;

import java.io.Serializable;

public class ExceptionSearchParams extends PageRequestBean implements Serializable {
    private String receiptNumber;               //收件编号
    private String registerNumber;              //受理编号
    private String businessName;                //业务名称
    private String excMsg;                      //异常信息
    private String taskDirection;               //任务方向
    private String noticeType;                  //通知类型
    private String handleStatus;                //处理状态
    private String happenTimeStart;             //触发起始节点
    private String happenTimeEnd;               //触发结束节点
    private String handleMan;                   //执行人

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getExcMsg() {
        return excMsg;
    }

    public void setExcMsg(String excMsg) {
        this.excMsg = excMsg;
    }

    public String getTaskDirection() {
        return taskDirection;
    }

    public void setTaskDirection(String taskDirection) {
        this.taskDirection = taskDirection;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getHappenTimeStart() {
        return happenTimeStart;
    }

    public void setHappenTimeStart(String happenTimeStart) {
        this.happenTimeStart = happenTimeStart;
    }

    public String getHappenTimeEnd() {
        return happenTimeEnd;
    }

    public void setHappenTimeEnd(String happenTimeEnd) {
        this.happenTimeEnd = happenTimeEnd;
    }

    public String getHandleMan() {
        return handleMan;
    }

    public void setHandleMan(String handleMan) {
        this.handleMan = handleMan;
    }
}
