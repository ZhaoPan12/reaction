package com.net.system.controller.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auth0.jwt.JWT;
import com.github.pagehelper.PageInfo;
import com.net.common.annotation.OperationLog;
import com.net.common.config.WxSPConfig;
import com.net.common.interceptor.Authorize;
import com.net.common.interceptor.SecurityParameter;
import com.net.common.util.AesEncryptUtil;
import com.net.common.util.DateUtils;
import com.net.common.util.PageResultBean;
import com.net.common.util.ResultBean;
import com.net.common.util.Signature;
import com.net.common.util.WxPayHttp;
import com.net.common.util.XmlUtil;
import com.net.system.controller.product.AESUtil;
import com.net.system.model.CardUser;
import com.net.system.model.Order;
import com.net.system.model.OrderProduct;
import com.net.system.model.Product;
import com.net.system.model.User;
import com.net.system.service.order.OrderService;
import com.net.system.service.sysmange.UserService;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/order")
@Api(tags = "OrderController", description = "商城订单")
public class ShopOrderController {

	@Autowired
	private UserService userService;

	@Autowired
	private OrderService orderService;

	private AESUtil aESUtil = new AESUtil();

	private Logger logger = LoggerFactory.getLogger(OrderController.class);

	private AesEncryptUtil aesEncryptUtil = AesEncryptUtil.getAesEncryptUtil();

	@OperationLog("商品订单列表")
	@PostMapping("/wx/findCommodityOrder")
	@ResponseBody
	@Authorize(required = true)
	@SecurityParameter(inDecode = false, outEncode = true)
	@ApiImplicitParam(name = "map", value = "商品订单列表", required = false, dataType = "Object")
	public PageResultBean<Order> findCartPage(HttpServletRequest request, @RequestParam String param) {
		try {
			String token = request.getHeader("authorization");
			String userId = JWT.decode(token).getKeyId();
			String data = aesEncryptUtil.desEncrypt(param);
			Map map = aesEncryptUtil.transStringToMap(data);
			map.put("userId", userId);
			PageInfo<Order> pageInfo = orderService.findCommodityOrder(map);
			return new PageResultBean<Order>(pageInfo.getTotal(), pageInfo.getList());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new PageResultBean<Order>(0, new ArrayList<>());
	}

	@OperationLog("商品订单详情")
	@PostMapping("/wx/findCommodityOrderById")
	@Authorize(required = true)
	@SecurityParameter(inDecode = false, outEncode = true)
	@ResponseBody
	@ApiImplicitParam(name = "cart", value = "购物车信息", required = false, dataType = "Object")
	public ResultBean findCommodityOrderById(@RequestParam String param) {
		try {
			
			String data = aesEncryptUtil.desEncrypt(param);
			Map map = aesEncryptUtil.transStringToMap(data);
			return ResultBean.success(orderService.findCommodityOrderById(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultBean.error("参数解析失败");
	}

	@OperationLog("订单取消")
	@PostMapping("/wx/cancelCommodityOrder")
	@Authorize(required = true)
	@SecurityParameter(inDecode = false, outEncode = false)
	@ResponseBody
	public ResultBean cancelCommodityOrder(@RequestParam String param) {
		try {
			String data = aesEncryptUtil.desEncrypt(param);
			Map map = aesEncryptUtil.transStringToMap(data);
			return ResultBean.success(orderService.cancelCommodityOrder(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultBean.error("参数解析失败");
	}

	@OperationLog("订单删除")
	@PostMapping("/wx/deteteOrder")
	@Authorize(required = true)
	@SecurityParameter(inDecode = false, outEncode = false)
	@ResponseBody
	public ResultBean deteteOrder(@RequestParam String param) {
		try {

			String data = aesEncryptUtil.desEncrypt(param);
			Map map = aesEncryptUtil.transStringToMap(data);
			return ResultBean.success(orderService.deteteOrder(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultBean.error("参数解析失败");
	}
	
	@OperationLog("确认收货")
	@PostMapping("/wx/complete")
	@Authorize(required = true)
	@SecurityParameter(inDecode = false, outEncode = false)
	@ResponseBody
	public ResultBean complete(@RequestParam String param) {
		try {

			String data = aesEncryptUtil.desEncrypt(param);
			Map map = aesEncryptUtil.transStringToMap(data);
			return ResultBean.success(orderService.complete(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultBean.error("参数解析失败");
	}

	@OperationLog("用户下单")
	@PostMapping("/wx/userShopOrder")
	@ResponseBody
	@ApiOperation(value = "用户下单", notes = "用户下单")
	@SecurityParameter(outEncode = false, inDecode = false)
	@Authorize(required = true)
	public ResultBean userShopOrder(HttpServletRequest request, @RequestParam String param) {
		try {
			String token = request.getHeader("authorization");
			String userId = JWT.decode(token).getKeyId();
			String data = aesEncryptUtil.desEncrypt(param);
			Map<String,Object> map = aesEncryptUtil.transStringToMap(data);
//      map={orderId=75061bdf72f44e90aba296ec9cac0fc2, type=2}
			map.put("userId", userId);
			return orderService.shopOrderPay(map, request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	@OperationLog("微信支付回调")
    @RequestMapping("/wx/shopNotifyResultCoupon")
    @ResponseBody
    @ApiOperation(value = "微信支付回调", notes="微信支付回调")
    @SecurityParameter(outEncode=false,inDecode=false)
    @Authorize(required=false)
    public void shopNotifyResultCoupon(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("微信回调request===>" + request);
		// 获取微信异步通知的数据
		Map<String, String> paramsToMap = XmlUtil.reqParamsToMap(request);
		logger.info("微信回调参数map===>" + paramsToMap);
		// 校验微信的sign值
		boolean flag = Signature.validateSign(paramsToMap, WxSPConfig.MCH_KEY);
		if (flag) {
			System.out.println("------------------------------------------------------");
			// 根据订单号查询订单信息
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("superSerialNumber", paramsToMap.get("out_trade_no").toString());
			Order order=orderService.selectByOrderNo(paramsToMap.get("out_trade_no").toString());
			System.out.println("------------------------------------------------------:"+order);
				logger.info("微信回调订单实体===>" + order.toString());
				// 判断是否是未支付状态
				if (order.getStatus() == 1) {
					// 更新订单状态
					order.setStatus(2);
					order.setPaymentStatus(2);
					//1.微信支付
					order.setPaymentType(1);
					order.setTransactionId(paramsToMap.get("transaction_id").toString());
					order.setPaymentTime(DateUtils.dateNumberToDateStr(paramsToMap.get("time_end").toString()));
					JSONObject jsonObj = new JSONObject(paramsToMap);
					order.setNotifyResult(jsonObj.toString());
					// 调用更新订单状态接口
					orderService.updateOrderStatus(order);
				}
			}
			// 如果订单修改成功,通知微信接口不要在回调这个接口
			WxPayHttp.responseXmlSuccess(response);
		}
}
