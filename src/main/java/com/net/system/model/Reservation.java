package com.net.system.model;

import java.util.Date;

public class Reservation {
    private String id;

    /**
     * 预约时间
     */
    private Date reservationtime;

    /**
     * 预约产品id
     */
    private String productid;

    /**
     * 预约用户id
     */
    private Integer reservationuser;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 状态
     */
    private Integer status;
    
    private Product product;
    
    private User user;
    
    private String orderNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getReservationtime() {
        return reservationtime;
    }

    public void setReservationtime(Date reservationtime) {
        this.reservationtime = reservationtime;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public Integer getReservationuser() {
        return reservationuser;
    }

    public void setReservationuser(Integer reservationuser) {
        this.reservationuser = reservationuser;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	
    
}