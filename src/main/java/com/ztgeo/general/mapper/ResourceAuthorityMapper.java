
package com.ztgeo.general.mapper;

import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.ztgeo.general.entity.ResourceAuthority;
import org.apache.ibatis.annotations.Param;

public interface ResourceAuthorityMapper extends CommonMapper<ResourceAuthority> {
     void deleteByAuthorityIdAndResourceType(@Param("authorityId") String authorityId, @Param("resourceType") String resourceType, @Param("type") String type);
}
