package com.net.system.model;


import java.util.Date;

public class DiscountUser {
    private String id;

    /**
     * 用户id
     */
    private Integer userid;

    /**
     * 领取时间
     */
    private Date collectiontime;

    /**
     * 优惠券id
     */
    private String discountid;

    /**
     * 优惠券状态
     */
    private Integer status;

    /**
     * 使用时间
     */
    private Date usagetime;

    /**
     * 使用订单
     */
    private String usageorder;

    /**
     * 失效时间
     */
    private Date invalidtime;
    
    
    private Discount discount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Date getCollectiontime() {
        return collectiontime;
    }

    public void setCollectiontime(Date collectiontime) {
        this.collectiontime = collectiontime;
    }

    public String getDiscountid() {
        return discountid;
    }

    public void setDiscountid(String discountid) {
        this.discountid = discountid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUsagetime() {
        return usagetime;
    }

    public void setUsagetime(Date usagetime) {
        this.usagetime = usagetime;
    }

    public String getUsageorder() {
        return usageorder;
    }

    public void setUsageorder(String usageorder) {
        this.usageorder = usageorder;
    }

    public Date getInvalidtime() {
        return invalidtime;
    }

    public void setInvalidtime(Date invalidtime) {
        this.invalidtime = invalidtime;
    }

	public Discount getDiscount() {
		return discount;
	}

	public void setDiscount(Discount discount) {
		this.discount = discount;
	}
    
    
}
