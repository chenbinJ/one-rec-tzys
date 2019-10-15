package com.ztgeo.general.mapper.chenbin;

import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Fjtm_Position;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface EntryPositManagerMapper extends CommonMapper<SJ_Power_Fjtm_Position> {
    public Integer deleteByParam(@Param("positId")String positId,@Param("entryId")String entryId);
    public Integer selectCountOfEntryPositSame(SJ_Power_Fjtm_Position entity);
}
