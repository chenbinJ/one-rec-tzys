package com.ztgeo.general.mapper.penghao;

import com.ztgeo.general.entity.extend.Fjtm_Power;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjfile;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjinst;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjtm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SJ_FjtmMapper {
    int deleteByPrimaryKey(String entryId);

    int insert(SJ_Fjfile record);

    int insertSelective(SJ_Fjtm record);



    /**
     * 通过岗位,模板查询对应权限
     * @param list
     * @return
     */
    List<Fjtm_Power> findFjtmPowerByPosition(List<String> list,String mid);


    List<Fjtm_Power> findFjtmByAdmin(String mid);

    SJ_Fjfile selectByPrimaryKey(String entryId);

    /**
     * 通过模板id查询条目
     * @param mid
     * @return
     */
    List<SJ_Fjtm> selectByMid(String mid);

    int updateByPrimaryKeySelective(SJ_Fjfile record);

    int updateByPrimaryKeyWithBLOBs(SJ_Fjfile record);

    int updateByPrimaryKey(SJ_Fjfile record);
}