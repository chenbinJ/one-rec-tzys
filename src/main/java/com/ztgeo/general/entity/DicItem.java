package com.ztgeo.general.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 字典-字典字项表
 * 
 * @author wei
 * @email 1205690873@qq.com
 * @version 2018-05-15 10:36:04
 */
@Table(name = "dic_item")
public class DicItem implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //字项编码
    @Id
    private String itemid;
	
	    //字典编码
    @Column(name = "DICCODE")
    private String diccode;
	
	    //显示名称
    @Column(name = "ITEMNAME")
    private String itemname;
	
	    //实际值
    @Column(name = "ITEMVAL")
    private String itemval;
	
	    //子项描述
    @Column(name = "ITEMNOTE")
    private String itemnote;
	
	    //序号
    @Column(name = "ITEMORDER")
    private BigDecimal itemorder;
	

	/**
	 * 设置：字项编码
	 */
	public void setItemid(String itemid) {
		this.itemid = itemid;
	}
	/**
	 * 获取：字项编码
	 */
	public String getItemid() {
		return itemid;
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
	 * 设置：显示名称
	 */
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	/**
	 * 获取：显示名称
	 */
	public String getItemname() {
		return itemname;
	}
	/**
	 * 设置：实际值
	 */
	public void setItemval(String itemval) {
		this.itemval = itemval;
	}
	/**
	 * 获取：实际值
	 */
	public String getItemval() {
		return itemval;
	}
	/**
	 * 设置：子项描述
	 */
	public void setItemnote(String itemnote) {
		this.itemnote = itemnote;
	}
	/**
	 * 获取：子项描述
	 */
	public String getItemnote() {
		return itemnote;
	}
	/**
	 * 设置：序号
	 */
	public void setItemorder(BigDecimal itemorder) {
		this.itemorder = itemorder;
	}
	/**
	 * 获取：序号
	 */
	public BigDecimal getItemorder() {
		return itemorder;
	}
}
