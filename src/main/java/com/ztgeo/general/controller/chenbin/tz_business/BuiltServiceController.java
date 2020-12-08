package com.ztgeo.general.controller.chenbin.tz_business;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Info_Immovable;
import com.ztgeo.general.entity.tz_entity.TZBuilt;
import com.ztgeo.general.entity.tz_entity.TZBusinessData;
import com.ztgeo.general.entity.tz_entity.statics.BuiltStaticsClass;
import com.ztgeo.general.service.chenbin.tz.BuiltService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tzApi/")
public class BuiltServiceController {

    @Autowired
    private BuiltService builtService;

    @RequestMapping(value = "/getBuilt",method = RequestMethod.GET)
    public ObjectRestResponse<List<SJ_Info_Immovable>> getBuilt(@RequestParam("queryParam") String queryParam){
        List<TZBuilt> builts = BuiltStaticsClass.tzBuilt;
        return new ObjectRestResponse<List<SJ_Info_Immovable>>().data(builtService.getImmovs(builts));
    }

    @RequestMapping(value = "/getBusiDatas",method = RequestMethod.GET)
    public ObjectRestResponse<List<TZBusinessData>> getBusiDatas(@RequestParam("receiptNumber")String receiptNumber){
        return new ObjectRestResponse<List<TZBusinessData>>().data(builtService.getBusiData(receiptNumber));
    }

    @RequestMapping(value = "/saveBusiDatas",method = RequestMethod.POST)
    public ObjectRestResponse<String> saveBusiDatas(@RequestBody List<TZBusinessData> tzDatas){
        System.out.println("进入保存");
        return new ObjectRestResponse<String>().data(builtService.saveBusiDatas(tzDatas));
    }
}
