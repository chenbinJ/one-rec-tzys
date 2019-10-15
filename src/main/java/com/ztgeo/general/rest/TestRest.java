package com.ztgeo.general.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.service_biz.chenbin.RecieveDataBusinessBiz;
import com.ztgeo.general.component.chenbin.StepManagerComponent;
import com.ztgeo.general.component.pubComponent.ReceiptNumberComponent;
import com.ztgeo.general.component.pubComponent.ServiceAuthorizationComponent;
import com.ztgeo.general.entity.service_data.interface_response.business.InquiryInformation;
import com.ztgeo.general.entity.service_data.interface_response.business.InquiryServiceData;
import com.ztgeo.general.entity.service_data.interface_response.business.information.ImmovableMortgageInquiryInformation;
import com.ztgeo.general.entity.service_data.interface_response.business.information.ImmovableObjectionInquiryInformation;
import com.ztgeo.general.entity.service_data.interface_response.business.information.ImmovableRightInquiryInformation;
import com.ztgeo.general.entity.service_data.interface_response.business.information.ImmovableSequestrationInquiryInformation;
import com.ztgeo.general.entity.service_data.json_data.JSONBdcdyxgxx;
import com.ztgeo.general.entity.service_data.json_data.JSONReceiptData;
import com.ztgeo.general.entity.service_data.json_data.JSONServiceData;
import com.ztgeo.general.entity.service_data.pub_data.*;
import com.ztgeo.general.entity.service_data.sys_data.MyTask;
import com.ztgeo.general.mapper.TestMapper;
import com.ztgeo.general.rest.test_entity.Diff;
import com.ztgeo.general.rest.test_entity.EntIdx;
import com.ztgeo.general.rest.test_entity.GSCommonCheckEntity;
import com.ztgeo.general.util.chenbin.Base64Util;
import com.ztgeo.general.util.chenbin.QRCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskInfo;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Scope("prototype")
@RequestMapping("api/test")
public class TestRest {
    @Value("${receipt.qrImgUrl}")
    private String qrImgUrl;
    @Autowired
    private StepManagerComponent stepManagerComponent;
    @Autowired
    private ReceiptNumberComponent receiptNumberComponent;
    @Autowired
    private RecieveDataBusinessBiz recDataBiz;
    @Autowired
    private ServiceAuthorizationComponent svrAuthComponent;
    @Autowired
    private TestMapper testMapper;

    @RequestMapping(value = "/test",method = RequestMethod.POST)
    public ObjectRestResponse<Object> test(@RequestParam("list1") String list1,@RequestParam("list2")String list2){
        ObjectRestResponse<Object> r = new ObjectRestResponse<Object>();
        List<SJ_Act_Step> steps = JSONArray.parseArray(list1,SJ_Act_Step.class);
        List<SJ_Act_Step_Gl> gls = JSONArray.parseArray(list2,SJ_Act_Step_Gl.class);
        return r.data(stepManagerComponent.addAndMergeSteps(steps,gls));
    }

    @RequestMapping(value = "/testRemove",method = RequestMethod.POST)
    public ObjectRestResponse<String> testRemove(@RequestParam("processId")String processId){
        ObjectRestResponse<Object> r = new ObjectRestResponse<Object>();
        return r.data(stepManagerComponent.removeProcessSteps(processId));
    }
    @RequestMapping(value = "/testSelect",method = RequestMethod.POST)
    public ObjectRestResponse<SJ_Act_Step> testSelect(@RequestParam("stepMouldId")String stepMouldId){
        return stepManagerComponent.getStepSettings(stepMouldId);
    }
    @RequestMapping(value = "/testGetRecNumb",method = RequestMethod.GET)
    public ObjectRestResponse<String> testGetRecNumb(){
        return new ObjectRestResponse<String>().data(receiptNumberComponent.getNextReceiptNumber());
    }

    @RequestMapping(value = "/testInterface",method = RequestMethod.POST)
    public JSONReceiptData testInterface(@RequestBody Map<String,Object> params){
        String receiptNumber = (String)params.get("receiptNumber");
        String serviceCode = (String)params.get("serviceCode");
        String serviceDataTo = (String)params.get("serviceDataTo");
        SJ_Sjsq sjsq = recDataBiz.findSjsqServiceDataByCode(receiptNumber,serviceCode,serviceDataTo);

        List<Sj_Info_Bdcdyxgxx> dyxgxxs = sjsq.getImmovableCurrentMortgageInfoVoList();
        for(Sj_Info_Bdcdyxgxx dyxgxx:dyxgxxs){
            dyxgxx.setProvideUnit("BDC");
        }
        List<JSONServiceData> serviceDatas = new ArrayList<JSONServiceData>();
        JSONServiceData serviceDate = new JSONServiceData();
        serviceDate.setServiceCode(serviceCode);
        serviceDate.setServiceDataTo(serviceDataTo);
        serviceDate.setServiceDataInfos(JSONArray.toJSONString(dyxgxxs));
        serviceDatas.add(serviceDate);
        JSONReceiptData receiptData = new JSONReceiptData();
        receiptData.setServiceDatas(JSONArray.toJSONString(serviceDatas));
        return receiptData;
    }

    @RequestMapping(value = "/tetst" ,method = RequestMethod.POST)
    public String dododod(@RequestParam("slbh") String slbh){
        try {
            QRCodeUtil.encode(slbh, null, qrImgUrl, slbh, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    @RequestMapping(value = "/test100" ,method = RequestMethod.POST)
    public String test100(@RequestParam("slbh") String slbh){
        String img = "";
        try {
            String path = qrImgUrl + slbh + ".jpg";
            img = Base64Util.getBase64Img(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }
    @RequestMapping(value = "/test101" ,method = RequestMethod.POST)
    public ObjectRestResponse<MyTask> test101(@RequestParam("taskId") String taskId){
        MyTask task = svrAuthComponent.getTask(taskId);
        System.out.println(task.getAssignee());
        System.out.println(task.getHistory());
        System.out.println(task.getDueDate());
        return new ObjectRestResponse<MyTask>().data(task);
    }

    @RequestMapping(value = "/test102" ,method = RequestMethod.GET)
    public ObjectRestResponse<Object> test102(@RequestParam("infoId") String infoId){
        try {
            testMapper.selectTest(infoId);
        }catch (MyBatisSystemException e){
            System.err.println(e.getClass());
            e.printStackTrace();
        }
        return new ObjectRestResponse<Object>();
    }

    @RequestMapping(value = "/testOuterService", method = RequestMethod.POST)
    public ObjectRestResponse<Object> testOuterService(@RequestBody Map<String,String> map){
        ObjectRestResponse<Object> rv= new ObjectRestResponse<Object>();
        List<InquiryInformation> ins = new ArrayList<InquiryInformation>();
        System.out.println("请求接入正常！");
        System.out.println("本次请求的参数为：");
        System.err.println(JSONObject.toJSONString(map));

        List<SJ_Qlr_Info> qlrs = new ArrayList<SJ_Qlr_Info>();
        List<SJ_Qlr_Info> dyqrs = new ArrayList<SJ_Qlr_Info>();
        List<SJ_Qlr_Info> cfrs = new ArrayList<SJ_Qlr_Info>();
        List<SJ_Qlr_Info> yyrs = new ArrayList<SJ_Qlr_Info>();

        SJ_Qlr_Info qlr1 = new SJ_Qlr_Info("张三","身份证","320323199207133336");
        SJ_Qlr_Info qlr2 = new SJ_Qlr_Info("李四","身份证","320323199207133356");
        SJ_Qlr_Info dyqr = new SJ_Qlr_Info("泉山区测试第一银行","统一社会信用代码","98564621");
        SJ_Qlr_Info cfr = new SJ_Qlr_Info("泉山区测试第一人民法院","统一社会信用代码","97564212");
        SJ_Qlr_Info yyr = new SJ_Qlr_Info("王五","身份证","320323199207133376");
        qlrs.add(qlr1);
        qlrs.add(qlr2);
        dyqrs.add(dyqr);
        cfrs.add(cfr);
        yyrs.add(yyr);

        ImmovableRightInquiryInformation right = new ImmovableRightInquiryInformation();
        right.setInformationType("QL");
        right.setAcceptanceNumber("202907010001");
        right.setImmovableUnicode("320301011111GB12211111111");
        right.setImmovableSite("徐州市泉山区测试街道测试地址第0001号");
        right.setImmovableCertificateNo("苏(2029)徐州市不动产权证测试第0000001号");
        right.setCertificateType("房屋不动产证");
        right.setRegistrationDate("2029-07-03 10:36:42");
        right.setObligeeVoList(qlrs);

        ImmovableMortgageInquiryInformation mortgage = new ImmovableMortgageInquiryInformation();
        mortgage.setInformationType("DY");
        mortgage.setAcceptanceNumber("202907100001");
        mortgage.setImmovableUnicode("320301011111GB12211111111");
        mortgage.setImmovableSite("徐州市泉山区测试街道测试地址第0001号");
        mortgage.setRelevantCertificateNo("苏(2029)徐州市不动产权证测试第0000001号");
        mortgage.setMortgageCertificateNo("苏(2029)徐州市不动产证明测试第0000001号");
        mortgage.setRegistrationDate("2029-07-11 10:36:42");
        mortgage.setMortgagorVoList(qlrs);
        mortgage.setMortgageHolderVoList(dyqrs);

        ImmovableSequestrationInquiryInformation sequestrat = new ImmovableSequestrationInquiryInformation();
        sequestrat.setInformationType("CF");
        sequestrat.setAcceptanceNumber("202908100001");
        sequestrat.setImmovableUnicode("320301011111GB12211111111");
        sequestrat.setImmovableSite("徐州市泉山区测试街道测试地址第0001号");
        sequestrat.setRelevantCertificateNo("苏(2029)徐州市不动产权证测试第0000001号");
        sequestrat.setSequestrationCertificateNo("苏(2029)徐州市不动产证明测试第0000002号");
        sequestrat.setRegistrationDate("2029-08-11 10:36:42");
        sequestrat.setObligeeVoList(qlrs);
        sequestrat.setSealUpPersonVoList(cfrs);

        ImmovableObjectionInquiryInformation objection = new ImmovableObjectionInquiryInformation();
        objection.setInformationType("YY");
        objection.setAcceptanceNumber("202907150001");
        objection.setImmovableUnicode("320301011111GB12211111111");
        objection.setImmovableSite("徐州市泉山区测试街道测试地址第0001号");
        objection.setRelevantCertificateNo("苏(2029)徐州市不动产权证测试第0000001号");
        objection.setObjectionCertificateNo("苏(2029)徐州市不动产证明测试第0000003号");
        objection.setRegistrationDate("2029-07-15 10:36:42");
        objection.setObligeeVoList(qlrs);
        objection.setDissidentVoList(yyrs);

        ins.add(right);
        ins.add(mortgage);
        ins.add(sequestrat);
        ins.add(objection);

        System.out.println("请求结束！");
        return rv.data(ins);
    }

    @RequestMapping(value = "share/rkkjzxxcx",method = RequestMethod.POST)
    public Object rkkjzxxcx111(@RequestBody Map<String,Object> map, HttpServletRequest request){
        System.out.println("headers:    api_id:"+request.getHeader("api_id")+"   from_user:"+request.getHeader("from_user"));
        System.out.println("body:   "+JSONObject.toJSONString(map));
        Map<String,Object> rv = new HashMap<String,Object>();
        Map<String,String> r = new HashMap<String,String>();
        List<Map<String,String>> rl = new ArrayList<Map<String,String>>();
        r.put("gmsfzh","测试身份证号");
        r.put("gmsfzh_ppddm","1");
        r.put("xm","测试姓名");
        r.put("xm_ppddm","1");
        r.put("csd_gjdqm","出生地_国家");
        r.put("csd_ssxdqm","出生地_地区");
        r.put("csrq","出生日期");
        r.put("mzdm","民族代码");
        r.put("swbs","死亡标识");
        r.put("xbdm","性别代码");
        rl.add(r);
        rv.put("status","0");
        rv.put("msg","查询成功");
        rv.put("data",rl);
        return rv;
    }

    @RequestMapping(value = "share/rkkrxbd",method = RequestMethod.POST)
    public Object rkkrxbd(@RequestBody Map<String,Object> map, HttpServletRequest request){
        System.out.println("headers:    api_id:"+request.getHeader("api_id")+"   from_user:"+request.getHeader("from_user"));
        System.out.println("body:   "+JSONObject.toJSONString(map));
        Map<String,Object> rv = new HashMap<String,Object>();
        Map<String,String> r = new HashMap<String,String>();
        r.put("gmsfzh","320682198704277154");
        r.put("gmsfzh_ppddm","1");
        r.put("xm","姜志伟");
        r.put("xm_ppddm","1");
        r.put("swbs","0");
        r.put("bddm","0");
        r.put("xsd","0");
        List<Map<String,String>> rl = new ArrayList<Map<String,String>>();
        rv.put("status","0");
        rv.put("msg","查询成功");
        rl.add(r);
        rv.put("data",rl);
        return rv;
    }

    @RequestMapping(value = "share/qyjbxxyz")
    public Object qyjbxxyz(@RequestBody Map<String,Object> map, HttpServletRequest request){
        GSCommonCheckEntity rv = new GSCommonCheckEntity();
        EntIdx ent_idx = new EntIdx();
        List<Diff> diff_list = new ArrayList<Diff>();
        ent_idx.setEntname("测试公司");
        ent_idx.setUniscid("9113215111TY");
        rv.setEnt_idx(ent_idx);
        rv.set_map_("true");
        rv.setEntchk_checkres_key("04");
        rv.setRegstateCn("存续（在营、开业、在册）");
        Diff diff = new Diff();
        diff.setChk_key("regno");
        diff.setIn_val("dfdf");
        diff.setOut_val("320582000093337");
        diff_list.add(diff);
        rv.setDiff_list(diff_list);
        Map<String,Object> r = new HashMap<String,Object>();
        r.put("status","0");
        r.put("msg","查询成功");
        r.put("data",rv);
        return r;
    }

}
