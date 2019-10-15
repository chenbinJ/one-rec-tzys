package com.ztgeo.general.mapper.penghao;

import com.ztgeo.general.entity.Depart;

import java.util.List;

public interface DepartUserMapper {

    /**
     * 通过userId查询部门
     * @param userId
     * @return
     */
    List<Depart> getDepartByUserId(String userId);
}
