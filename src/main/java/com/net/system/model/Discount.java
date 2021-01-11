package com.net.system.model;

import java.math.BigDecimal;
import java.util.Date;

public class Discount {
    /**
     * id
     */
    private String id;

    /**
     * 名称
     */
    private String discountname;

    /**
     * 介绍
     */
    private String introduction;

    /**
     * 标签
     */
    private String label;

    /**
     * 最低消费
     */
    private BigDecimal minconsume;

    /**
     * 满减金额
     */
    private BigDecimal reduction;

    /**
     * 分发类型
     */
    private String type;

    /**
     * 有效期类型(1.领券后某某天 2.规定时间段)
     */
    private Integer validperiod;

    /**
     * 开始时间
     */
    private Date starttime;

    /**
     * 结束时间
     */
    private Date endtime;

    /**
     * 天
     */
    private Integer day;

    /**
     * 发行数量
     */
    private Integer quantity;

    /**
     * 限制范围(1.全场通用 2.指定分类 3.指定商品)
     */
    private String restricted;

    /**
     * 指定分类id
     */
    private String categoryid;

    /**
     * 指定商品id
     */
    private String productid;

    /**
     * 兑换码
     */
    private String redemptioncode;

    /**
     * 状态(1.正常 2.已过期)
     */
    private Integer status;
    
    private Date addTime;
    
    /**
         * 每人限领
     */
    private Integer limitedcollar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiscountname() {
        return discountname;
    }

    public void setDiscountname(String discountname) {
        this.discountname = discountname;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public BigDecimal getMinconsume() {
        return minconsume;
    }

    public void setMinconsume(BigDecimal minconsume) {
        this.minconsume = minconsume;
    }

    public BigDecimal getReduction() {
        return reduction;
    }

    public void setReduction(BigDecimal reduction) {
        this.reduction = reduction;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getValidperiod() {
        return validperiod;
    }

    public void setValidperiod(Integer validperiod) {
        this.validperiod = validperiod;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getRestricted() {
        return restricted;
    }

    public void setRestricted(String restricted) {
        this.restricted = restricted;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getRedemptioncode() {
        return redemptioncode;
    }

    public void setRedemptioncode(String redemptioncode) {
        this.redemptioncode = redemptioncode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Integer getLimitedcollar() {
		return limitedcollar;
	}

	public void setLimitedcollar(Integer limitedcollar) {
		this.limitedcollar = limitedcollar;
	}
    
}
