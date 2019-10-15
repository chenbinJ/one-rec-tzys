package com.ztgeo.general.controller;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.ztgeo.general.biz.TenantBiz;
import com.ztgeo.general.biz.UserBiz;
import com.ztgeo.general.entity.Tenant;
import com.ztgeo.general.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 租户管理
 * @author ace
 */
@RestController
@RequestMapping("api/tenant")
public class TenantController extends BaseController<TenantBiz, Tenant> {
    @Autowired
    private UserBiz userBiz;
    @RequestMapping(value = "/{id}/user",method = RequestMethod.PUT)
    public ObjectRestResponse<Boolean> updateUser(@PathVariable("id") String id, String userId){
        baseBiz.updateUser(id,userId);
        return new ObjectRestResponse<>();
    }

    @RequestMapping(value = "/{id}/user",method = RequestMethod.GET)
    public ObjectRestResponse<User> updateUser(@PathVariable("id") String id){
        Tenant tenant = baseBiz.selectById(id);
        return new ObjectRestResponse<>().data(userBiz.selectById(tenant.getOwner()));
    }
}