package com.ztgeo.general.controller.penghao;


import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.penghao.TemplateCategoryBiz;
import com.ztgeo.general.entity.service_data.pub_data.TemplateCategory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/categoryManager/templateStepManager")
@Api(tags = {"模板与大类关联API"})
public class TemplateCategoryController {

    @Autowired
    private TemplateCategoryBiz templateCategoryBiz;


    @RequestMapping(value = "/findTemplateByModel", method = RequestMethod.GET)
    @ApiOperation("通过模板展示大类")
    public Object findTemplateByModel(String mid) {
        ObjectRestResponse<Object> rv = new ObjectRestResponse<Object>();
        return rv.data(templateCategoryBiz.findTemplateByModel(mid));
    }

    @RequestMapping(value = "/insertTemplate", method = RequestMethod.POST)
    @ApiOperation("新增")
    public Object insertTemplate(TemplateCategory templateCategory) {
        return templateCategoryBiz.insertTemplate(templateCategory);
    }


}
