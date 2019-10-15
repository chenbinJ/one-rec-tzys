package com.ztgeo.general.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 自定义字典字段定义表
 * 
 * @author wei
 * @email 1205690873@qq.com
 * @version 2018-05-15 10:36:04
 */
@Table(name = "dic_usrdef_field")
public class DicUsrdefField implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //编码
    @Id
    private String id;
	
	    //字典编码
    @Column(name = "DICCODE")
    private String diccode;
	
	    //字段名称
    @Column(name = "FIELDNAME")
    private String fieldname;
	
	    //序号
    @Column(name = "SORTNUM")
    private BigDecimal sortnum;

    @Column(name = "COLUMNVAL")
    public String columnval;

    public String getColumnval() {
        return columnval;
    }

    public void setColumnval(String columnval) {
        this.columnval = columnval;
    }

    /**
	 * 设置：编码
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * 获取：编码
	 */
	public String getId() {
		return id;
	}
	/**
	 * 设置：字典编码
	 */
	public void setDiccode(String diccode) {
		this.diccode = diccode;
	}
	/**
	 * 获取：字典编码
	 */
	public String getDiccode() {
		return diccode;
	}
	/**
	 * 设置：字段名称
	 */
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}
	/**
	 * 获取：字段名称
	 */
	public String getFieldname() {
		return fieldname;
	}
	/**
	 * 设置：序号
	 */
	public void setSortnum(BigDecimal sortnum) {
		this.sortnum = sortnum;
	}
	/**
	 * 获取：序号
	 */
	public BigDecimal getSortnum() {
		return sortnum;
	}


}
