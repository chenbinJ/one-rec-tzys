package com.ztgeo.general.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * 字典-字典主项表
 * 
 * @author wei
 * @email 1205690873@qq.com
 * @version 2018-05-15 10:36:04
 */
@Table(name = "dic_main")
public class DicMain implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //字典编码
    @Id
    private String diccode;
	
	    //字典名称
    @Column(name = "DICNAME")
    private String dicname;
	
	    //表名
    @Column(name = "TABLENAME")
    private String tablename;
	
	    //字典类别
    @Column(name = "DICTYPE")
    private String dictype;
	
	    //所属系统
    @Column(name = "SYSNAME")
    private String sysname;
	
	    //字典状态
    @Column(name = "DICSTATE")
    private String dicstate;
	
	    //描述
    @Column(name = "DICNOTE")
    private String dicnote;
	
	    //SID
    @Column(name = "SID")
    private String sid;
	
	    //PID
    @Column(name = "PID")
    private String pid;
	

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
	 * 设置：字典名称
	 */
	public void setDicname(String dicname) {
		this.dicname = dicname;
	}
	/**
	 * 获取：字典名称
	 */
	public String getDicname() {
		return dicname;
	}
	/**
	 * 设置：表名
	 */
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	/**
	 * 获取：表名
	 */
	public String getTablename() {
		return tablename;
	}
	/**
	 * 设置：字典类别
	 */
	public void setDictype(String dictype) {
		this.dictype = dictype;
	}
	/**
	 * 获取：字典类别
	 */
	public String getDictype() {
		return dictype;
	}
	/**
	 * 设置：所属系统
	 */
	public void setSysname(String sysname) {
		this.sysname = sysname;
	}
	/**
	 * 获取：所属系统
	 */
	public String getSysname() {
		return sysname;
	}
	/**
	 * 设置：字典状态
	 */
	public void setDicstate(String dicstate) {
		this.dicstate = dicstate;
	}
	/**
	 * 获取：字典状态
	 */
	public String getDicstate() {
		return dicstate;
	}
	/**
	 * 设置：描述
	 */
	public void setDicnote(String dicnote) {
		this.dicnote = dicnote;
	}
	/**
	 * 获取：描述
	 */
	public String getDicnote() {
		return dicnote;
	}
	/**
	 * 设置：SID
	 */
	public void setSid(String sid) {
		this.sid = sid;
	}
	/**
	 * 获取：SID
	 */
	public String getSid() {
		return sid;
	}
	/**
	 * 设置：PID
	 */
	public void setPid(String pid) {
		this.pid = pid;
	}
	/**
	 * 获取：PID
	 */
	public String getPid() {
		return pid;
	}
}
