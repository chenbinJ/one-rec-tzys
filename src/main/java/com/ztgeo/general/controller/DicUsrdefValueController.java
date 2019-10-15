package com.ztgeo.general.controller;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.TreeUtil;
import com.ztgeo.general.biz.DicUsrdefValueBiz;
import com.ztgeo.general.entity.DicUsrdefValue;
import com.ztgeo.general.vo.DicUsrdefValueTree;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
@Api(tags={"自定义树形菜单的相关接口"})
@RestController
@RequestMapping("api/commonDict")
public class DicUsrdefValueController extends BaseController<DicUsrdefValueBiz,DicUsrdefValue> {

    //获取自定义菜单的树形结构 ，主要用于第三方程序进行调用分析，建议调用方同步建模方便取值
    @ApiOperation(value="获取自定义菜单的树形结构",
            notes="主要用于第三方程序进行调用分析，建议调用方同步建模方便取值")
    @ApiImplicitParams({
            @ApiImplicitParam(name="dicCode",value="顶级节点id",dataType="string", paramType = "query")
    })
    @PostMapping(value = "/getModlue")
    public ObjectRestResponse getModlue(
             String dicCode
    ){
        List<DicUsrdefValue> dicUsrdefValues = baseBiz.getModlue(dicCode);
        List<DicUsrdefValueTree> trees = new ArrayList<>();
        dicUsrdefValues.forEach(dicUsrdefValue -> {
            trees.add(new DicUsrdefValueTree(dicUsrdefValue));
        });
        List<DicUsrdefValueTree> dicUsrdefValueTrees = TreeUtil.buildByRecursive(trees,dicUsrdefValues.get(0).getDiccode() );
        return new ObjectRestResponse<List<DicUsrdefValueTree>>().data(dicUsrdefValueTrees);
    }
}