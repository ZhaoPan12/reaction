package com.net.system.model;

import java.util.Date;

public class CardUser {
    private String id;

    /**
     * 卡号
     */
    private Integer cardnum;

    /**
     * 卡id
     */
    private String productid;

    /**
     * 用户id
     */
    private Integer userid;

    /**
     * 状态(1.待激活 2.已生效 3.已过期)
     */
    private Integer status;

    /**
     * 添加时间
     */
    private Date addtime;

    /**
     * 过期时间
     */
    private Date endtime;

    /**
     * 激活时间
     */
    private Date effecttime;
    
    /**
         * 订单号
     */
    private String orderNo;
    
    
    private Product product;

    
    private String day;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getCardnum() {
        return cardnum;
    }

    public void setCardnum(Integer cardnum) {
        this.cardnum = cardnum;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public Date getEffecttime() {
        return effecttime;
    }

    public void setEffecttime(Date effecttime) {
        this.effecttime = effecttime;
    }

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}
	
	
    
}