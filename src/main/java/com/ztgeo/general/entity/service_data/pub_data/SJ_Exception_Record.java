package com.ztgeo.general.entity.service_data.pub_data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Table(name = "sj_exception_record")
public class SJ_Exception_Record implements Serializable {

    @Id
    private String id;                          //主键
    private String receiptNumber;               //收件编号
    private String registerNumber;              //受理编号
    private String businessName;                //业务名称
    private String departCode;                  //部门标识（目前没有）
    private String taskId;                      //任务ID
    private String excMsg;                      //异常信息
    private String taskDirection;               //任务执行方向
    private String noticeType;                  //通知类型
    private String noticeUrl;                   //通知处理的URL
    private String noticeText;                  //通知内容
    private String handleStatus;                //处理状态
    private String handleResult;                //处理结果
    private Date happenTime;                    //异常发生时间
    private Date handleTime;                    //异常处理时间
    private String handleMan;                   //处理人
    private String ext1;
    private String ext2;
    private String ext3;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getDepartCode() {
        return departCode;
    }

    public void setDepartCode(String departCode) {
        this.departCode = departCode;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public String getNoticeUrl() {
        return noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }

    public String getNoticeText() {
        return noticeText;
    }

    public void setNoticeText(String noticeText) {
        this.noticeText = noticeText;
    }

    public String getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
    }

    public String getHandleResult() {
        return handleResult;
    }

    public void setHandleResult(String handleResult) {
        this.handleResult = handleResult;
    }

    public Date getHappenTime() {
        return happenTime;
    }

    public void setHappenTime(Date happenTime) {
        this.happenTime = happenTime;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public String getHandleMan() {
        return handleMan;
    }

    public void setHandleMan(String handleMan) {
        this.handleMan = handleMan;
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

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public SJ_Exception_Record excMsgg(String msg){
        this.excMsg = msg;
        return this;
    }
}
