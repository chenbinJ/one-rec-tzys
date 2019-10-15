package com.ztgeo.general.controller;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.ztgeo.general.biz.DicUsrdefFieldBiz;
import com.ztgeo.general.entity.DicUsrdefField;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("api/dicUsrdefField")
public class DicUsrdefFieldController extends BaseController<DicUsrdefFieldBiz,DicUsrdefField> {

    @ApiOperation(value="通过diccode获取字段集合")
    @GetMapping("getByDicCode")
    public ObjectRestResponse<List<DicUsrdefField>> getByDicCode(String dicCode) {
        //传入的为dicCode
        DicUsrdefField dicUsrdefField = new DicUsrdefField();
        dicUsrdefField.setDiccode(dicCode);
        return new ObjectRestResponse<List<DicUsrdefField>>().data(baseBiz.selectList(dicUsrdefField));
    }
}