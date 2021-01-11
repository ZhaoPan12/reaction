package com.net.system.service.imple.order;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections.map.HashedMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.net.common.util.DateUtils;

import com.net.common.config.WxSPConfig;
import com.net.common.rabbitmq.RabbitConfig;
import com.net.common.rabbitmq.RabbitmqPublish;
import com.net.common.util.BeanUtil;

import com.net.common.util.MapToUtil;
import com.net.common.util.ResultBean;
import com.net.system.mapper.cardpack.CardUserMapper;
import com.net.system.mapper.order.CartMapper;
import com.net.system.mapper.order.OrderLogisticsMapper;

import com.net.common.util.Signature;
import com.net.common.util.WxPayHttp;
import com.net.common.util.XmlUtil;

import com.net.system.mapper.order.OrderMapper;
import com.net.system.mapper.order.OrderProductMapper;

import com.net.system.mapper.order.OrderReceivingInfoMapper;

import com.net.system.mapper.product.DiscountMapper;
import com.net.system.mapper.product.DiscountUserMapper;

import com.net.system.mapper.product.ProductMapper;
import com.net.system.mapper.product.ProductSkuMapper;
import com.net.system.mapper.sysmange.UserMapper;
import com.net.system.model.CardUser;
import com.net.system.model.Cart;
import com.net.system.model.Discount;
import com.net.system.model.DiscountUser;
import com.net.system.model.Order;
import com.net.system.model.OrderLogistics;
import com.net.system.model.OrderProduct;
import com.net.system.model.OrderReceivingInfo;
import com.net.system.model.Product;
import com.net.system.model.ProductSku;
import com.net.system.model.UserReceivingInfo;
import com.net.system.model.vo.OrderVo;
import com.net.system.model.vo.ShopOrderVo;
import com.net.system.model.vo.WxParam;

import com.net.system.service.order.OrderService;

import cn.hutool.core.lang.UUID;

@Service
public class OrderServiceImple implements OrderService {

	private Logger logger = LoggerFactory.getLogger(OrderServiceImple.class);

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private OrderProductMapper orderProductMapper;

	@Autowired
	private ProductSkuMapper productSkuMapper;

	@Autowired
	private DiscountMapper discountMapper;

	@Autowired
	private DiscountUserMapper discountUserMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private CartMapper cartMapper;

	@Autowired
	private OrderReceivingInfoMapper orderReceivingInfoMapper;

	@Autowired
	private OrderLogisticsMapper orderLogisticsMapper;

	@Autowired
	private CardUserMapper cardUserMapper;

	@Autowired
	private RabbitmqPublish rabbitmqPublish;

	private static final String NOTIFY_URL = "http://card.tzcitycard.com/reaction/order/wx/notifyResultCoupon";
	private static final String SHOP_NOTIFY_URL = "http://tl.zyyg0523.cn/reaction/order/wx/shopNotifyResultCoupon";
	// private static final String SHOP_NOTIFY_URL =
	// "http://smk.zyyg0523.cn/reaction/order/wx/shopNotifyResultCoupon";
	// private static final String SHOP_NOTIFY_URL =
	// "http://localhost/reaction/order/wx/shopNotifyResultCoupon";

	@Override
	@Transactional
	public ResultBean OrderPay(Map<String, Object> map, HttpServletRequest request) throws Exception {
		try {
			OrderProduct productList = new OrderProduct();
			// 获取订单id
			String orderId = (String) map.get("orderId");
			// 获取优惠券id
			String couponId = (String) map.get("couponId");
			Order order = orderMapper.selectById(orderId);
			String userId = (String) map.get("userId");
			if (order == null) {
				order = new Order();
				order.setId(UUID.randomUUID().toString().replace("-", ""));
				order.setBusinessId(userId);
				order.setFreight(new BigDecimal(0.00));
				order.setStatus(1);
				// 获取订单号
				order.setSerialNumber(getOrderIdByTime());
				// 获取订单的商品信息
				productList = MapToUtil.decode(OrderProduct.class, map, "orderProduct");
				productList.setOrderId(order.getId());

				CardUser cardUser = new CardUser();
				cardUser.setUserid(Integer.parseInt(userId));
				cardUser.setProductid(productList.getId());
				CardUser cardUsers = cardUserMapper.selectByUserCard(cardUser);
				if (cardUsers != null && cardUsers.getStatus() == 1) {
					return ResultBean.error("已经购买过该长三角卡!请前往激活");
				} else if (cardUsers != null && cardUsers.getStatus() == 2) {
					return ResultBean.error("已经购买过该长三角卡");
				} else if (cardUsers != null && cardUsers.getStatus() == 3) {
					cardUsers.setStatus(0);
					cardUserMapper.updateByUserCardStatus(cardUsers);
				}
				Product product = productMapper.selectById(productList.getProId());
				BigDecimal total = product.getPrice();
				total = total.multiply(new BigDecimal(productList.getQuantity()));
				// 如果使用了优惠券,则修改优惠券状态
				if (couponId != null) {
					Discount discount = discountMapper.selectById(couponId);
					total = total.subtract(discount.getReduction());
					DiscountUser discountUser = new DiscountUser();
					discountUser.setUserid(Integer.parseInt(userId));
					discountUser.setDiscountid(couponId);
					discountUser.setStatus(2);
					discountUserMapper.updateDynamic(discountUser);
				}
				productList.setDescription(product.getDescription());
				productList.setProName(product.getProductname());
				productList.setProImage(product.getMainimage());
				productList.setCreateTime(new Date());
				productList.setId(UUID.randomUUID().toString().replace("-", ""));
				productList.setActualPrice(total);
				productList.setTotalPrice(total);
				productList.setUnitPrice(product.getPrice());
				orderProductMapper.insertDynamic(productList);
				order.setMemberId(Integer.parseInt(userId));
				order.setAddTime(new Date());
				order.setPaymentStatus(1);
				order.setActivityType(2);
				order.setTotal(total);
				order.setActual(total);
				orderMapper.insertDynamic(order);
			}
			// 微信支付
			WxParam wxParam = new WxParam();
			wxParam.setWxParam(wxParam, order.getSerialNumber(), order.getActual().doubleValue(), request, NOTIFY_URL,
					userMapper.getUserOpenId(userId));
			Map<String, String> wxParamTOMap = BeanUtil.convertBeanToMap(wxParam);
			String sign = Signature.getSign(wxParamTOMap, WxSPConfig.MCH_KEY);
			wxParamTOMap.put("sign", sign);
			String xmlResponse = WxPayHttp.doPostPayUnifiedOrder(WxSPConfig.UNIFIED_ORDER_URL,
					XmlUtil.createRequestXml(wxParamTOMap));
			Map<String, String> wxParamVO = parseXmlResponse(xmlResponse);
			wxParamVO.put("serialNumber", order.getSerialNumber());
			return ResultBean.success(wxParamVO);
		} catch (Exception e) {
			return ResultBean.error("错误");
		}
	}

	@Override
	public List<Order> findPageWithResult(Map<String, Object> map) {
		String page = (String) map.get("page");
		String rows = (String) map.get("limit");
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		// TODO Auto-generated method stub
		return orderMapper.findPageWithResult(map);
	}

	@Override
	public Order selectByOrderNo(String orderNo) {
		// TODO Auto-generated method stub
		return orderMapper.selectByOrderNo(orderNo);
	}

	@Override
	public List<Order> selectByMemberId(String memberId) {

		return orderMapper.selectByMemberId(memberId);
	}

	/* 解析微信请求响应的数据并返回小程序需要的数据 */
	private Map<String, String> parseXmlResponse(String xmlResponse) {
		Map<String, String> resultMap = new HashMap<>();
		// 判断sign值是否正确
		Boolean flag = Signature.validateResponseXmlToMap(xmlResponse);
		if (flag) {
			Map<String, String> map = XmlUtil.xmlToMap(xmlResponse);
			logger.info("微信支付请求响应的数据的预支付ID====>" + map.get("prepay_id"));
			resultMap.put("appId", WxSPConfig.APP_ID_PUBLIC);
			resultMap.put("timeStamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			resultMap.put("nonceStr", UUID.randomUUID().toString().replaceAll("-", ""));
			resultMap.put("package", "prepay_id=" + map.get("prepay_id").toString());
			resultMap.put("signType", "MD5");
			String sign = Signature.getSign(resultMap, WxSPConfig.MCH_KEY);
			resultMap.put("paySign", sign);
		}
		return resultMap;
	}

	public static String getOrderIdByTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String newDate = sdf.format(new Date());
		String result = "";
		Random random = new Random();
		for (int i = 0; i < 3; i++) {
			result += random.nextInt(10);
		}
		return newDate + result;
	}

	@Override
	public List<OrderVo> findPageWithResultByType(Map<String, Object> map) {
		Integer page = Integer.parseInt(map.get("page").toString());
		Integer limit = Integer.parseInt(map.get("limit").toString());

		if (null == map.get("type")) {
			map.put("type", 1);
		}

		PageHelper.startPage(page, limit);
		return orderMapper.findPageWithResultByType(map);
	}

	@Override
	public Map<String, Object> orderInfo(Order order) {
		Map<String, Object> map = new HashMap<String, Object>();

		// 订单信息
		Order orderInfo = orderMapper.selectById(order.getId());

		// 订单商品信息
		List<OrderProduct> orderProduct = orderProductMapper.findOrderProduct(order.getId());

		if (orderInfo.getActivityType() == 1) {

			// 订单收货地址
			OrderReceivingInfo orderReceivingInfo = orderReceivingInfoMapper.findByOrderId(order);

			// 物流信息
			OrderLogistics orderLogistics = orderLogisticsMapper.findByOrderId(order);

			map.put("orderReceivingInfo", orderReceivingInfo);
			map.put("orderLogistics", orderLogistics);
		}
		map.put("orderInfo", orderInfo);
		map.put("orderProduct", orderProduct);
		return map;
	}

	@Override
	public int addRemarks(Order order) {
		return orderMapper.updateDynamic(order);
	}

	@Override
	public int updateReceivingInfo(OrderReceivingInfo orderReceivingInfo) {
		// TODO Auto-generated method stub
		return orderReceivingInfoMapper.updateDynamic(orderReceivingInfo);
	}

	@Override
	public int orderDeliverGoods(OrderLogistics orderLogistics) {
		// 新增物流信息
		orderLogistics.setId(UUID.randomUUID().toString().replace("-", ""));
		orderLogistics.setAddTime(DateUtils.getNowDate());
		orderLogisticsMapper.insert(orderLogistics);

		// 修改订单信息为已发货
		Order order = new Order();
		order.setId(orderLogistics.getOrderId());
		order.setStatus(5);
		order.setDeliveryTime(orderLogistics.getAddTime());
		order.setFreight(new BigDecimal(0));
		order.setUpdateTime(orderLogistics.getAddTime());
		return orderMapper.updateDynamic(order);
	}

	@Override
	public int orderReject(Order order) {

		// 订单信息修改
		order.setStatus(4);
		order.setUpdateTime(DateUtils.getNowDate());

		// 订单退款操作

		return orderMapper.updateDynamic(order);
	}

	@Override
	public boolean cancelOrder(Map<String, Object> map) {
		// TODO Auto-generated method stub
		try {
			orderMapper.updateStatus(map);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public int updateOrderStatus(Order order) {
		// TODO Auto-generated method stub
		return orderMapper.updateOrderStatus(order);
	}

	@Override
	public Order selectById(String id) {
		// TODO Auto-generated method stub
		return orderMapper.selectById(id);
	}

	@Override
	public PageInfo<Order> findCommodityOrder(Map<String, Object> map) {
		Integer page = Integer.parseInt(map.get("page").toString());
		Integer limit = Integer.parseInt(map.get("limit").toString());
		PageHelper.startPage(page, limit);
		List<Order> result = orderMapper.findCommodityOrder(map);
		PageInfo<Order> pageInfo = new PageInfo<Order>(result);
		return pageInfo;
	}

	@Override
	public ShopOrderVo findCommodityOrderById(Map<String, Object> map) {
		ShopOrderVo shopOrderVo=orderMapper.findCommodityOrderById(map);
		//查询优惠劵
		if(!shopOrderVo.getCouponId().equals("")) {
			shopOrderVo.setCouponName(discountUserMapper.findCouponName(shopOrderVo.getCouponId()));
		}
		
		
		return shopOrderVo;
	}

	@Override
	public int cancelCommodityOrder(Map<String, Object> map) {
		ShopOrderVo order = orderMapper.findCommodityOrderById(map);

		// 归还库存
		List<OrderProduct> productList = order.getOrderProductList();
		for (OrderProduct p : productList) {
			productMapper.returnStock(p);
			productSkuMapper.returnStock(p);
		}

		// 归还优惠劵
		if (order.getCouponId() != null && !order.getCouponId().equals("")) {

			// 判断优惠劵是否过期
			DiscountUser discountUser = discountUserMapper.selectById(order.getCouponId());

			if (DateUtils.calLastedTime(discountUser.getInvalidtime()) < 0) {
				discountUser.setStatus(1);
				discountUserMapper.updateDynamic(discountUser);
			}

		}

		// 修改订单状态
		Order updateOrder = new Order();
		updateOrder.setId(order.getId());
		updateOrder.setStatus(3);
		updateOrder.setCloseTime(new Date());
		return orderMapper.updateDynamic(updateOrder);
	}

	@Override
	public int deteteOrder(Map<String, Object> map) {
		ShopOrderVo order = orderMapper.findCommodityOrderById(map);

		// 删除关联订单的商品
		List<OrderProduct> productList = order.getOrderProductList();
		for (OrderProduct p : productList) {
			orderProductMapper.delete(p.getId());
		}

		// 删除订单收货信息
		orderReceivingInfoMapper.delete(order.getOrderReceivingInfo().getId());

		// 删除订单物流信息
		orderLogisticsMapper.deleteByOrderId(order.getId());

		return orderMapper.delete(order.getId());
	}

	@Override
	public ResultBean shopOrderPay(Map<String, Object> map, HttpServletRequest request) {

		// 用户id
		String userId = map.get("userId").toString();

		// 订单id
		String orderId = map.get("orderId").toString();

		// type 1、重新下单 2、支付
		Integer type = Integer.parseInt(map.get("type").toString());
		
		//支付类型  1、微信 2、余额
				//Integer paymentType=Integer.parseInt(map.get("paymentType").toString());

		// 总金额
		double total = 0;

		// 优惠金额
		double discount = 0;

		// 实付金额
		double actual = 0;

		Order order = null;

		try {

			// 判断订单是否是新下单 还是重新支付
			if (type == 1) {

				// 优惠劵id
				String couponId = map.get("couponId").toString();

				// 1、商品详情页下单 2、购物车下单
				Integer orderType = Integer.parseInt(map.get("orderType").toString());

				List<OrderProduct> orderProducts = new ArrayList<OrderProduct>();
				orderId = UUID.randomUUID().toString().replace("-", "");

				List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("orderProductList");
				for (Map<String, Object> m : list) {
					OrderProduct orderProduct = new OrderProduct();
					orderProduct.setProImage(m.get("proImage").toString());
					orderProduct.setQuantity(new Double(m.get("quantity").toString()).intValue());
					orderProduct.setSku(m.get("sku").toString());
					orderProduct.setProId(m.get("proId").toString());
					orderProduct.setDescription(m.get("description").toString());
					orderProducts.add(orderProduct);
				}

				// 判断商品库存
				for (OrderProduct orderProduct : orderProducts) {
					ProductSku sku = productSkuMapper.selectBySkuId(orderProduct.getSku());
					if (sku != null) {
						Product product = productMapper.selectById(sku.getProductId());
						if (product != null && product.getStatus() == 1) {
							if (sku.getStock() > orderProduct.getQuantity()) {
								orderProduct.setId(UUID.randomUUID().toString().replace("-", ""));
								orderProduct.setOrderId(orderId);
								orderProduct.setProId(product.getId());
								orderProduct.setProName(product.getProductname());
								orderProduct.setSku(sku.getSkuId());
								orderProduct.setProductSn(product.getProductSn());
								orderProduct.setUnitPrice(sku.getPrice());
								orderProduct.setTotalPrice(new BigDecimal(sku.getPrice().doubleValue() * orderProduct.getQuantity()));
								orderProduct.setActualPrice(orderProduct.getTotalPrice());
								total += orderProduct.getTotalPrice().doubleValue();
							} else {
								return ResultBean.error("商品库存不足!");
							}
						} else {
							return ResultBean.error(product.getProductname() + "-商品已下架!");
						}

					} else {
						return ResultBean.error("商品参数错误!");
					}
				}

				// 购物车下单 修改购物车
				if (orderType == 2) {
					for (OrderProduct orderProduct : orderProducts) {
						Cart cart = new Cart();
						cart.setUserId(userId);
						cart.setSkuId(orderProduct.getSku());
						cartMapper.deleteBySku(cart);
					}

				}

				// 扣除库存
				for (OrderProduct orderProduct : orderProducts) {
					productMapper.reduceStock(orderProduct);
					productSkuMapper.reduceStock(orderProduct);
					orderProductMapper.insert(orderProduct);
				}

				// 判断优惠劵
				if (!couponId.equals("")) {
					Map<String, Object> reduction = discountUserMapper.findReductionById(couponId);
					if (reduction != null) {
						discount = Double.parseDouble(reduction.get("reduction").toString());
						// 修改优惠劵状态
						DiscountUser discountUser=new DiscountUser();
						discountUser.setId(couponId);
						discountUser.setStatus(2);
						discountUserMapper.update(discountUser);
					} else {
						return ResultBean.error("优惠劵已失效!");
					}
				}

				// 添加订单收货地址
				Map<String, Object> userReceivingInfo = (Map<String, Object>) map.get("userReceivingInfo");
				OrderReceivingInfo receivingInfo = new OrderReceivingInfo();
				receivingInfo.setReceiverName(userReceivingInfo.get("receiverName").toString());
				receivingInfo.setReceiverPhone(userReceivingInfo.get("receiverPhone").toString());
				receivingInfo.setReceiverProvince(userReceivingInfo.get("receiverProvince").toString());
				receivingInfo.setReceiverCity(userReceivingInfo.get("receiverCity").toString());
				receivingInfo.setReceiverDistrict(userReceivingInfo.get("receiverDistrict").toString());
				receivingInfo.setReceiverAddress(userReceivingInfo.get("receiverAddress").toString());
				receivingInfo.setCreateTime(new Date());
				receivingInfo.setId(UUID.randomUUID().toString().replace("-", ""));
				receivingInfo.setOrderId(orderId);
				orderReceivingInfoMapper.insert(receivingInfo);

				// 添加订单
				actual = new BigDecimal(String.format("%.2f", total - discount)).doubleValue();
				order = new Order();
				order.setId(orderId);
				order.setSerialNumber(getOrderIdByTime());
				order.setMemberId(Integer.parseInt(userId));
				order.setFreight(new BigDecimal(0));
				order.setStatus(1);
				order.setTotal(new BigDecimal(total));
				order.setActual(new BigDecimal(actual));
				order.setAddTime(new Date());
				order.setPaymentStatus(1);
				order.setBuyerMessage(map.get("buyerMessage").toString());
				order.setPaymentEndTime(DateUtils.calculateDay(new Date(), 1));
				order.setCouponId(couponId);
				order.setDiscount(new BigDecimal(discount));
				order.setActivityType(1);
				order.setRefundStatus(0);
				order.setPaymentType(1);
				orderMapper.insertDynamic(order);

				// 订单延时24小时
				// 发送消息队列 监听订单支付状态
//				rabbitmqPublish.sendTimeoutMsg(RabbitConfig.DELAYED_EXCHANGE_XDELAY_ORDER_PAYMENT,
//						RabbitConfig.DELAY_ROUTING_KEY_XDELAY_ORDER_PAYMENT, order.getId(), 86400);
			} else {
				order = orderMapper.selectById(orderId);
				if (order != null && order.getStatus() == 1) {
					actual = order.getActual().doubleValue();
				} else {
					return ResultBean.error("订单支付失败");
				}
			}

			// 微信支付
			WxParam wxParam = new WxParam();
			wxParam.setWxParam(wxParam, order.getSerialNumber(), order.getActual().doubleValue(), request,
					SHOP_NOTIFY_URL, userMapper.getUserOpenId(userId));
			Map<String, String> wxParamTOMap = BeanUtil.convertBeanToMap(wxParam);
			String sign = Signature.getSign(wxParamTOMap, WxSPConfig.MCH_KEY);
			wxParamTOMap.put("sign", sign);
			String xmlResponse = WxPayHttp.doPostPayUnifiedOrder(WxSPConfig.UNIFIED_ORDER_URL,
					XmlUtil.createRequestXml(wxParamTOMap));
			Map<String, String> wxParamVO = parseXmlResponse(xmlResponse);
			wxParamVO.put("serialNumber", order.getSerialNumber());
			return ResultBean.success(wxParamVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultBean.error("错误");
	}

	@Override
	public int complete(Map<String, Object> map) {
		Order order=new Order();
		order.setId(map.get("id").toString());
		order.setStatus(6);
		return orderMapper.updateDynamic(order);
	}

}
