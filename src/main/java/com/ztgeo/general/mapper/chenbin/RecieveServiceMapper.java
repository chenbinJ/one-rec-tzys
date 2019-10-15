package com.ztgeo.general.mapper.chenbin;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Act_Step;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface RecieveServiceMapper {
    //业务查询需求,获取接口服务配置
    public SJ_Act_Step selectActInfoByStepId(@Param("stepId") String stepId);
}
