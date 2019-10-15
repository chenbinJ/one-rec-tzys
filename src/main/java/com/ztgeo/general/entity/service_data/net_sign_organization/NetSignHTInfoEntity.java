package com.ztgeo.general.entity.service_data.net_sign_organization;

import com.ztgeo.general.entity.service_data.intface_param.NetSignHTParamBodyEntity;

import java.io.Serializable;
import java.util.List;

public class NetSignHTInfoEntity implements Serializable {
    private NetSignHTParamBodyEntity htParam;       //合同查询条件信息
    private List<NetSignHTFWInfoEntity> htfws;      //合同房屋信息列表
    private List<NetSignHTQLRInfoEntity> htqlrs;    //合同权利人信息列表


    public List<NetSignHTFWInfoEntity> getHtfws() {
        return htfws;
    }

    public void setHtfws(List<NetSignHTFWInfoEntity> htfws) {
        this.htfws = htfws;
    }

    public List<NetSignHTQLRInfoEntity> getHtqlrs() {
        return htqlrs;
    }

    public void setHtqlrs(List<NetSignHTQLRInfoEntity> htqlrs) {
        this.htqlrs = htqlrs;
    }

    public NetSignHTParamBodyEntity getHtParam() {
        return htParam;
    }

    public void setHtParam(NetSignHTParamBodyEntity htParam) {
        this.htParam = htParam;
    }
}
