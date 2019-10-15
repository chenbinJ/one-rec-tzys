package com.ztgeo.general.biz.penghao;

import com.ztgeo.general.entity.service_data.pub_data.SJ_Power_Step_Position;

import java.util.List;

public interface SjPowerStepPositionBiz {

    Object getPositionByStepId(String stepId);

    Object insetPositionList(List<SJ_Power_Step_Position> list, String stepId);

}
