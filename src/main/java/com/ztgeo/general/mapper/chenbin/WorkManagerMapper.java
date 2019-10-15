package com.ztgeo.general.mapper.chenbin;

import com.github.pagehelper.Page;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Exception_Record;
import com.ztgeo.general.entity.service_data.sys_data.ExampleRespBody;
import com.ztgeo.general.entity.service_data.sys_data.ExampleSearchParams;
import com.ztgeo.general.entity.service_data.sys_data.ExceptionSearchParams;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface WorkManagerMapper {
    Page<ExampleRespBody> selectExample(ExampleSearchParams params);
    Page<SJ_Exception_Record> selectException(ExceptionSearchParams params);
}
