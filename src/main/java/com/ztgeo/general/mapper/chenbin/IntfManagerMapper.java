package com.ztgeo.general.mapper.chenbin;

import com.github.pagehelper.Page;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Interface;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Interface_Params;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface IntfManagerMapper {
    public Page<SJ_Interface> selectIntfsByPage(@Param("intfParam") String intfParam,@Param("status") String status);
    public SJ_Interface selectIntfById(@Param("intfId") String intfId);
    public List<SJ_Interface> selectAllEnableIntfs();
    public Integer insertIntf(SJ_Interface sjIntf);
    public Integer updateIntf(SJ_Interface sjIntf);
    public Integer updateReqParam(SJ_Interface sjIntf);
    public Integer updateIntfStatus(@Param("status") String status,@Param("intfId") String intfId);
    public Integer deleteIntfById(@Param("intfId") String intfId);

    public Integer updateIntfParamStatus(@Param("status") String status,@Param("intfId") String intfId,@Param("paramId") String paramId);
    public Integer deleteIntfParams(@Param("intfId") String intfId,@Param("paramId") String paramId);
    public Integer insertIntfParam(SJ_Interface_Params param);
    public Integer updateIntfParam(SJ_Interface_Params param);
    public List<SJ_Interface_Params> selectParamsByIntfid(@Param("intfId") String intfid);
    public SJ_Interface_Params selectSjIntfParamByParamid(@Param("paramId")String paramId);
}
