package com.net.system.controller.order;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.github.pagehelper.PageInfo;
import com.net.common.annotation.OperationLog;
import com.net.common.interceptor.Authorize;
import com.net.common.interceptor.SecurityParameter;
import com.net.common.util.AesEncryptUtil;
import com.net.common.util.PageResultBean;
import com.net.common.util.ResultBean;
import com.net.common.util.ShiroUtil;
import com.net.system.controller.product.AESUtil;
import com.net.system.model.Order;
import com.net.system.model.User;
import com.net.system.service.order.CartService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/cart")
public class CartController {

	@Resource
	private CartService cartService;

	private AESUtil aESUtil = new AESUtil();

	private Logger logger = LoggerFactory.getLogger(OrderController.class);

	private AesEncryptUtil aesEncryptUtil = AesEncryptUtil.getAesEncryptUtil();

	@OperationLog("根据id删除购物车")
	@PostMapping("/wx/delCart")
	@SecurityParameter(outEncode = true, inDecode = false)
	@Authorize(required = true)
	@ResponseBody
	@ApiImplicitParam(name = "requestData", value = "购物车信息", required = false, dataType = "Object")
	@ApiOperation(value = "根据id删除商品", notes = "根据id删除商品")
	public ResultBean delCart(@RequestParam String param) {

		try {
			String data = aesEncryptUtil.desEncrypt(param);
			Map map = aesEncryptUtil.transStringToMap(data);
			return ResultBean.success(cartService.delete(map));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ResultBean.error("参数解析失败");
	}

	@OperationLog("将商品添加到购物车")
	@PostMapping("/wx/addCart")
	@ResponseBody
	@SecurityParameter(outEncode = true, inDecode = false)
	@Authorize(required = true)
	@ApiOperation(value = "将商品添加到购物车", notes = "将商品添加到购物车")
	@ApiImplicitParam(name = "requestData", value = "购物车信息", required = false, dataType = "Object")
	public ResultBean addCart(HttpServletRequest request, @RequestParam String param) {
		try {
			String token = request.getHeader("authorization");
			String userId = JWT.decode(token).getKeyId();
			String data = aesEncryptUtil.desEncrypt(param);
			Map map = aesEncryptUtil.transStringToMap(data);
			map.put("userId", userId);
			return ResultBean.success(cartService.addCart(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultBean.error("参数解析失败");
	}

	@OperationLog("统计购物车商品数量")
	@PostMapping("/wx/countCartNumber")
	@SecurityParameter(outEncode = true, inDecode = false)
	@Authorize(required = true)
	@ResponseBody
	@ApiImplicitParam(name = "cart", value = "购物车信息", required = false, dataType = "Object")
	public ResultBean countCartNumber(HttpServletRequest request, @RequestParam String param) {
		try {
			String token = request.getHeader("authorization");
			String userId = JWT.decode(token).getKeyId();
			String data = aesEncryptUtil.desEncrypt(param);
			Map map = aesEncryptUtil.transStringToMap(data);
			map.put("userId", userId);
			return ResultBean.success(cartService.countCartNumber(map));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultBean.error("参数解析失败");

	}

	@OperationLog("修改购物车商品数量")
	@PostMapping("/wx/updateCartProductNumber")
	@SecurityParameter(outEncode = true, inDecode = false)
	@Authorize(required = true)
	@ResponseBody
	@ApiImplicitParam(name = "cart", value = "购物车信息", required = false, dataType = "Object")
	public ResultBean updateCartProductNumber(HttpServletRequest request, @RequestParam String param) {
		try {

			String data = aesEncryptUtil.desEncrypt(param);
			Map map = aesEncryptUtil.transStringToMap(data);
			return ResultBean.success(cartService.updateCartProductNumber(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultBean.error("参数解析失败");
	}

	@OperationLog("查询购物车列表")
	@PostMapping("/wx/findCartPage")
	@ResponseBody
	@SecurityParameter(outEncode = true, inDecode = false)
	@Authorize(required = true)
	@ApiImplicitParam(name = "map", value = "购物车分页参数", required = false, dataType = "Object")
	public PageResultBean<Map<String, Object>> findCartPage(HttpServletRequest request, @RequestParam String param) {
		try {
			String token = request.getHeader("authorization");
			String userId = JWT.decode(token).getKeyId();
			String data = aesEncryptUtil.desEncrypt(param);
			Map map = aesEncryptUtil.transStringToMap(data);
			map.put("userId", userId);
			PageInfo<Map<String, Object>> pageInfo = cartService.findCartPage(map);
			return new PageResultBean<Map<String, Object>>(pageInfo.getTotal(), pageInfo.getList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new PageResultBean<Map<String, Object>>(0, new ArrayList<>());
	}
}
