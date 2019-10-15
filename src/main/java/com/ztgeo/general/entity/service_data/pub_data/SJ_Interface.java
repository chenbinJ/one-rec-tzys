package com.ztgeo.general.entity.service_data.pub_data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SJ_Interface implements Serializable {
    private String interfaceId;             //接口id
    private String interfaceCode;           //接口code
    private String interfaceName;           //接口名称
    private String interfaceURL;            //接口URL
    private String reqMethod;               //请求方式
    private String contentType;              //内容类型
    private String reqParamClass;           //接口参数类型
    private String respAdapterClass;        //适配类型
    private String respNode;                //返回值说明
    private String interfaceUse;            //接口用途（是否需要数据入库/仅作验证/做系统操作）
    private String useNode;                 //用途描述
    private String isRecord;                //接口使用信息是否被记录
    private String provideUnit;             //接口提供单位
    private String createBy;                //创建人
    private String createDate;              //创建时间
    private String status;                  //状态（0/1是否可用）
    private String ext1;                    //扩展字段1
    private String ext2;                    //扩展字段2

    private List<SJ_Interface_Params> paramVoList;       //参数

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getInterfaceCode() {
        return interfaceCode;
    }

    public void setInterfaceCode(String interfaceCode) {
        this.interfaceCode = interfaceCode;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceURL() {
        return interfaceURL;
    }

    public void setInterfaceURL(String interfaceURL) {
        this.interfaceURL = interfaceURL;
    }

    public String getReqMethod() {
        return reqMethod;
    }

    public void setReqMethod(String reqMethod) {
        this.reqMethod = reqMethod;
    }

    public String getRespNode() {
        return respNode;
    }

    public void setRespNode(String respNode) {
        this.respNode = respNode;
    }

    public String getInterfaceUse() {
        return interfaceUse;
    }

    public void setInterfaceUse(String interfaceUse) {
        this.interfaceUse = interfaceUse;
    }

    public String getUseNode() {
        return useNode;
    }

    public void setUseNode(String useNode) {
        this.useNode = useNode;
    }

    public String getIsRecord() {
        return isRecord;
    }

    public void setIsRecord(String isRecord) {
        this.isRecord = isRecord;
    }

    public String getProvideUnit() {
        return provideUnit;
    }

    public void setProvideUnit(String provideUnit) {
        this.provideUnit = provideUnit;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createDate);
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

    public List<SJ_Interface_Params> getParamVoList() {
        return paramVoList;
    }

    public void setParamVoList(List<SJ_Interface_Params> paramVoList) {
        this.paramVoList = paramVoList;
    }

    public String getReqParamClass() {
        return reqParamClass;
    }

    public void setReqParamClass(String reqParamClass) {
        this.reqParamClass = reqParamClass;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getRespAdapterClass() {
        return respAdapterClass;
    }

    public void setRespAdapterClass(String respAdapterClass) {
        this.respAdapterClass = respAdapterClass;
    }
}
