package com.ztgeo.general.mapper.chenbin;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Service;

import java.util.List;

public interface SJServiceMapper {

    /**
     * 根据用户id查询
     * @param uid
     * @return
     */
    List<SJ_Service> findSjServiceByUid(String uid);
    List<SJ_Service> findAllSjServices();
}
