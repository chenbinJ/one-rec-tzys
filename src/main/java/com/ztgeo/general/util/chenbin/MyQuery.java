package com.ztgeo.general.util.chenbin;

import java.util.LinkedHashMap;
import java.util.Map;

public class MyQuery extends LinkedHashMap<String, Object> {
        private static final long serialVersionUID = 1L;
        //当前页码
        private int page = 1;
        //每页条数
        private int limit = 10;
        //查询条件
        private String searchParam = "";

        public MyQuery(Map<String, Object> params){
            this.putAll(params);
            //分页参数
            if(params.get("page")!=null) {
                this.page = Integer.parseInt(params.get("page").toString());
            }
            if(params.get("limit")!=null) {
                this.limit = Integer.parseInt(params.get("limit").toString());
            }
            if(params.get("searchParam")!=null){
                this.searchParam = (String) params.get("searchParam");
            }
            this.remove("page");
            this.remove("limit");
            this.remove("searchParam");
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

    public String getSearchParam() {
        return searchParam;
    }

    public void setSearchParam(String searchParam) {
        this.searchParam = searchParam;
    }
}
