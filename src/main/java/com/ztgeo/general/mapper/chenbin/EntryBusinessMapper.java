package com.ztgeo.general.mapper.chenbin;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EntryBusinessMapper {
    List<String> selectEntryByLevel(@Param("mId")String mId,@Param("powerLevel")String powerLevel,@Param("positionId")String positionId);
}
