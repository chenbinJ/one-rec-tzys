package com.ztgeo.general.util.chenbin;

import com.ztgeo.general.exception.chenbin.ZtgeoBizException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyMapUtil {
    public static Map<String,String> ValueArrayToValue(Map<String,String[]> map_p){
        Map<String,String> map_v = new HashMap<String,String>();
        Set<String> set_p =  map_p.keySet();
        for(String p:set_p){
            String[] v_array = map_p.get(p);
            String v = "";
            for(String v_:v_array){
                v = v+v_+"$";
            }
            if(v.lastIndexOf("$")>0){
                v = v.substring(0,v.lastIndexOf("$"));
            }
            map_v.put(p,v);
        }
        return map_v;
    }

    /**
     * 将Object对象里面的属性和值转化成Map对象
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> objToMap(Object obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();	// 获取f对象对应类中的所有属性域
        for (int i = 0, len = fields.length; i < len; i++) {
            String varName = fields[i].getName();
            varName = varName.toLowerCase();					// 将key置为小写，默认为对象的属性
            try {
                boolean accessFlag = fields[i].isAccessible();	// 获取原来的访问控制权限
                fields[i].setAccessible(true);					// 修改访问控制权限
                Object o = fields[i].get(obj);					// 获取在对象f中属性fields[i]对应的对象中的变量
                if (o != null){
                    map.put(varName, o.toString());
                }
                fields[i].setAccessible(accessFlag);			// 恢复访问控制权限
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
                //throw new ZtgeoBizException("对象转MAP异常1，程序执行失败");
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
                //throw new ZtgeoBizException("对象转MAP异常2，程序执行失败");
            }
        }
        return map;
    }

    public static Map<String, String> objToStringMap(Object obj) {
        Map<String, String> mapStr = new HashMap<String, String>();
        Map<String, Object> mapObj = objToMap(obj);
        for (String key:mapObj.keySet()){
            mapStr.put(key,(String) mapObj.get(key));
        }
        return mapStr;
    }
}
