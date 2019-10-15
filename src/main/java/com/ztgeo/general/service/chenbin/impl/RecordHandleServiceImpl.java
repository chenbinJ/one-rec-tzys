package com.ztgeo.general.service.chenbin.impl;

import com.ztgeo.general.entity.service_data.interface_response.Sj_Inquiry_Record;
import com.ztgeo.general.entity.service_data.interface_response.Sj_Inquiry_Record_Ext;
import com.ztgeo.general.mapper.chenbin.RecordHandleMapper;
import com.ztgeo.general.service.chenbin.RecordHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordHandleServiceImpl implements RecordHandleService {
    @Autowired
    private RecordHandleMapper recordHandleMapper;

    @Override
    public void addRecordInfos(Sj_Inquiry_Record record, List<Sj_Inquiry_Record_Ext> record_exts) {
        recordHandleMapper.insertRecord(record);
        for(Sj_Inquiry_Record_Ext record_ext:record_exts){
            recordHandleMapper.insertRecordExt(record_ext);
        }
    }
}
