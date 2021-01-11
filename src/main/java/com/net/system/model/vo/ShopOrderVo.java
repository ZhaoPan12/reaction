package com.net.system.model.vo;

import java.util.List;

import com.net.system.mapper.product.DiscountMapper;
import com.net.system.mapper.product.DiscountUserMapper;
import com.net.system.model.Discount;
import com.net.system.model.Order;
import com.net.system.model.OrderLogistics;
import com.net.system.model.OrderProduct;
import com.net.system.model.OrderReceivingInfo;
import com.net.system.model.OrderRefunds;

public class ShopOrderVo extends Order {

	private List<OrderProduct> orderProductList;

	private OrderReceivingInfo orderReceivingInfo;

	private OrderLogistics orderLogistics;

	private String couponName;
	
	private OrderRefundsVo orderRefunds;
	
	
	
	public OrderRefundsVo getOrderRefunds() {
		return orderRefunds;
	}

	public void setOrderRefunds(OrderRefundsVo orderRefunds) {
		this.orderRefunds = orderRefunds;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public OrderReceivingInfo getOrderReceivingInfo() {
		return orderReceivingInfo;
	}

	public void setOrderReceivingInfo(OrderReceivingInfo orderReceivingInfo) {
		this.orderReceivingInfo = orderReceivingInfo;
	}

	public OrderLogistics getOrderLogistics() {
		return orderLogistics;
	}

	public void setOrderLogistics(OrderLogistics orderLogistics) {
		this.orderLogistics = orderLogistics;
	}

	public List<OrderProduct> getOrderProductList() {
		return orderProductList;
	}

	public void setOrderProductList(List<OrderProduct> orderProductList) {
		this.orderProductList = orderProductList;
	}

}
