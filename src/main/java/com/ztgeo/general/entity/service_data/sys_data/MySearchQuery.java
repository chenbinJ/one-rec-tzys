package com.ztgeo.general.entity.service_data.sys_data;

import com.ztgeo.general.entity.service_data.page.PageRequestBean;
import com.ztgeo.general.util.chenbin.JavaBeanUtil;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;

/*
    个人分页查询类
 */
public class MySearchQuery<T extends PageRequestBean> extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    //当前页码
    private int page = 1;
    //每页条数
    private int limit = 10;
    //查询条件
    private T searchParam;

    public MySearchQuery(T t) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        if(t!=null){
            this.page = t.getPageNum();
            this.limit = t.getPageSize();
            searchParam = t;
            this.putAll(JavaBeanUtil.convertBeanToMap(t));
        }
    }
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public T getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(T searchParam) {
        this.searchParam = searchParam;
    }
}
