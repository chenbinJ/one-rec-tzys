package com.ztgeo.general.controller.chenbin.business;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.ztgeo.general.biz.service_biz.chenbin.WorkManagerBiz;
import com.ztgeo.general.biz.service_biz.chenbin.impl.ExceptionRecordBiz;
import com.ztgeo.general.constant.chenbin.BizOrBizExceptionConstant;
import com.ztgeo.general.entity.service_data.page.PageResponseBean;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Exception_Record;
import com.ztgeo.general.entity.service_data.sys_data.ExampleRespBody;
import com.ztgeo.general.entity.service_data.sys_data.ExampleSearchParams;
import com.ztgeo.general.entity.service_data.sys_data.ExceptionSearchParams;
import com.ztgeo.general.exception.chenbin.ZtgeoBizException;
import com.ztgeo.general.util.chenbin.ErrorDealUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/biz/ExampleHandle")
@Api(tags = {"实例管理类API，包括" +
        "1.管理员查看全部实例，搜索部分实例，分页显示实例，操作实例，删除实例等。" +
        "2.各办件参与者查看所参与的全部实例，搜索所参与的全部实例，分页显示结果列表以及点击查看实例办件详情"+
        "3.实例执行过程中产生的和其它系统同步失败的情况处理"
})
public class WorkManagerController {
    @Autowired
    private WorkManagerBiz workManager;
    @Autowired
    private ExceptionRecordBiz exceptionRecordBiz;

    @RequestMapping(value = "/search" , method = RequestMethod.POST)
    public ObjectRestResponse<PageResponseBean<ExampleRespBody>> searchExample(@RequestBody ExampleSearchParams params){
        return new ObjectRestResponse<PageResponseBean<ExampleRespBody>>().data(workManager.searchExample(params));
    }

    @RequestMapping(value = "/handleSubException" , method = RequestMethod.GET)
    public ObjectRestResponse<String> handleSubExceptionByManager(@RequestParam("excId") String excId){
        try {
            workManager.handleSubExceptionByManager(excId);
        } catch (Exception e){
            e.printStackTrace();
            log.error(ErrorDealUtil.getErrorInfo(e));
            workManager.handleExceptionFaild(excId);
            throw new ZtgeoBizException(BizOrBizExceptionConstant.EXCEPTION_DEAL_FAILD);
        }
        return new ObjectRestResponse<String>().data("操作成功");
    }

    @RequestMapping(value = "/subException/page" , method = RequestMethod.POST)
    public ObjectRestResponse<PageResponseBean<SJ_Exception_Record>> page(@RequestBody ExceptionSearchParams params){
        return new ObjectRestResponse<PageResponseBean<SJ_Exception_Record>>().data(workManager.searchException(params));
    }

    @RequestMapping(value = "/subException/findById" , method = RequestMethod.GET)
    public ObjectRestResponse<SJ_Exception_Record> findById(@RequestParam("id") String id){
        return new ObjectRestResponse<SJ_Exception_Record>().data(exceptionRecordBiz.selectById(id));
    }

    @RequestMapping(value = "/subException/deleteById" , method = RequestMethod.GET)
    public ObjectRestResponse<String> deleteById(@RequestParam("id") String id){
        exceptionRecordBiz.deleteById(id);
        return new ObjectRestResponse<String>().data("删除成功");
    }

    @RequestMapping(value = "handleAotuInterface" , method = RequestMethod.GET)
    public ObjectRestResponse<String> handleAotuInterface(@RequestParam("taskId") String taskId){
        return new ObjectRestResponse<String>().data(workManager.handleAutoInterfaceByManager(taskId));
    }
}
