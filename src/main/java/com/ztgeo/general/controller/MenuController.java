/*
 *  Copyright (C) 2018  Wanghaobin<463540703@qq.com>

 *  AG-Enterprise 企业版源码
 *  郑重声明:
 *  如果你从其他途径获取到，请告知老A传播人，奖励1000。
 *  老A将追究授予人和传播人的法律责任!

 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.ztgeo.general.controller;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.ztgeo.general.biz.MenuBiz;
import com.ztgeo.general.biz.UserBiz;
import com.ztgeo.general.constant.AdminCommonConstant;
import com.ztgeo.general.entity.Menu;
import com.ztgeo.general.vo.MenuTree;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @version 2017-06-12 8:49
 */
@Controller
@RequestMapping("api/menu")
public class MenuController extends BaseController<MenuBiz, Menu> {
    @Autowired
    private UserBiz userBiz;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Menu> list(String title) {
        Example example = new Example(Menu.class);
        if (StringUtils.isNotBlank(title)) {
            example.createCriteria().andLike("title", "%" + title + "%");
        }
        return baseBiz.selectByExample(example);
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public List<MenuTree> getTree(String title) {
        Example example = new Example(Menu.class);
        if (StringUtils.isNotBlank(title)) {
            example.createCriteria().andLike("title", "%" + title + "%");
        }
        return MenuTree.buildTree(baseBiz.selectByExample(example), AdminCommonConstant.ROOT);
    }
}
