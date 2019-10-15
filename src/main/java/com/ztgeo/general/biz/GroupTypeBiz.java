
package com.ztgeo.general.biz;

import com.github.wxiaoqi.security.common.biz.BusinessBiz;
import com.ztgeo.general.entity.GroupType;
import com.ztgeo.general.mapper.GroupTypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wanghaobin
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GroupTypeBiz extends BusinessBiz<GroupTypeMapper, GroupType> {
}
