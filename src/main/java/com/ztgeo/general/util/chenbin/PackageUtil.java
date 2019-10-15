package com.ztgeo.general.util.chenbin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PackageUtil {
    public static void getClasses(String packageName,String searchSign,List<Map<String,String>> classList) throws IOException, ClassNotFoundException {
        Enumeration<URL> iterator = Thread.currentThread().getContextClassLoader().getResources(packageName.replace(".","/"));
        URL url = null;
        File file = null;
        File[] files = null;
        Class<?> c = null;
        String className = null;
        while(iterator.hasMoreElements()){
            url = iterator.nextElement();
            if("file".equals(url.getProtocol())){
                file = new File(URLDecoder.decode(url.getPath(),"UTF-8"));
                if(file.isDirectory()){
                    files = file.listFiles();
                    for(File f:files){
                        className = f.getName();
                        if(f.isDirectory()) {
                            getClasses(packageName+"."+f.getName(),searchSign,classList);
                        }else {
                            className = className.substring(0,className.lastIndexOf("."));
                            if(searchSign!=null&&!className.toUpperCase().contains(searchSign.toUpperCase())) {
                                continue;
                            }
                            c = Thread.currentThread().getContextClassLoader().loadClass(packageName+"."+className);
                            Map<String,String> map = new HashMap<String,String>();
                            map.put("className",className);
                            map.put("allClassName",c.getName());
                            //chinese name 选择
                            classList.add(map);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取属性名数组
     * @throws ClassNotFoundException
     * */
    public static List<Map<String,String>> getFiledName(String className) throws ClassNotFoundException{
        Field[] fields=Class.forName(className).getDeclaredFields();
        List<Map<String,String>> objs = new ArrayList<Map<String,String>>();
        for(int i=0;i<fields.length;i++){
            Map<String,String> obj = new HashMap<String,String>();
            String fieldName=fields[i].getName();
            String type = "String";
            if(fieldName.contains("Time")||fieldName.contains("time")||fieldName.contains("Date")||fieldName.contains("date")){
                type = "Date";
            }
            obj.put("fieldName",fieldName);
            obj.put("type",type);
            //type = fields[i].getType().getName();
            objs.add(obj);
        }
        return objs;
    }

    /**
     * 根据属性名获取对象中的属性值
     * */
    @SuppressWarnings("unused")
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {
            return null;
        }
    }
}
