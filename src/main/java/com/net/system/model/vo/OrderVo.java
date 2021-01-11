package com.net.system.model.vo;

import java.util.List;

import com.net.system.model.Order;
import com.net.system.model.OrderProduct;
import com.net.system.model.OrderReceivingInfo;
import com.net.system.model.UserReceivingInfo;

public class OrderVo extends Order{
	
	private UserReceivingInfo userReceivingInfo;
	
	private List<OrderProduct> orderProductList;
	
	/**
	 * 订单图片
	 */
	private String proImage;
	
	/**
	 * 订单商品名称
	 */
	private String proName;
	
	/**
	 * 商品数量
	 */
	private Integer quantity;
	
	
    private Integer refundStatus;


    

	public UserReceivingInfo getUserReceivingInfo() {
		return userReceivingInfo;
	}

	public void setUserReceivingInfo(UserReceivingInfo userReceivingInfo) {
		this.userReceivingInfo = userReceivingInfo;
	}

	public List<OrderProduct> getOrderProductList() {
		return orderProductList;
	}

	public void setOrderProductList(List<OrderProduct> orderProductList) {
		this.orderProductList = orderProductList;
	}

	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getProImage() {
		return proImage;
	}

	public void setProImage(String proImage) {
		this.proImage = proImage;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
	

}
