package com.ztgeo.general.mapper;

import com.github.wxiaoqi.security.common.data.Tenant;
import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.ztgeo.general.entity.Depart;
import com.ztgeo.general.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Tenant
public interface DepartMapper extends CommonMapper<Depart> {

    List<User> selectDepartUsers(@Param("departId") String departId, @Param("userName") String userName);

    List<Depart> selectAllDepart();

    List<Depart> selectDepartsByName(@Param("departName") String departName);

    void deleteDepartUser(@Param("departId")String departId, @Param("userId") String userId);

    void insertDepartUser(@Param("id") String id, @Param("departId") String departId, @Param("userId") String userId,@Param("tenantId") String tenantId);

}
