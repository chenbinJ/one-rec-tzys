package com.ztgeo.general.controller;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.ztgeo.general.biz.PositionBiz;
import com.ztgeo.general.entity.Position;
import com.ztgeo.general.entity.User;
import com.ztgeo.general.vo.DepartTree;
import com.ztgeo.general.vo.GroupTree;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("api/position")
public class PositionController extends BaseController<PositionBiz, Position> {

    @RequestMapping(value = "/{id}/user", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyUsers(@PathVariable String id, String users) {
        baseBiz.modifyPositionUsers(id, users);
        return new ObjectRestResponse();
    }

    @RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<User>> getUsers(@PathVariable("id") String positionId) {
        return new ObjectRestResponse<List<User>>().data(baseBiz.getPositionUsers(positionId));
    }

    @RequestMapping(value = "/{id}/group", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyRoles(@PathVariable String id, String groups) {
        baseBiz.modifyPositionGroups(id, groups);
        return new ObjectRestResponse();
    }

    @RequestMapping(value = "/{id}/group", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<GroupTree>> getRoles(@PathVariable("id") String positionId) {
        return new ObjectRestResponse<List<GroupTree>>().data(baseBiz.getPositionGroups(positionId));
    }

    @RequestMapping(value = "/{id}/depart", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifyDeparts(@PathVariable String id, String departs) {//先删后写的modify
        baseBiz.modifyPositionDeparts(id, departs);
        return new ObjectRestResponse();
    }

    @RequestMapping(value = "/{id}/depart", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<DepartTree>> getDeparts(@PathVariable("id") String positionId) {
        return new ObjectRestResponse<List<DepartTree>>().data(baseBiz.getPositionDeparts(positionId));
    }


}
