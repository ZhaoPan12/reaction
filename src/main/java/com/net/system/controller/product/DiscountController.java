package com.net.system.controller.product;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.auth0.jwt.JWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.net.common.annotation.OperationLog;
import com.net.common.interceptor.Authorize;
import com.net.common.interceptor.SecurityParameter;
import com.net.common.util.AesEncryptUtil;
import com.net.common.util.PageResultBean;
import com.net.common.util.ResultBean;
import com.net.system.model.Discount;
import com.net.system.model.DiscountUser;
import com.net.system.model.OrderProduct;
import com.net.system.model.Product;
import com.net.system.model.Tup;
import com.net.system.model.vo.ProeratorVo;
import com.net.system.service.product.DiscountService;
import com.net.system.service.product.DiscountUserService;
import com.net.system.service.product.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/discount")
@Api(tags = "DiscountController", description = "优惠券管理类")
public class DiscountController {

	@Autowired
	private DiscountService discountService;

	@Autowired
	private ProductService productService;

	@Autowired
	private DiscountUserService discountUserService;

	private AESUtil aESUtil = new AESUtil();

	private AesEncryptUtil aesEncryptUtil = AesEncryptUtil.getAesEncryptUtil();

	@GetMapping("/index")
	@ApiIgnore
	public String index(Model model) {
		return "discount/discount-list";
	}

	@GetMapping
	@ApiIgnore
	public String add(Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("label", "DISTYPE");
		List<Tup> list = productService.findTupList(map);
		model.addAttribute("tupList", list);
		return "discount/discount-add";
	}

	@OperationLog("查询优惠券列表")
	@GetMapping("/findPageWithResult")
	@ResponseBody
	@ApiOperation(value = "查询优惠券列表", notes = "查询优惠券列表")
	@ApiImplicitParam(name = "map", value = "参数实体", required = true, dataType = "map")
	public PageResultBean<Discount> findPageWithResult(HttpServletRequest request,
			@RequestParam Map<String, Object> map) {
		List<Discount> list = discountService.findPageWithResult(map);
		PageInfo<Discount> discountPageInfo = new PageInfo<>(list);
		return new PageResultBean<>(discountPageInfo.getTotal(), discountPageInfo.getList());

	}

	@OperationLog("新增优惠券")
	@PostMapping
	@ResponseBody
	@ApiOperation(value = "新增优惠券", notes = "新增优惠券")
	@ApiImplicitParam(name = "Discount", value = "实体对象", required = false, dataType = "Discount")
	public ResultBean insert(Discount discount, String date) throws ParseException {
		if (!StringUtil.isEmpty(date)) {
			String[] dateArr = date.split(",");
			discount.setStarttime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateArr[0]));
			discount.setEndtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateArr[1]));
		}
		return ResultBean.success(discountService.insertDynamic(discount));
	}

	@GetMapping("/edit/{discountId}")
	@ApiIgnore
	public String update(@PathVariable("discountId") String discountId, Model model) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("label", "DISTYPE");
		List<Tup> list = productService.findTupList(map);
		model.addAttribute("tupList", list);
		model.addAttribute("discount", discountService.selectById(discountId));
		return "discount/discount-add";
	}

	@OperationLog("修改优惠券")
	@PutMapping
	@ResponseBody
	@ApiOperation(value = "修改优惠券", notes = "修改优惠券")
	@ApiImplicitParam(name = "Discount", value = "实体对象", required = false, dataType = "Discount")
	public ResultBean update(Discount discount, String date) throws ParseException {
		if (!StringUtil.isEmpty(date)) {
			String[] dateArr = date.split("-");
			discount.setStarttime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateArr[0].trim()));
			discount.setEndtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateArr[1].trim()));
		}
		return ResultBean.success(discountService.updateDynamic(discount));
	}

	@OperationLog("公众号优惠券列表")
	@PostMapping("/wx/findPageWithUserResult")
	@ResponseBody
	@ApiOperation(value = "公众号优惠券列表", notes = "公众号优惠券列表")
	@ApiImplicitParam(name = "map", value = "参数实体", required = true, dataType = "map")
	public PageResultBean<DiscountUser> findPageWithUserResult(HttpServletRequest request,
			@RequestParam Map<String, Object> map) {
		List<DiscountUser> list = discountUserService.findPageWithResult(map);
		PageInfo<DiscountUser> discountPageInfo = new PageInfo<>(list);
		return new PageResultBean<>(discountPageInfo.getTotal(), discountPageInfo.getList());
	}

	@OperationLog("我的优惠券列表")
	@PostMapping("/wx/findMyDiscountList")
	@ResponseBody
	@ApiOperation(value = "我的优惠券列表", notes = "我的优惠券列表")
	@ApiImplicitParam(name = "map", value = "参数实体", required = true, dataType = "map")
	public ResultBean findMyDiscountList(HttpServletRequest request, @RequestBody Map<String, Object> map) {
		String token = request.getHeader("authorization");
		String userId = JWT.decode(token).getKeyId();
		map.put("userid", userId);
		List<Discount> list = discountService.selectMyDiscountList(map);
		return ResultBean.success(list);
	}

	@OperationLog("可领取优惠券")
	@PostMapping("/wx/isReceiveDiscountList")
	@ResponseBody
	@ApiOperation(value = "可领取优惠券", notes = "可领取优惠券")
	@ApiImplicitParam(name = "map", value = "参数实体", required = true, dataType = "map")
	public ResultBean isReceiveDiscountList(HttpServletRequest request, @RequestBody Map<String, Object> map) {
		String token = request.getHeader("authorization");
		String userId = JWT.decode(token).getKeyId();
		map.put("userid", userId);
		List<Discount> list = discountService.selectIsReceiveDiscountList(map);
		return ResultBean.success(list);
	}

	@OperationLog("优惠券领取")
	@PostMapping("/wx/receive")
	@ResponseBody
	@ApiOperation(value = "优惠券领取", notes = "优惠券领取")
	@SecurityParameter(outEncode = true, inDecode = false)
	@Authorize(required = true)
	public ResultBean receive(HttpServletRequest request, @RequestParam String param) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		try {
			paramMap = AesEncryptUtil.transStringToMap(aesEncryptUtil.decrypt(param));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 从token中获取用户id
		String token = request.getHeader("authorization");
		String userId = JWT.decode(token).getKeyId();
		DiscountUser discountUser = new DiscountUser();
		discountUser.setUserid(Integer.valueOf(userId));
		/*
		 * Integer userid= (Integer) map.get("userid"); discountUser.setUserid(userid);
		 */
		String discountId = (String) paramMap.get("discountId");
		discountUser.setDiscountid(discountId);
		return discountUserService.receive(discountUser);
	}

	@OperationLog("下单可使用优惠劵列表")
	@PostMapping("/wx/filterCoupon")
	@ResponseBody
	@SecurityParameter(outEncode = true, inDecode = false)
	@Authorize(required = true)
	public ResultBean filterCoupon(HttpServletRequest request, @RequestParam String param) {
		// 从token中获取用户id
		String token = request.getHeader("authorization");
		String userId = JWT.decode(token).getKeyId();
		String data;
		try {
			data = aesEncryptUtil.desEncrypt(param);
			Map map = aesEncryptUtil.transStringToMap(data);
			String res = JSON.toJSON(map.get("list").toString()).toString();
			List<OrderProduct> list = JSONArray.parseArray(res, OrderProduct.class);

			return ResultBean.success(
					discountUserService.filterCoupon(list, userId, Double.parseDouble(map.get("total").toString())));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultBean.error("解析失败!");
	}
}
