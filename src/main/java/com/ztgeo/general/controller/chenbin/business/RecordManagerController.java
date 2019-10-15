package com.ztgeo.general.controller.chenbin.business;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.service_biz.chenbin.RecordManagerBiz;
import com.ztgeo.general.component.pubComponent.ServiceAuthorizationComponent;
import com.ztgeo.general.entity.service_data.interface_response.Sj_Inquiry_Record;
import com.ztgeo.general.entity.service_data.intface_param.PersonnelUnitReqEntity;
import com.ztgeo.general.entity.service_data.page.PageResponseBean;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Service;
import com.ztgeo.general.entity.service_data.sys_data.RecordSearchParams;
import com.ztgeo.general.util.chenbin.IDUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/biz/recordBusi")
@Api(tags = {"查询服务执行记录管理-cb"})
public class RecordManagerController {

    @Autowired
    private RecordManagerBiz recordManagerBiz;

    @RequestMapping(value = "getRecordId",method = RequestMethod.GET)
    public ObjectRestResponse<String> getRecordId(){
        return new ObjectRestResponse<String>().data(IDUtil.getRecordId());
    }

    @RequestMapping(value = "getRecordList/page",method = RequestMethod.POST)
    public ObjectRestResponse<PageResponseBean<Sj_Inquiry_Record>> page(@RequestBody RecordSearchParams params){
        return new ObjectRestResponse<PageResponseBean<Sj_Inquiry_Record>>().data(recordManagerBiz.searchExceptionPage(params));
    }

    @RequestMapping(value = "getRecordInfo",method = RequestMethod.GET)
    public ObjectRestResponse<Map<String, Object>> getRecordInfo(@RequestParam("recordId")String recordId){
        return new ObjectRestResponse<Map<String, Object>>().data(recordManagerBiz.findRecordInfo(recordId));
    }

    @RequestMapping(value = "getRecordPDF", method = RequestMethod.POST)
    public ObjectRestResponse<Object> getRecordPDF(@RequestParam("recordId")String recordId){
        return new ObjectRestResponse<Object>().data(recordManagerBiz.getRecordPDF(recordId));
    }

    @RequestMapping(value = "downloadRecordPDF", method = RequestMethod.POST)
    public void downloadRecordPDF(@RequestParam("recordId")String recordId){

    }

    @RequestMapping(value = "getPersonCheckResult",method = RequestMethod.POST)
    public ObjectRestResponse<Object> getPersonCheckResult(@RequestBody PersonnelUnitReqEntity personnelUnit){
        return new ObjectRestResponse<Object>().data(recordManagerBiz.getPersonCheckResult(personnelUnit));
    }
}
