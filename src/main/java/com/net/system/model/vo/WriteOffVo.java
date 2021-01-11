package com.net.system.model.vo;

import java.util.Date;

public class WriteOffVo {

	 /**
     * 景点编号
     */
    private String rightDtlCode;

    /**
     * 景点名称
     */
    private String rightDtlName;

    /**
     * 核销时间
     */
    private String checkTime;

    /**
     * 核销方式
     */
    private String checkType;

    /**
     * 机具类型
     */
    private String machineType;

    /**
     * 入园方式
     */
    private String voucherType;

    /**
     * 核销记录编号
     */
    private String checkNo;
    
    private String orderNo;


	public String getRightDtlCode() {
		return rightDtlCode;
	}

	public void setRightDtlCode(String rightDtlCode) {
		this.rightDtlCode = rightDtlCode;
	}

	public String getRightDtlName() {
		return rightDtlName;
	}

	public void setRightDtlName(String rightDtlName) {
		this.rightDtlName = rightDtlName;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getMachineType() {
		return machineType;
	}

	public void setMachineType(String machineType) {
		this.machineType = machineType;
	}

	public String getVoucherType() {
		return voucherType;
	}

	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}

	public String getCheckNo() {
		return checkNo;
	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


    
    
}
