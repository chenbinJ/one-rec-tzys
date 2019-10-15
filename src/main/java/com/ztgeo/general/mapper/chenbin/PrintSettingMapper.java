package com.ztgeo.general.mapper.chenbin;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PrintSettingMapper {
    public List<String> selectModelIdByReceiptNumber(@Param("receiptNumber")String receiptNumber);
}
