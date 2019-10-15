package com.ztgeo.general.mapper;

import com.ztgeo.general.entity.Position;

import java.util.List;

public interface PositionUserMapper {

    /**
     * 查询关系表用户对应的职位
     * @param uid
     * @return
     */
    List<Position> selectPositionByUid(String uid);

}
