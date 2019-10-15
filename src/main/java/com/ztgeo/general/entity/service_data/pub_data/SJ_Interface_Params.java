package com.ztgeo.general.entity.service_data.pub_data;

import java.io.Serializable;

public class SJ_Interface_Params implements Serializable {
    private String paramId;                    //参数id
    private String interfaceId;                //所属接口id
    private String paramCode;                  //参数code（用于设定为key）
    private String paramName;                  //参数名称
    private String paramType;                  //参数类型(使用字典標示：字符串；日期時間；數字)
    private Integer orderNumber;                //参数序号
    private String isFolder;                    //是否附件参数
    private String status;                     //参数可用状态
    private String ext1;                       //扩展字段1
    private String ext2;                       //扩展字段2

    public String getParamId() {
        return paramId;
    }

    public void setParamId(String paramId) {
        this.paramId = paramId;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getParamCode() {
        return paramCode;
    }

    public void setParamCode(String paramCode) {
        this.paramCode = paramCode;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
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

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getIsFolder() {
        return isFolder;
    }

    public void setIsFolder(String isFolder) {
        this.isFolder = isFolder;
    }
}
