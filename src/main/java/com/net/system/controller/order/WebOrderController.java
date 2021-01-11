package com.net.system.controller.order;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.net.common.annotation.OperationLog;
import com.net.common.util.PageResultBean;
import com.net.common.util.ResultBean;
import com.net.system.model.Order;
import com.net.system.model.OrderLogistics;
import com.net.system.model.OrderReceivingInfo;
import com.net.system.model.vo.OrderVo;
import com.net.system.service.order.OrderService;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/order")
public class WebOrderController {

	@Resource
	private OrderService orderService;

	@GetMapping("/index")
	@ApiIgnore
	public String index() {
		return "order/order-list";
	}

	@GetMapping("/orderProductInfo/{orderId}")
	@ApiIgnore
	public String orderProductInfo(@PathVariable("orderId") String orderId, Model model) {
		Order order = new Order();
		order.setId(orderId);
		model.addAttribute("order", orderService.orderInfo(order));
		return "order/order-product-info";
	}
	
	
	
	@GetMapping("/orderCardInfo/{orderId}")
	@ApiIgnore
	public String orderCardInfo(@PathVariable("orderId") String orderId, Model model) {
		Order order = new Order();
		order.setId(orderId);
		model.addAttribute("order", orderService.orderInfo(order));
		return "order/order-card-info";
	}

	@OperationLog("查询订单列表")
	@GetMapping("/findPageWithResultByType")
	@ResponseBody
	public PageResultBean<OrderVo> findPageWithResultByType(@RequestParam Map<String, Object> map) {
		List<OrderVo> list = orderService.findPageWithResultByType(map);
		PageInfo<OrderVo> pageInfo = new PageInfo<OrderVo>(list);
		return new PageResultBean<OrderVo>(pageInfo.getTotal(), pageInfo.getList());
	}

	
	
	@OperationLog("修改收货人信息")
	@PostMapping("/updateReceivingInfo")
	@ResponseBody
	public ResultBean updateReceivingInfo(OrderReceivingInfo orderReceivingInfo) {
		return ResultBean.success(orderService.updateReceivingInfo(orderReceivingInfo));
	}
	
	@OperationLog("订单发货")
	@PostMapping("/orderDeliverGoods")
	@ResponseBody
	public ResultBean orderDeliverGoods(OrderLogistics orderLogistics) {
		return ResultBean.success(orderService.orderDeliverGoods(orderLogistics));
	}
	
	@OperationLog("订单驳回")
	@PostMapping("/orderReject")
	@ResponseBody
	public ResultBean orderReject(Order order) {
		return ResultBean.success(orderService.orderReject(order));
	}
	
	

}
