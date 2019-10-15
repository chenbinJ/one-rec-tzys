package com.ztgeo.general.mapper.penghao;

import com.ztgeo.general.entity.service_data.pub_data.TemplateCategory;

import java.util.List;

public interface TemplateCategoryMapper {
    int deleteByPrimaryKey(String templateId);

    void  deleteByModel(String mid);

    int insert(TemplateCategory record);

    TemplateCategory findTemplateByModel(String mid);

    int insertSelective(TemplateCategory record);

    TemplateCategory selectByPrimaryKey(String templateId);

    int updateByPrimaryKeySelective(TemplateCategory record);

    int updateByPrimaryKey(TemplateCategory record);
}