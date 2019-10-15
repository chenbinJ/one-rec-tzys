package com.ztgeo.general.biz.service_biz.chenbin;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.entity.service_data.page.PageResponseBean;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Exception_Record;
import com.ztgeo.general.entity.service_data.sys_data.ExampleRespBody;
import com.ztgeo.general.entity.service_data.sys_data.ExampleSearchParams;
import com.ztgeo.general.entity.service_data.sys_data.ExceptionSearchParams;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

public interface WorkManagerBiz {
    PageResponseBean<ExampleRespBody> searchExample(ExampleSearchParams params);

    String handleAutoInterfaceByManager(String taskId);

    String handleSubExceptionByManager(String excId);

    Integer handleExceptionFaild(String excId);

    PageResponseBean<SJ_Exception_Record> searchException(ExceptionSearchParams params);
}
