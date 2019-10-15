package com.ztgeo.general.util.chenbin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ztgeo.general.entity.service_data.json_data.JSONReceiptData;
import com.ztgeo.general.entity.service_data.pub_data.SJ_Sjsq;
import com.ztgeo.general.entity.service_data.resp_data.RespServiceData;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaBeanUtil {
    public static Map<String,Object> convertBeanToMap(Object bean) throws IntrospectionException,IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map<String,Object> returnMap = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }

    public static Map<String,String> convertSJSQJSONToStringMap(String sjsq_json){
        JSONReceiptData sjsq = JSONObject.parseObject(sjsq_json,JSONReceiptData.class);
        return MyMapUtil.objToStringMap(sjsq);
    }
}
