package com.ztgeo.general.controller;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.ztgeo.general.biz.DicItemBiz;
import com.ztgeo.general.entity.DicItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"普通字典的相关接口"})
@RestController
@RequestMapping("api/customDict")
//@CheckClientToken
//@CheckUserToken
public class DicItemController extends BaseController<DicItemBiz, DicItem> {

    //获取key->value
    @ApiOperation(value = "获取普通字典值(key=>value)",
            notes = "该接口返回普通字典值，返回item实体，具体的数据由调用者组织分析，参数为使用父级目录和子集详细菜单名称进行定位 如 阅读须知 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parParam", value = "父级菜单名称", dataType = "string", paramType = "query", example = "系统验证控制"),
            @ApiImplicitParam(name = "childParam", value = "子级key名称", dataType = "string", paramType = "query", example = "是否实名认证")})
    @PostMapping(value = "/getDicWithPandC")
    public ObjectRestResponse getDicWithPandC(String parParam, String childParam) {
        return new ObjectRestResponse<DicItem>().data(baseBiz.getDicWithPandC(parParam, childParam));
    }

    //根据目录名称获取普通字典列表集合
    @ApiOperation(value = "获取普通字典值(根据目录名称)",
            notes = "该接口返回普通字典值，返回List<item>实体，具体的数据由调用者组织分析，参数为使用目录名称进行定位(要求字典中最要出现重名的字典目录)")
    @ApiImplicitParam(name = "parParam", value = "父级菜单名称", dataType = "string", paramType = "query", example = "预约时间配置")
    @PostMapping(value = "/getDicListWithP")
    public ObjectRestResponse getDicListWithP(String parParam) {
        return new ObjectRestResponse<List<DicItem>>().data(baseBiz.getDicWithPandC(parParam));
    }

    //根据目录名称获取普通字典列表集合
    @ApiOperation(value = "获取普通字典值(根据id)",
            notes = "该接口返回普通字典值，返回List<item>实体，具体的数据由调用者组织分析，参数为使用目录名称进行定位(要求字典中最要出现重名的字典目录)")
    @PostMapping(value = "/getDicListWithDicCode")
    public ObjectRestResponse getDicListWithDicCode(String dicCode) {
        return new ObjectRestResponse<List<DicItem>>().data(baseBiz.getDicWithDicCode(dicCode));
    }
}