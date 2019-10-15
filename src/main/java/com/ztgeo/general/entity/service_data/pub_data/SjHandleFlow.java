package com.ztgeo.general.entity.service_data.pub_data;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.validation.constraints.NotEmpty;
import java.text.SimpleDateFormat;
import java.util.Date;
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SjHandleFlow {
    private String handleId;

    private String handleNoticePerson;//通知人

    private String handleZjhm;//证件号码

    private String handleXb;//性别

    private String handleCsrq;//出生日期

    private String handleMz;//民族

    private String handleName;//姓名

    private String handleDz;//地址

    private String handleDh;//电话

    private String handleCreateBy;//创建人

    private Date handleCreateTime;//创建时间

    private String handleFzjg;//发证机关

    private String handleYxqsrq;//身份起始日期

    private String handleYxjzrq;//身份截止日期

    private String handleProcess;//实例

    private String handleSceneFile;//现场图片

    private String handleIdentityFile;//身份证图片

    private String picInSite;

    private String picInIdentityCard;

    private String handleCompateResult;//识别度


    public String getHandleId() {
        return handleId;
    }

    public void setHandleId(String handleId) {
        this.handleId = handleId;
    }

    public String getHandleNoticePerson() {
        return handleNoticePerson;
    }

    public void setHandleNoticePerson(String handleNoticePerson) {
        this.handleNoticePerson = handleNoticePerson;
    }

    public String getHandleZjhm() {
        return handleZjhm;
    }

    public void setHandleZjhm(String handleZjhm) {
        this.handleZjhm = handleZjhm;
    }

    public String getHandleXb() {
        return handleXb;
    }

    public void setHandleXb(String handleXb) {
        this.handleXb = handleXb;
    }

    public String getHandleCsrq() {
        return handleCsrq;
    }

    public void setHandleCsrq(String handleCsrq) {
        this.handleCsrq = handleCsrq;
    }

    public String getHandleMz() {
        return handleMz;
    }

    public void setHandleMz(String handleMz) {
        this.handleMz = handleMz;
    }

    public String getHandleName() {
        return handleName;
    }

    public void setHandleName(String handleName) {
        this.handleName = handleName;
    }

    public String getHandleDz() {
        return handleDz;
    }

    public void setHandleDz(String handleDz) {
        this.handleDz = handleDz;
    }

    public String getHandleDh() {
        return handleDh;
    }

    public void setHandleDh(String handleDh) {
        this.handleDh = handleDh;
    }

    public String getHandleCreateBy() {
        return handleCreateBy;
    }

    public void setHandleCreateBy(String handleCreateBy) {
        this.handleCreateBy = handleCreateBy;
    }

    public Date getHandleCreateTime() {
        return handleCreateTime;
    }

    public void setHandleCreateTime(Date handleCreateTime) {
        this.handleCreateTime = handleCreateTime;
    }

    public String getHandleFzjg() {
        return handleFzjg;
    }

    public void setHandleFzjg(String handleFzjg) {
        this.handleFzjg = handleFzjg;
    }

    public String getHandleYxqsrq() {
        return handleYxqsrq;
    }

    public void setHandleYxqsrq(String handleYxqsrq) {
        this.handleYxqsrq = handleYxqsrq;
    }

    public String getHandleYxjzrq() {
        return handleYxjzrq;
    }

    public void setHandleYxjzrq(String handleYxjzrq) {
        this.handleYxjzrq = handleYxjzrq;
    }

    public String getHandleProcess() {
        return handleProcess;
    }

    public void setHandleProcess(String handleProcess) {
        this.handleProcess = handleProcess;
    }

    public String getHandleSceneFile() {
        return handleSceneFile;
    }

    public void setHandleSceneFile(String handleSceneFile) {
        this.handleSceneFile = handleSceneFile;
    }

    public String getHandleIdentityFile() {
        return handleIdentityFile;
    }

    public void setHandleIdentityFile(String handleIdentityFile) {
        this.handleIdentityFile = handleIdentityFile;
    }

    public String getPicInSite() {
        return picInSite;
    }

    public void setPicInSite(String picInSite) {
        this.picInSite = picInSite;
    }

    public String getPicInIdentityCard() {
        return picInIdentityCard;
    }

    public void setPicInIdentityCard(String picInIdentityCard) {
        this.picInIdentityCard = picInIdentityCard;
    }

    public String getHandleCompateResult() {
        return handleCompateResult;
    }

    public void setHandleCompateResult(String handleCompateResult) {
        this.handleCompateResult = handleCompateResult;
    }
}