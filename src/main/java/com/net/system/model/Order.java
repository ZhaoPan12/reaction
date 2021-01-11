package com.net.system.model;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Order {
	
    /**
     * 编号
     */
    private String id;

    /**
     * 订单号
     */
    private String serialNumber;

    /**
     * 用户编号
     */
    private Integer memberId;

    /**
     * 运费
     */
    private BigDecimal freight;

    /**
     * 发货时间
     */
    private Date deliveryTime;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 商家id
     */
    private String businessId;

    /**
     * 订单总金额
     */
    private BigDecimal total;

    /**
     * 订单实付金额
     */
    private BigDecimal actual;

    /**
     * 订单添加时间
     */
    private Date addTime;

    /**
     * 付款类型
     */
    private Integer paymentType;

    /**
     * 交易编号
     */
    private String transactionId;

    /**
     * 支付通知结果
     */
    private String notifyResult;

    /**
     * 付款时间
     */
    private Date paymentTime;

    /**
     * 订单结束时间
     */
    private Date endTime;

    /**
     * 订单更新时间
     */
    private Date updateTime;

    /**
     * 订单关闭/取消时间
     */
    private Date closeTime;

    /**
     * 支付状态 1、待支付 2、已支付
     */
    private Integer paymentStatus;

    /**
     * 父订单号，用于支付回调时验证
     */
    private String superSerialNumber;

    /**
     * 用户角色
     */
    private String memberRole;

    /**
     * 买家留言
     */
    private String buyerMessage;

    /**
     * 待支付订单付款截止时间默认3天期限
     */
    private Date paymentEndTime;

    /**
     * 上门取件提取码
     */
    private String extractionCode;

    /**
     * 订单类型 1、快递寄送 2、上门取件 3、优惠劵
     */
    private Integer orderType;

    /**
     * 物流单号
     */
    private String logisticsNumber;

    /**
     * 商家联系电话
     */
    private String businessPhone;

    /**
     * 用户选择的自提时间
     */
    private Date extractTime;

    /**
     * 自提电话号码
     */
    private String extractPhone;

    /**
     * 优惠劵
     */
    private String couponId;

    /**
     * 订单优惠
     */
    private BigDecimal discount;

    /**
     * 活动id
     */
    private String activityId;

    /**
     * 订单类型
     */
    private Integer activityType;

    /**
     * 售后状态\r\n0、正常\r\n1、待审核\r\n2、审核通过待退款\r\n3、退款成功\r\n4、驳回\r\n5、取消退款
     */
    private Integer refundStatus;

    /**
     * 上门取货地址
     */
    private String obtainAddress;

    private String logisticsName;
    
    private String businessMessage;
    
    public String getBusinessMessage() {
		return businessMessage;
	}


	public void setBusinessMessage(String businessMessage) {
		this.businessMessage = businessMessage;
	}



    private List<OrderProduct> productList;
    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public Date getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getActual() {
        return actual;
    }

    public void setActual(BigDecimal actual) {
        this.actual = actual;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getNotifyResult() {
        return notifyResult;
    }

    public void setNotifyResult(String notifyResult) {
        this.notifyResult = notifyResult;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Date closeTime) {
        this.closeTime = closeTime;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getSuperSerialNumber() {
        return superSerialNumber;
    }

    public void setSuperSerialNumber(String superSerialNumber) {
        this.superSerialNumber = superSerialNumber;
    }

    public String getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(String memberRole) {
        this.memberRole = memberRole;
    }

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public Date getPaymentEndTime() {
        return paymentEndTime;
    }

    public void setPaymentEndTime(Date paymentEndTime) {
        this.paymentEndTime = paymentEndTime;
    }

    public String getExtractionCode() {
        return extractionCode;
    }

    public void setExtractionCode(String extractionCode) {
        this.extractionCode = extractionCode;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public Date getExtractTime() {
        return extractTime;
    }

    public void setExtractTime(Date extractTime) {
        this.extractTime = extractTime;
    }

    public String getExtractPhone() {
        return extractPhone;
    }

    public void setExtractPhone(String extractPhone) {
        this.extractPhone = extractPhone;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public Integer getActivityType() {
        return activityType;
    }

    public void setActivityType(Integer activityType) {
        this.activityType = activityType;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getObtainAddress() {
        return obtainAddress;
    }

    public void setObtainAddress(String obtainAddress) {
        this.obtainAddress = obtainAddress;
    }

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

	public List<OrderProduct> getProductList() {
		return productList;
	}

	public void setProductList(List<OrderProduct> productList) {
		this.productList = productList;
	}
    
}