package com.ztgeo.general.service.chenbin.tz;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Info_Immovable;
import com.ztgeo.general.entity.tz_entity.TZBuilt;
import com.ztgeo.general.entity.tz_entity.TZBusinessData;

import java.util.List;

public interface BuiltService {
    List<SJ_Info_Immovable> getImmovs(List<TZBuilt> builts);
    List<TZBusinessData> getBusiData(String receiptNumber);
    String saveBusiDatas(List<TZBusinessData> tzDatas);
}
