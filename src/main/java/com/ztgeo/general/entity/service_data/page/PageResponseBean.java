package com.ztgeo.general.entity.service_data.page;

import com.github.pagehelper.Page;

import java.io.Serializable;

public class PageResponseBean<T> implements Serializable {
    private Page<T> pageArray;
    private int limit;
    private int page;
    private long total;

    public PageResponseBean() {
        super();
    }

    public PageResponseBean(Page<T> pageArray, int limit, int page, long total) {
        super();
        this.pageArray = pageArray;
        this.limit = limit;
        this.page = page;
        this.total = total;
    }

    public Page<T> getPageArray() {
        return pageArray;
    }

    public void setPageArray(Page<T> pageArray) {
        this.pageArray = pageArray;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
