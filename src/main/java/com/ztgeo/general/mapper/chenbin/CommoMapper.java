package com.ztgeo.general.mapper.chenbin;

import com.ztgeo.general.entity.Depart;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

public interface CommoMapper {

    Depart selectBelongDepart(@Param("userId")String userId);
}
