package com.ztgeo.general.mapper.chenbin.tz;

import com.ztgeo.general.entity.tz_entity.TZBusinessData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SjTzDataMapper {
    List<TZBusinessData> selectTZBusinessDatas(@Param("receiptNumber")String receiptNumber);
    Integer insertTzBusinessData(TZBusinessData tzData);
    Integer updateTzBusinessData(TZBusinessData tzData);
}
