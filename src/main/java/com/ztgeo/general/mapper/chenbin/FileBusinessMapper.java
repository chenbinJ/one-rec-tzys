package com.ztgeo.general.mapper.chenbin;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjfile;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Fjinst;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FileBusinessMapper {
    List<SJ_Fjinst> selectAllFjinstByProcInst(@Param("processInstanceId") String processInstanceId);
    String selectEntryNameById(@Param("entryId")String entryId);
    SJ_Fjfile selectFileByFileId(@Param("fileId")String fileId);
}
