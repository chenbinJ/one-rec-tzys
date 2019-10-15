package com.ztgeo.general.controller;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.TreeUtil;
import com.ztgeo.general.biz.GroupBiz;
import com.ztgeo.general.biz.ResourceAuthorityBiz;
import com.ztgeo.general.constant.AdminCommonConstant;
import com.ztgeo.general.entity.Element;
import com.ztgeo.general.entity.Group;
import com.ztgeo.general.vo.AuthorityMenuTree;
import com.ztgeo.general.vo.GroupTree;
import com.ztgeo.general.vo.GroupUsers;
import com.ztgeo.general.vo.MenuTree;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * weihao
 */
@RestController
@RequestMapping("api/group")
@Api("角色模块")
public class GroupController extends BaseController<GroupBiz, Group> {
    @Autowired
    private ResourceAuthorityBiz resourceAuthorityBiz;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Group> list(String name, String groupType) {
        if (StringUtils.isBlank(name) && StringUtils.isBlank(groupType)) {
            return new ArrayList<Group>();
        }
        Example example = new Example(Group.class);
        if (StringUtils.isNotBlank(name)) {
            example.createCriteria().andLike("name", "%" + name + "%");
        }
        if (StringUtils.isNotBlank(groupType)) {
            example.createCriteria().andEqualTo("groupType", groupType);
        }

        return baseBiz.selectByExample(example);
    }


    @RequestMapping(value = "/{id}/user", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifiyUsers(@PathVariable String id, String members, String leaders) {
        baseBiz.modifyGroupUsers(id, members, leaders);
        return new ObjectRestResponse();
    }

    @RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<GroupUsers> getUsers(@PathVariable String id) {
        return new ObjectRestResponse<GroupUsers>().data(baseBiz.getGroupUsers(id));
    }

    @RequestMapping(value = "/{id}/authority/menu", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse modifyMenuAuthority(@PathVariable String id, String menuTrees) {
        String[] menus = menuTrees.split(",");
        baseBiz.modifyAuthorityMenu(id, menus, AdminCommonConstant.RESOURCE_TYPE_VIEW);
        return new ObjectRestResponse();
    }

    @RequestMapping(value = "/{id}/authority/menu", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<AuthorityMenuTree>> getMenuAuthority(@PathVariable String id) {
        return new ObjectRestResponse().data(baseBiz.getAuthorityMenu(id, AdminCommonConstant.RESOURCE_TYPE_VIEW));
    }

    @RequestMapping(value = "/{id}/authority/element/add", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse addElementAuthority(@PathVariable String id, String menuId, String elementId) {
        baseBiz.modifyAuthorityElement(id, menuId, elementId, AdminCommonConstant.RESOURCE_TYPE_VIEW);
        return new ObjectRestResponse();
    }

    @RequestMapping(value = "/{id}/authority/element/remove", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse removeElementAuthority(@PathVariable String id, String menuId, String elementId) {
        baseBiz.removeAuthorityElement(id, elementId, AdminCommonConstant.RESOURCE_TYPE_VIEW);
        return new ObjectRestResponse();
    }

    @RequestMapping(value = "/{id}/authority/element", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<String>> getElementAuthority(@PathVariable String id) {
        return new ObjectRestResponse().data(baseBiz.getAuthorityElement(id, AdminCommonConstant.RESOURCE_TYPE_VIEW));
    }

    @RequestMapping(value = "/{id}/authorize/menu", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse modifyMenuAuthorize(@PathVariable String id, String menuTrees) {
        String[] menus = menuTrees.split(",");
        baseBiz.modifyAuthorityMenu(id, menus, AdminCommonConstant.RESOURCE_TYPE_AUTHORISE);
        return new ObjectRestResponse();
    }

    @RequestMapping(value = "/{id}/authorize/menu", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<AuthorityMenuTree>> getMenuAuthorize(@PathVariable String id) {
        return new ObjectRestResponse().data(baseBiz.getAuthorityMenu(id, AdminCommonConstant.RESOURCE_TYPE_AUTHORISE));
    }

    @RequestMapping(value = "/{id}/authorize/element/add", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse addElementAuthorize(@PathVariable String id, String menuId, String elementId) {
        baseBiz.modifyAuthorityElement(id, menuId, elementId, AdminCommonConstant.RESOURCE_TYPE_AUTHORISE);
        return new ObjectRestResponse();
    }

    @RequestMapping(value = "/{id}/authorize/element/remove", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse removeElementAuthorize(@PathVariable String id, String menuId, String elementId) {
        baseBiz.removeAuthorityElement(id, elementId, AdminCommonConstant.RESOURCE_TYPE_AUTHORISE);
        return new ObjectRestResponse();
    }

    @RequestMapping(value = "/{id}/authorize/element", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<String>> getElementAuthorize(@PathVariable String id) {
        return new ObjectRestResponse().data(baseBiz.getAuthorityElement(id, AdminCommonConstant.RESOURCE_TYPE_AUTHORISE));
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public List<GroupTree> tree(String name, String groupType) {
        Example example = new Example(Group.class);
        if (StringUtils.isNotBlank(name)) {
            example.createCriteria().andLike("name", "%" + name + "%");
        }
        if (StringUtils.isNotBlank(groupType)) {
            example.createCriteria().andEqualTo("groupType", groupType);
        }
        return getTree(baseBiz.selectByExample(example), AdminCommonConstant.ROOT);
    }

    /**
     * 获取可管理的资源
     * @param menuId
     * @return
     */
    @RequestMapping(value = "/element/authorize/list", method = RequestMethod.GET)
    public TableResultResponse<Element> getAuthorizeElement(String menuId) {
        List<Element> elements = baseBiz.getAuthorizeElements(menuId);
        return new TableResultResponse<Element>(elements.size(), elements);
    }

    /**
     * 获取可管理的菜单
     * @return
     */
    @RequestMapping(value = "/menu/authorize/list", method = RequestMethod.GET)
    public List<MenuTree> getAuthorizeMenus() {
        return TreeUtil.bulid(baseBiz.getAuthorizeMenus(), AdminCommonConstant.ROOT, null);
    }

    private List<GroupTree> getTree(List<Group> groups, String root) {
        List<GroupTree> trees = new ArrayList<GroupTree>();
        GroupTree node = null;
        for (Group group : groups) {
            node = new GroupTree();
            node.setLabel(group.getName());
            BeanUtils.copyProperties(group, node);
            trees.add(node);
        }
        return TreeUtil.bulid(trees, root, null);
    }
}
