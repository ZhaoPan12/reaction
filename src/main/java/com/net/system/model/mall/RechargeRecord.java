package com.net.system.model.mall;


import java.math.BigDecimal;
import java.util.Date;

public class RechargeRecord {
    /**
     * id
     */
    private String id;

    /**
     * 订单号
     */
    private String orderno;

    /**
     * 流水号
     */
    private Integer serialnum;

    /**
     * 支付类型
     */
    private Integer paytype;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 充值面额
     */
    private BigDecimal rechargeName;

    /**
     * 用户id
     */
    private Integer userid;

    /**
     * 支付时间
     */
    private Date paytime;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 关闭时间
     */
    private Date closetime;
    
    private Integer type;
    
    /**
     * 支付通知结果
     */
    private String notifyResult;
    
    private String acckind;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public Integer getSerialnum() {
        return serialnum;
    }

    public void setSerialnum(Integer serialnum) {
        this.serialnum = serialnum;
    }

    public Integer getPaytype() {
        return paytype;
    }

    public void setPaytype(Integer paytype) {
        this.paytype = paytype;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getRechargeName() {
        return rechargeName;
    }

    public void setRechargeName(BigDecimal rechargeName) {
        this.rechargeName = rechargeName;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Date getPaytime() {
        return paytime;
    }

    public void setPaytime(Date paytime) {
        this.paytime = paytime;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getClosetime() {
        return closetime;
    }

    public void setClosetime(Date closetime) {
        this.closetime = closetime;
    }

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getNotifyResult() {
		return notifyResult;
	}

	public void setNotifyResult(String notifyResult) {
		this.notifyResult = notifyResult;
	}

	public String getAcckind() {
		return acckind;
	}

	public void setAcckind(String acckind) {
		this.acckind = acckind;
	}
	
	
    
    
}