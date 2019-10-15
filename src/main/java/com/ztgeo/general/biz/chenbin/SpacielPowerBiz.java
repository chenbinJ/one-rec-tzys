package com.ztgeo.general.biz.chenbin;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Spaciel_Position_Power;

public interface SpacielPowerBiz {
    public SJ_Spaciel_Position_Power findOne(String positionId);

    public String insertBody(String positionId);

    public String deleteOne(String positionId);
}
