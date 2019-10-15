package com.ztgeo.general.controller;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.TreeUtil;
import com.ztgeo.general.biz.DicMainBiz;
import com.ztgeo.general.entity.DicMain;
import com.ztgeo.general.vo.DictMainTree;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("api/dicMain")
//@CheckClientToken
//@CheckUserToken
public class DicMainController extends BaseController<DicMainBiz,DicMain> {

    //获取整课目录树 纯数据返回 用于前台展示
    @ApiOperation(value="获取目录菜单的组件树",
            notes="该接口返回字典目录的完整树形结构，但不包含具体的字典目录对应的字典内容，需要调用其他接口完成")
    @GetMapping("/tree")
    public ObjectRestResponse getDicMainTree(){
        List<DicMain> dicMains = baseBiz.selectListAll();
        List<DictMainTree> trees = new ArrayList<>();
        dicMains.forEach(dicMain -> {
            trees.add(new DictMainTree(dicMain.getDiccode(),dicMain.getPid(),dicMain.getDicname(),dicMain.getDictype(),dicMain.getTablename(),dicMain.getDicnote()));
        });
        List<DictMainTree> dictMainTrees = TreeUtil.buildByRecursive(trees, "-1");
        //List<DictMainTree> dictMainTrees = TreeUtil.bulid(trees, "-1",null);
        return new ObjectRestResponse<List<DictMainTree>>().data(dictMainTrees);
    }

}