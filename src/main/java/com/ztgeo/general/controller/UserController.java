package com.ztgeo.general.controller;

import com.github.ag.core.context.BaseContextHandler;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.ztgeo.general.biz.UserBiz;
import com.ztgeo.general.entity.User;
import com.ztgeo.general.vo.FrontUser;
import com.ztgeo.general.vo.MenuTree;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController extends BaseController<UserBiz, User> {

    @RequestMapping(value = "/front/info", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getUserInfo() throws Exception {
        FrontUser userInfo = baseBiz.getUserInfo();
        if(userInfo==null) {
            return ResponseEntity.status(401).body(false);
        } else {
            return ResponseEntity.ok(userInfo);
        }
    }

    @RequestMapping(value = "/front/menus", method = RequestMethod.GET)
    public @ResponseBody
    List<MenuTree> getMenusByUsername() throws Exception {
        return baseBiz.getMenusByUsername();
    }

    @RequestMapping(value = "/dataDepart",method = RequestMethod.GET)
    public List<String> getUserDataDepartIds(String userId){
        if(BaseContextHandler.getUserID().equals(userId)){
            return baseBiz.getUserDataDepartIds(userId);
        }
        return new ArrayList<>();
    }

//    @ApiOperation("账户修改密码")
//    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
//    public ObjectRestResponse<Boolean> changePassword(String oldPass, String newPass) {
//        return new ObjectRestResponse<Boolean>().data(baseBiz.changePassword(oldPass, newPass));
//    }

}