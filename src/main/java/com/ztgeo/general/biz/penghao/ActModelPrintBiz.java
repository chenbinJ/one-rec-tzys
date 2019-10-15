package com.ztgeo.general.biz.penghao;

import com.ztgeo.general.entity.service_data.sys_data.ActModelPrint;

public interface ActModelPrintBiz {

    /**
     * 新增模板与打印关系
     *
     * @param actModelPrint
     * @return
     */
    Object insertModelPrint(ActModelPrint actModelPrint);

    /**
     * 根据模板Id查询模板与打印信息
     *
     * @param modelId
     * @return
     */
    Object selectByModelId(String modelId);

}
