package com.ztgeo.general.entity.service_data.pub_data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
@Table(name = "sj_fjtm")
public class SJ_Fjtm implements Serializable {

    @Id
    private String entryId;            //附件条目id
    private String entryName;          //条目名称
    private String mappingName;       //映射名称
    private String processMouldId;     //隶属流程模板id
    private String node;               //业务说明
    private String createBy;           //创建人
    private String entryType;           //是否必选（针对整个流程）
    private Date createTime;         //创建时间
    private Date lastUpdate;         //上次修改时间
    private String status;             //是否可用
    private String ext1;               //扩展字段1
    private String ext2;               //扩展字段2

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public String getMappingName() {
        return mappingName;
    }

    public void setMappingName(String mappingName) {
        this.mappingName = mappingName;
    }

    public String getProcessMouldId() {
        return processMouldId;
    }

    public void setProcessMouldId(String processMouldId) {
        this.processMouldId = processMouldId;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
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

    @Override
    public String toString() {
        return "SJ_Fjtm{" +
                "entryId='" + entryId + '\'' +
                ", entryName='" + entryName + '\'' +
                ", processMouldId='" + processMouldId + '\'' +
                ", node='" + node + '\'' +
                ", createBy='" + createBy + '\'' +
                ", createTime='" + createTime + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                ", status='" + status + '\'' +
                ", ext1='" + ext1 + '\'' +
                ", ext2='" + ext2 + '\'' +
                '}';
    }
}
