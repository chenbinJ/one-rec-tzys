package com.ztgeo.general.biz;
import com.github.wxiaoqi.security.common.biz.BusinessBiz;
import com.ztgeo.general.entity.Tenant;
import com.ztgeo.general.entity.User;
import com.ztgeo.general.mapper.TenantMapper;
import com.ztgeo.general.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 租户表
 * @author weihao
 */
@Service
public class TenantBiz extends BusinessBiz<TenantMapper, Tenant> {
    @Autowired
    private UserMapper userMapper;

    public void updateUser(String id, String userId) {
        Tenant tenant = this.mapper.selectByPrimaryKey(id);
        tenant.setOwner(userId);
        updateSelectiveById(tenant);
        User user = userMapper.selectByPrimaryKey(userId);
        user.setTenantId(id);
        userMapper.updateByPrimaryKeySelective(user);
    }
}