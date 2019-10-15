package com.ztgeo.general.biz.penghao;

import com.ztgeo.general.entity.service_data.pub_data.TemplateCategory;

public interface TemplateCategoryBiz {

    Object findTemplateByModel(String mid);

    Object insertTemplate(TemplateCategory templateCategory);
}
