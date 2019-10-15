package com.ztgeo.general.entity.service_data.pub_data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SJ_Fjinst implements Serializable {
    private String cid;             //树形附件id
    private String pid;             //父级目录id
    private String cname;           //当前文件或文件夹显示名称
    private String pnode;           //父节点（所属业务实例）
    private String pnodeType;       //父节点类型（实例）
    private String ctype;           //是否必选
    private String ckind;           //文件类型（文件夹/文件）
    private String fileId;          //附件id
    private String entryId;         //隶属条目id
    private Integer orderNumber;     //序号
    private String createTime;      //创建时间
    private String createBy;        //创建人
    private String status;          //状态（保留字段）
    private String ext1;            //扩展字段1
    private String ext2;            //扩展字段2
    private String ext3;            //扩展字段3
    private List<SJ_Fjinst> children=new ArrayList<>();//存储子级
    private String permissionLevel; //读/写 操作

    private SJ_Fjfile file;         //附件信息
    private List<SJ_Fjinst> fileInstVoList; //目录内包含的SJ_Fjinst

    public String getPowerTitle() {
        return permissionLevel;
    }

    public void setPowerTitle(String permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<SJ_Fjinst> getChildren() {
        return children;
    }

    public void setChildren(List<SJ_Fjinst> children) {
        this.children = children;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getPnode() {
        return pnode;
    }

    public void setPnode(String pnode) {
        this.pnode = pnode;
    }

    public String getPnodeType() {
        return pnodeType;
    }

    public void setPnodeType(String pnodeType) {
        this.pnodeType = pnodeType;
    }

    public String getCtype() {
        return ctype;
    }

    public void setCtype(String ctype) {
        this.ctype = ctype;
    }

    public String getCkind() {
        return ckind;
    }

    public void setCkind(String ckind) {
        this.ckind = ckind;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createTime);
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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

    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }

    public SJ_Fjfile getFile() {
        return file;
    }

    public void setFile(SJ_Fjfile file) {
        this.file = file;
    }

    public List<SJ_Fjinst> getFileInstVoList() {
        return fileInstVoList;
    }

    public void setFileInstVoList(List<SJ_Fjinst> fileInstVoList) {
        this.fileInstVoList = fileInstVoList;
    }
}
