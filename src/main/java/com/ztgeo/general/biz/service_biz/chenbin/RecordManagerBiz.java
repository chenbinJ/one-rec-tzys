package com.ztgeo.general.biz.service_biz.chenbin;

import com.ztgeo.general.entity.service_data.interface_response.Sj_Inquiry_Record;
import com.ztgeo.general.entity.service_data.intface_param.PersonnelUnitReqEntity;
import com.ztgeo.general.entity.service_data.page.PageResponseBean;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Service;
import com.ztgeo.general.entity.service_data.sys_data.RecordSearchParams;

import java.util.List;
import java.util.Map;

public interface RecordManagerBiz {
    PageResponseBean<Sj_Inquiry_Record> searchExceptionPage(RecordSearchParams params);

    Map<String, Object> findRecordInfo(String recordId);

    Object getRecordPDF(String recordId);

    Object downloadRecordPDF(String recordId);

    Map<String,Object> getPersonCheckResult(PersonnelUnitReqEntity personnelUnit);
}
