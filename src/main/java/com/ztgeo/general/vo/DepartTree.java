package com.ztgeo.general.vo;

import com.github.wxiaoqi.security.common.vo.TreeNodeVO;

/**
 * @author wei
 * @create 2018年12月25日21:13:57
 */
public class DepartTree extends TreeNodeVO<DepartTree> {
    String label;
    String code;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public DepartTree(){

    }
    public DepartTree(Object id, Object parentId, String label, String code) {
        this.label = label;
        this.code = code;
        this.setId(id);
        this.setParentId(parentId);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
