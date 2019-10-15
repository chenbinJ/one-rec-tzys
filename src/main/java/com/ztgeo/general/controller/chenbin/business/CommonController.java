package com.ztgeo.general.controller.chenbin.business;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.DepartBiz;
import com.ztgeo.general.biz.chenbin.impl.CommonBiz;
import com.ztgeo.general.component.pubComponent.ServiceAuthorizationComponent;
import com.ztgeo.general.entity.Depart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class CommonController {
    @Autowired
    private CommonBiz commoBiz;

    @Autowired
    private ServiceAuthorizationComponent svrAuthComponent;

    @RequestMapping(value = "getBelongDepart",method = RequestMethod.GET)
    public ObjectRestResponse<Depart> getBelongDepart(){
        Depart depart = null;
        try{
            depart = commoBiz.findBelongDepart();
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ObjectRestResponse<Depart>().data(depart);
    }

    @RequestMapping(value = "getSomeoneDepart",method = RequestMethod.GET)
    public ObjectRestResponse<Depart> getSomeoneDepart(@RequestParam("preservationMan") String preservationMan){
        Depart depart = null;
        try{
            depart = commoBiz.findSomeoneDepart(preservationMan);
        } catch (Exception e){
            e.printStackTrace();
        }
        return new ObjectRestResponse<Depart>().data(depart);
    }

    @RequestMapping(value = "checkAdmin",method = RequestMethod.GET)
    public ObjectRestResponse<Boolean> checkAdmin(){
        return new ObjectRestResponse<Boolean>().data(svrAuthComponent.checkAdmin());
    }
}
