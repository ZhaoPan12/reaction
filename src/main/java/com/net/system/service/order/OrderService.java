package com.net.system.service.order;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.net.common.util.ResultBean;
import com.net.system.model.Order;
import com.net.system.model.OrderLogistics;
import com.net.system.model.OrderReceivingInfo;
import com.net.system.model.vo.OrderVo;
import com.net.system.model.vo.ShopOrderVo;

public interface OrderService {
	
	//订单支付
	public ResultBean OrderPay(Map<String,Object> map,HttpServletRequest request)throws Exception;
	
	List<Order> findPageWithResult(Map<String,Object> map);
	
	Order selectById(String id);
	
	boolean cancelOrder(Map<String,Object> map);
	
	/**
	 * 后台订单列表
	 */
	List<OrderVo> findPageWithResultByType(Map<String,Object> map);
	
	Map<String,Object> orderInfo(Order order);
	
	
	int addRemarks(Order order);
	
	
	int updateReceivingInfo(OrderReceivingInfo orderReceivingInfo);
	
	
	int orderDeliverGoods(OrderLogistics orderLogistics);
	
	int orderReject(Order order);

	int updateOrderStatus(Order order);
	
	Order selectByOrderNo(String superSerialNumber);

	
	PageInfo<Order> findCommodityOrder(Map<String,Object> map);
	
	ShopOrderVo findCommodityOrderById(Map<String,Object> map);
	
	int cancelCommodityOrder(Map<String,Object> map);
	
	
	int deteteOrder(Map<String,Object> map);
	
    List<Order> selectByMemberId(String memberId);
    
    
    //商城订单支付
	public ResultBean shopOrderPay(Map<String, Object> map, HttpServletRequest request);
	
	int complete(Map<String, Object> map);
}

