
package com.ztgeo.general.controller;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.ztgeo.general.biz.GroupTypeBiz;
import com.ztgeo.general.entity.GroupType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 组织
 */
@Controller
@RequestMapping("api/groupType")
public class GroupTypeController extends BaseController<GroupTypeBiz, GroupType> {
}
