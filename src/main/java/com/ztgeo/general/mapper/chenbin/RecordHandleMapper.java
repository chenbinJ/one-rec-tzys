package com.ztgeo.general.mapper.chenbin;

import com.github.pagehelper.Page;
import com.ztgeo.general.entity.service_data.interface_response.Sj_Inquiry_Record;
import com.ztgeo.general.entity.service_data.interface_response.Sj_Inquiry_Record_Ext;
import com.ztgeo.general.entity.service_data.interface_response.interface_data.InterfaceData;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Service;
import com.ztgeo.general.entity.service_data.sys_data.RecordSearchParams;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RecordHandleMapper {
    Integer insertRecord(Sj_Inquiry_Record record);
    Integer insertRecordExt(Sj_Inquiry_Record_Ext record_ext);

    Page<Sj_Inquiry_Record> selectRecords(RecordSearchParams params);
    Sj_Inquiry_Record selectRecordById(@Param("recordId")String recordId);
    List<Sj_Inquiry_Record_Ext> selectRecordExts(@Param("recordId")String recordId);

    Integer updateRecordForPdf(@Param("recordId")String recordId,@Param("pdfPath")String pdfPath);

    List<InterfaceData> selectInterfaceDatasWithOuterId(@Param("outerId")String outerId,@Param("interfaceCode") String interfaceCode);
}
