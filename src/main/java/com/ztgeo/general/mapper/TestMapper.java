package com.ztgeo.general.mapper;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Qlr_Gl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TestMapper {
    SJ_Qlr_Gl selectTest(@Param("infoId")String infoId);
}
