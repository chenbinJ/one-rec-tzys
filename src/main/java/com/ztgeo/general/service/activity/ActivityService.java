package com.ztgeo.general.service.activity;

import com.ztgeo.general.entity.activity.ActivityPojo;

import java.io.IOException;
import java.util.List;

public interface ActivityService {

    /**
     * 查询全部定义工作流
     *
     * @param activityPojo
     * @return
     */
    List<ActivityPojo> selectForARP(ActivityPojo activityPojo);


    /**
     * 获取流程图,
     *
     * @param id
     * @return
     */
    Object getFlowchartPresentation(String id, String processId) throws IOException;


    /**
     * 分页查询全部定义工作流
     *
     * @param activityPojo
     * @return
     */
    Object selectForARPAndRowsAndPage(ActivityPojo activityPojo);

}
