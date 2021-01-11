package com.net.system.model;

import java.util.Date;

public class WriteOff {
    private String id;

    /**
     * 流水号
     */
    private String reqid;

    /**
     * 用户id
     */
    private Integer userid;

    /**
     * 景点编号
     */
    private String rightcode;

    /**
     * 景点名称
     */
    private String rightname;

    /**
     * 核销时间
     */
    private Date checktime;

    /**
     * 核销方式
     */
    private String checktype;

    /**
     * 机具类型
     */
    private String machinetype;

    /**
     * 入园方式
     */
    private String vouchertype;

    /**
     * 核销记录编号
     */
    private String checkno;

    /**
     * 渠道号
     */
    private String channelid;
    
    private String orderNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReqid() {
        return reqid;
    }

    public void setReqid(String reqid) {
        this.reqid = reqid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getRightcode() {
        return rightcode;
    }

    public void setRightcode(String rightcode) {
        this.rightcode = rightcode;
    }

    public String getRightname() {
        return rightname;
    }

    public void setRightname(String rightname) {
        this.rightname = rightname;
    }

    public Date getChecktime() {
        return checktime;
    }

    public void setChecktime(Date checktime) {
        this.checktime = checktime;
    }

    public String getChecktype() {
        return checktype;
    }

    public void setChecktype(String checktype) {
        this.checktype = checktype;
    }

    public String getMachinetype() {
        return machinetype;
    }

    public void setMachinetype(String machinetype) {
        this.machinetype = machinetype;
    }

    public String getVouchertype() {
        return vouchertype;
    }

    public void setVouchertype(String vouchertype) {
        this.vouchertype = vouchertype;
    }

    public String getCheckno() {
        return checkno;
    }

    public void setCheckno(String checkno) {
        this.checkno = checkno;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
    
    
}