package com.net.system.controller.order;

import java.util.HashMap;
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

import com.auth0.jwt.JWT;
import com.net.common.annotation.OperationLog;
import com.net.common.interceptor.Authorize;
import com.net.common.interceptor.SecurityParameter;
import com.net.common.util.AesEncryptUtil;
import com.net.common.util.ResultBean;
import com.net.common.util.ShiroUtil;
import com.net.system.controller.product.AESUtil;
import com.net.system.model.SysLog;
import com.net.system.model.User;
import com.net.system.service.order.UserReceivingInfoService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/receivingInfo")
public class UserReceivingInfoController {

	@Resource
	private UserReceivingInfoService userReceivingInfoService;

	private AESUtil aESUtil = new AESUtil();

	private AesEncryptUtil aesEncryptUtil = AesEncryptUtil.getAesEncryptUtil();

	@OperationLog("添加用户收货地址")
	@PostMapping("/wx/addUserReceivingInfo")
	@ResponseBody
	@SecurityParameter(inDecode = false, outEncode = true)
	@Authorize(required = true)
	@ApiImplicitParam(name = "requestData", value = "用户收货信息", required = false, dataType = "Object")
	@ApiOperation(value = "添加用户收货地址", notes = "添加用户收货地址")
	public ResultBean addUserReceivingInfo(HttpServletRequest request, @RequestParam String param) {

		try {
			// 获取当前登录用户名
			String token = request.getHeader("authorization");
			String userId = JWT.decode(token).getKeyId();
			String data = aesEncryptUtil.desEncrypt(param);
			Map map = aesEncryptUtil.transStringToMap(data);
			map.put("userId", userId);
			return ResultBean.success(userReceivingInfoService.addUserReceivingInfo(map));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultBean.error("参数解析失败");
	}

	@OperationLog("修改用户收货地址")
	@PostMapping("/wx/updateUserReceivingInfo")
	@ResponseBody
	@SecurityParameter(inDecode = false, outEncode = true)
	@Authorize(required = true)
	@ApiImplicitParam(name = "requestData", value = "用户收货信息", required = false, dataType = "Object")
	@ApiOperation(value = "修改用户收货地址", notes = "修改用户收货地址")
	public ResultBean updateUserReceivingInfo(HttpServletRequest request, @RequestParam String param) {
		try {
			// 获取当前登录用户名
			String token = request.getHeader("authorization");
			String userId = JWT.decode(token).getKeyId();
			String data = aesEncryptUtil.desEncrypt(param);
			Map<String,Object> map = aesEncryptUtil.transStringToMap(data);
			map.put("userId", userId);
			return ResultBean.success(userReceivingInfoService.updateUserReceivingInfo(map));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultBean.error("参数解析失败");
	}

	@OperationLog("删除用户收货地址")
	@PostMapping("/wx/delUserReceivingInfo")
	@ResponseBody
	@SecurityParameter(inDecode = false, outEncode = true)
	@Authorize(required = true)
	@ApiImplicitParam(name = "requestData", value = "用户收货信息", required = false, dataType = "Object")
	@ApiOperation(value = "删除用户收货地址", notes = "删除用户收货地址")
	public ResultBean delUserReceivingInfo(HttpServletRequest request, @RequestParam String param) {

		try {
			// 获取当前登录用户名
			String token = request.getHeader("authorization");
			String userId = JWT.decode(token).getKeyId();
			String data = aesEncryptUtil.desEncrypt(param);
			Map map = aesEncryptUtil.transStringToMap(data);
			map.put("userId", userId);
			return ResultBean.success(userReceivingInfoService.delUserReceivingInfo(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultBean.error("参数解析失败");
	}

	@OperationLog("查询用户所有收货地址")
	@PostMapping("/wx/findUserReceivingInfo")
	@ResponseBody
	@SecurityParameter(inDecode = false, outEncode = false)
	@Authorize(required = true)
	@ApiImplicitParam(name = "requestData", value = "用户收货信息", required = false, dataType = "Object")
	@ApiOperation(value = "查询用户所有收货地址", notes = "查询用户所有收货地址")
	public ResultBean findUserReceivingInfo(HttpServletRequest request) {
			Map<String,Object> map=new HashMap<String, Object>();
		try {
			// 获取当前登录用户名
			String token = request.getHeader("authorization");
			String userId = JWT.decode(token).getKeyId();
			map.put("userId", userId);
			return ResultBean.success(userReceivingInfoService.findUserReceivingInfo(map));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultBean.error("参数解析失败");
	}

	@OperationLog("查询用户收货地址详情")
	@PostMapping("/wx/findUserReceivingInfoById")
	@ResponseBody
	@SecurityParameter(inDecode = false, outEncode = true)
	@Authorize(required = true)
	@ApiImplicitParam(name = "requestData", value = "用户收货信息", required = false, dataType = "Object")
	@ApiOperation(value = "查询用户收货地址详情", notes = "查询用户收货地址详情")
	public ResultBean findUserReceivingInfoById(HttpServletRequest request, @RequestParam String id) {
			Map<String,Object> map=new HashMap<String, Object>();
		try {
			map.put("id", id);
			return ResultBean.success(userReceivingInfoService.findUserReceivingInfoById(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultBean.error("参数解析失败");
	}

	@OperationLog("查询用户默认收货地址")
	@PostMapping("/wx/findUserDefaultReceivingInfo")
	@ResponseBody
	@SecurityParameter(inDecode = false, outEncode = true)
	@Authorize(required = true)
	@ApiImplicitParam(name = "requestData", value = "用户收货信息", required = false, dataType = "Object")
	@ApiOperation(value = "查询用户默认收货地址", notes = "查询用户默认收货地址")
	public ResultBean findUserDefaultReceivingInfo(HttpServletRequest request, @RequestParam String param) {

		try {
			// 获取当前登录用户名
			String token = request.getHeader("authorization");
			String userId = JWT.decode(token).getKeyId();
			String data = aesEncryptUtil.desEncrypt(param);
			Map map = aesEncryptUtil.transStringToMap(data);
			map.put("userId", userId);
			return ResultBean.success(userReceivingInfoService.findUserDefaultReceivingInfo(map));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultBean.error("参数解析失败");
	}

}
