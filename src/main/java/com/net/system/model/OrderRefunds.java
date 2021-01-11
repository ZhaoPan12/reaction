package com.net.system.model;

import java.util.Date;

/**
 * 订单售后申请表
 * @author Administrator
 *
 */
public class OrderRefunds {
    /**
     * id
     */
    private String id;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 退款原因
     */
    private String refundReson;

    /**
     * 描述
     */
    private String describe;

    /**
     * 回复信息
     */
    private String reply;

    /**
     * 申请时间
     */
    private Date addTime;

    /**
     * 退款状态\r\n1、待审核\r\n2、审核通过\r\n3、驳回\r\n4、待退款\r\n5、已退款
     */
    private Integer status;

    /**
     * 退款时间
     */
    private Date refundTime;

    /**
     * 货物状态\r\n1、待发退款货\r\n2、已发退款货\r\n3、已收退款货
     */
    private Integer goodsStatus;

    /**
     * 申请类型\r\n0、退款无需退货\r\n1、退款退货
     */
    private Integer applyType;

    private Date updateTime;

    /**
     * 快递公司
     */
    private String logiCode;

    private String logiNo;

    /**
     * 收件人
     */
    private String addressee;

    /**
     * 电话
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRefundReson() {
        return refundReson;
    }

    public void setRefundReson(String refundReson) {
        this.refundReson = refundReson;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public Integer getGoodsStatus() {
        return goodsStatus;
    }

    public void setGoodsStatus(Integer goodsStatus) {
        this.goodsStatus = goodsStatus;
    }

    public Integer getApplyType() {
        return applyType;
    }

    public void setApplyType(Integer applyType) {
        this.applyType = applyType;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getLogiCode() {
        return logiCode;
    }

    public void setLogiCode(String logiCode) {
        this.logiCode = logiCode;
    }

    public String getLogiNo() {
        return logiNo;
    }

    public void setLogiNo(String logiNo) {
        this.logiNo = logiNo;
    }

    public String getAddressee() {
        return addressee;
    }

    public void setAddressee(String addressee) {
        this.addressee = addressee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}