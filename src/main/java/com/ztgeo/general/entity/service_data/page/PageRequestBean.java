package com.ztgeo.general.entity.service_data.page;

import java.io.Serializable;

public class PageRequestBean implements Serializable {
    //当前页
    private int pageNum=1;
    //页面记录条数
    private int pageSize=20;
    //最大页数
    private int pages=1;
    //total属性
    private long total;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
