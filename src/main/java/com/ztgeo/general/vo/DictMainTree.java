package com.ztgeo.general.vo;

import com.github.wxiaoqi.security.common.vo.TreeNodeVO;

/**
 * Create by Wei on 2018/5/16
 */
public class DictMainTree extends TreeNodeVO<DictMainTree> {
        //父类已经拥有的字段有 1. id-->diccode 2. parentid -->pid 3. chliden-->DictMainTree
        //需要返回给前台增加的字段
    private String label; //字典名称（目录名称）
    private String dictype;//字典的类型
    private String tablename; //寻找表的具体名称
    private String dicnote;//字典描述
    public DictMainTree(Object id, Object parentId, String label, String dictype, String tablename, String dicnote) {
        this.label = label;
        this.setId(id);
        this.setParentId(parentId);
        this.dictype = dictype;
        this.tablename = tablename;
        this.dicnote = dicnote;
    }

    public String getDicnote() {
        return dicnote;
    }

    public void setDicnote(String dicnote) {
        this.dicnote = dicnote;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDictype() {
        return dictype;
    }

    public void setDictype(String dictype) {
        this.dictype = dictype;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }
}
