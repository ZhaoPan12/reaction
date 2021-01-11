package com.net.system.controller.order;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.net.common.annotation.OperationLog;
import com.net.common.interceptor.Authorize;
import com.net.common.interceptor.SecurityParameter;
import com.net.common.util.PageResultBean;
import com.net.common.util.ResultBean;
import com.net.common.util.ShiroUtil;
import com.net.system.model.Order;
import com.net.system.model.OrderRefunds;
import com.net.system.model.User;
import com.net.system.model.vo.OrderRefundsVo;
import com.net.system.model.vo.OrderVo;
import com.net.system.service.order.OrderRefundsService;
import com.net.system.service.order.OrderService;

import cn.hutool.json.JSONObject;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/refunds")
public class OrderRefundsController {

	@Value("${photo.resourceLocations}")
	private String IMG_PATH_PREFIX;

	@Value("${photo.photoPath}")
	private String photoPath;

	@Resource
	private OrderRefundsService orderRefundsService;

	@Resource
	private OrderService orderService;

	@OperationLog("售后-待审核")
	@GetMapping("/web/notToExamineList")
	@ApiIgnore
	public String notToExamineList(Model model) {
		model.addAttribute("status", 1);
		model.addAttribute("goodsStatus", "");
		return "order/aftersales-list";
	}
	
	@OperationLog("售后-待发退款货")
	@GetMapping("/web/notDeliverGoods")
	@ApiIgnore
	public String notDeliverGoods(Model model) {
		model.addAttribute("status", 2);
		model.addAttribute("goodsStatus", "1");
		return "order/aftersales-list";
	}
	
	@OperationLog("售后-待收退款货")
	@GetMapping("/web/notReceivingGoods")
	@ApiIgnore
	public String notReceivingGoods(Model model) {
		model.addAttribute("status", 2);
		model.addAttribute("goodsStatus", "2");
		return "order/aftersales-list";
	}
	
	@OperationLog("售后-待退款")
	@GetMapping("/web/notrefund")
	@ApiIgnore
	public String notrefund(Model model) {
		model.addAttribute("status", 4);
		model.addAttribute("goodsStatus", "");
		return "order/aftersales-list";
	}
	
	@OperationLog("售后-已退款")
	@GetMapping("/web/refunded")
	@ApiIgnore
	public String refunded(Model model) {
		model.addAttribute("status", 5);
		model.addAttribute("goodsStatus", "");
		return "order/aftersales-list";
	}


	@OperationLog("查询售后订单列表")
	@GetMapping("/web/findOrderRefundsPage")
	@ResponseBody
	public PageResultBean<Map<String, Object>> findOrderRefundsPage(@RequestParam Map<String, Object> map) {
		List<Map<String, Object>> list = orderRefundsService.findPageWithResult(map);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
		return new PageResultBean<Map<String, Object>>(pageInfo.getTotal(), pageInfo.getList());

	}

	@GetMapping("/refundInfo/{orderId}")
	@ApiIgnore
	public String orderCardInfo(@PathVariable("orderId") String orderId, Model model) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", orderId);
		model.addAttribute("order", orderService.findCommodityOrderById(map));
		model.addAttribute("businessAddress",orderRefundsService.findBusinessAddress());
		return "order/order-refund-info";
	}

	@OperationLog("申请售后")
	@PostMapping("/wx/applyRefunds")
	@ResponseBody
	@Authorize(required = true)
	public ResultBean applyRefunds(@RequestBody OrderRefundsVo orderRefundsVo) {
		return ResultBean.success(orderRefundsService.applyRefunds(orderRefundsVo));
	}
	
	

	@OperationLog("取消售后")
	@PostMapping("/wx/cancelRefunds")
	@ResponseBody
	@Authorize(required = true)
	public ResultBean cancelRefunds(@RequestBody Map<String, Object> map) {
		return ResultBean.success(orderRefundsService.cancelRefunds((map.get("id").toString())));
	}

	@OperationLog("发货")
	@PostMapping("/wx/deliverGoods")
	@ResponseBody
	@Authorize(required = true)
	public ResultBean deliverGoods(@RequestBody OrderRefunds orderRefunds) {
		orderRefunds.setGoodsStatus(2);
		return ResultBean.success(orderRefundsService.updateStatus(orderRefunds));
	}
	
	@OperationLog("修改状态")
	@PostMapping("/updateStatus")
	@ResponseBody
	public ResultBean updateStatus(@RequestBody OrderRefunds orderRefunds) {
		
		return ResultBean.success(orderRefundsService.updateStatus(orderRefunds));
	}

	@PostMapping(value = "/wx/uploadImg")
	@ResponseBody
	public ResultBean uploadimages(MultipartFile file) throws IOException {

		String filename = UUID.randomUUID().toString().replaceAll("-", "");
		String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String filenames = filename + ext;
		String tagFilePath = IMG_PATH_PREFIX + filenames;// 单文件
		File dest = new File(tagFilePath);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		// 执行流写入
		file.transferTo(dest);
		return ResultBean.successData(photoPath + filenames);
	}

}
