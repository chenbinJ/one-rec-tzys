package com.ztgeo.general.controller;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.ztgeo.general.biz.DictValueBiz;
import com.ztgeo.general.entity.DictValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/dictValue")

public class DictValueController extends BaseController<DictValueBiz, DictValue> {

    @RequestMapping(value = "/type/{code}",method = RequestMethod.GET)
    public TableResultResponse<DictValue> getDictValueByDictTypeCode(@PathVariable("code") String code){
        Example example = new Example(DictValue.class);
        example.createCriteria().andLike("code",code+"%");
        List<DictValue> dictValues = this.baseBiz.selectByExample(example).stream().sorted(new Comparator<DictValue>() {
            @Override
            public int compare(DictValue o1, DictValue o2) {
                return o1.getOrderNum() - o2.getOrderNum();
            }
        }).collect(Collectors.toList());
        return new TableResultResponse<DictValue>(dictValues.size(),dictValues);
    }

    /*
    * like 获取 字典的值
    * */
    @RequestMapping(value = "/feign/{code}",method = RequestMethod.GET)
    public Map<String,String> getDictValueByCode(@PathVariable("code") String code){
        Example example = new Example(DictValue.class);
        example.createCriteria().andLike("code",code+"%");
        List<DictValue> dictValues = this.baseBiz.selectByExample(example);
        Map<String, String> result = dictValues.stream().collect(
                Collectors.toMap(DictValue::getValue, DictValue::getLabelDefault));
        return result;
    }
}