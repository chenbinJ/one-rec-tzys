package com.ztgeo.general.service.chenbin;

import com.ztgeo.general.entity.service_data.interface_response.Sj_Inquiry_Record;
import com.ztgeo.general.entity.service_data.interface_response.Sj_Inquiry_Record_Ext;

import java.util.List;

public interface RecordHandleService {
    public void addRecordInfos(Sj_Inquiry_Record record, List<Sj_Inquiry_Record_Ext> record_exts);
}
