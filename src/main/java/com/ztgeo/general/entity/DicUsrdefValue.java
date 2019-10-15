package com.ztgeo.general.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 自定义字典存储表
 * 
 * @author wei
 * @email 1205690873@qq.com
 * @version 2018-05-15 10:36:04
 */
@Table(name = "dic_usrdef_value")
public class DicUsrdefValue implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //编码
    @Id
    private String id;
	
	    //字典编码
    @Column(name = "DICCODE")
    private String diccode;
	
	    //父编码
    @Column(name = "PID")
    private String pid;
	
	    //序号
    @Column(name = "SORTNUM")
    private BigDecimal sortnum;
	
	    //值1
    @Column(name = "VALUE1")
    private String value1;
	
	    //值2
    @Column(name = "VALUE2")
    private String value2;
	
	    //值3
    @Column(name = "VALUE3")
    private String value3;
	
	    //值4
    @Column(name = "VALUE4")
    private String value4;
	
	    //值5
    @Column(name = "VALUE5")
    private String value5;
	
	    //值6
    @Column(name = "VALUE6")
    private String value6;
	
	    //值7
    @Column(name = "VALUE7")
    private String value7;
	
	    //值8
    @Column(name = "VALUE8")
    private String value8;
	
	    //值9
    @Column(name = "VALUE9")
    private String value9;
	
	    //值10
    @Column(name = "VALUE10")
    private String value10;
	

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
	 * 设置：父编码
	 */
	public void setPid(String pid) {
		this.pid = pid;
	}
	/**
	 * 获取：父编码
	 */
	public String getPid() {
		return pid;
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
	/**
	 * 设置：值1
	 */
	public void setValue1(String value1) {
		this.value1 = value1;
	}
	/**
	 * 获取：值1
	 */
	public String getValue1() {
		return value1;
	}
	/**
	 * 设置：值2
	 */
	public void setValue2(String value2) {
		this.value2 = value2;
	}
	/**
	 * 获取：值2
	 */
	public String getValue2() {
		return value2;
	}
	/**
	 * 设置：值3
	 */
	public void setValue3(String value3) {
		this.value3 = value3;
	}
	/**
	 * 获取：值3
	 */
	public String getValue3() {
		return value3;
	}
	/**
	 * 设置：值4
	 */
	public void setValue4(String value4) {
		this.value4 = value4;
	}
	/**
	 * 获取：值4
	 */
	public String getValue4() {
		return value4;
	}
	/**
	 * 设置：值5
	 */
	public void setValue5(String value5) {
		this.value5 = value5;
	}
	/**
	 * 获取：值5
	 */
	public String getValue5() {
		return value5;
	}
	/**
	 * 设置：值6
	 */
	public void setValue6(String value6) {
		this.value6 = value6;
	}
	/**
	 * 获取：值6
	 */
	public String getValue6() {
		return value6;
	}
	/**
	 * 设置：值7
	 */
	public void setValue7(String value7) {
		this.value7 = value7;
	}
	/**
	 * 获取：值7
	 */
	public String getValue7() {
		return value7;
	}
	/**
	 * 设置：值8
	 */
	public void setValue8(String value8) {
		this.value8 = value8;
	}
	/**
	 * 获取：值8
	 */
	public String getValue8() {
		return value8;
	}
	/**
	 * 设置：值9
	 */
	public void setValue9(String value9) {
		this.value9 = value9;
	}
	/**
	 * 获取：值9
	 */
	public String getValue9() {
		return value9;
	}
	/**
	 * 设置：值10
	 */
	public void setValue10(String value10) {
		this.value10 = value10;
	}
	/**
	 * 获取：值10
	 */
	public String getValue10() {
		return value10;
	}
}
