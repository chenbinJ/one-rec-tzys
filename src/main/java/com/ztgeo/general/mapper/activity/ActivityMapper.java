package com.ztgeo.general.mapper.activity;

import com.ztgeo.general.entity.activity.ActivityPojo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ActivityMapper {
    /**
     * 查询工作流信息
     * @param activityPojo
     * @return
     */
    List<ActivityPojo> selectForARP(ActivityPojo activityPojo);

}
