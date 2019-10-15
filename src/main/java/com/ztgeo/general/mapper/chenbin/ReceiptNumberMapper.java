package com.ztgeo.general.mapper.chenbin;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface ReceiptNumberMapper {
    public Integer InitCurrentNumber(@Param("seqName")String seqName);
    public Integer getCurrentNumber(@Param("seqName")String seqName);
    public Integer getNextNumber(@Param("seqName")String seqName);
}
