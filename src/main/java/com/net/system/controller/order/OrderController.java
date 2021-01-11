package com.net.system.controller.order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.net.common.util.MapToUtil;
import com.net.common.util.ResultBean;
import com.net.common.util.Signature;
import com.net.common.util.WxPayHttp;
import com.net.common.util.XmlUtil;
import com.net.system.controller.product.AESUtil;
import com.net.system.controller.product.SignUtil;
import com.net.system.mapper.product.KeyMapper;
import com.net.system.model.CardUser;
import com.net.system.model.Key;
import com.net.system.model.Order;
import com.net.system.model.OrderProduct;
import com.net.system.model.Product;
import com.net.system.model.Reservation;
import com.net.system.model.User;
import com.net.system.model.WriteOff;
import com.net.system.model.vo.CardUserVo;
import com.net.system.model.vo.WriteOffVo;
import com.net.system.service.cardpack.CardUserService;
import com.net.system.service.cardpack.WriteOffService;
import com.net.system.service.imple.order.OrderServiceImple;
import com.net.system.service.order.OrderService;
import com.net.system.service.product.ProductService;
import com.net.system.service.product.ReservationService;
import com.net.system.service.sysmange.UserService;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/order")
@Api(tags = "OrderController", description = "订单管理类")
public class OrderController {
	
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private WriteOffService writeOffService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private CardUserService cardUserService;
	
	@Autowired
	private KeyMapper keyMapper;
	private AESUtil aESUtil=new AESUtil();
	
	private Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	private AesEncryptUtil aesEncryptUtil=AesEncryptUtil.getAesEncryptUtil();
	
    @OperationLog("用户预约")
    @GetMapping("/wx/userReservation")
    @ResponseBody
    @ApiOperation(value = "用户预约", notes="用户预约")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=true,inDecode=true)
    @Authorize(required=true)
    public ResultBean userReservation(HttpServletRequest request,@RequestBody String param) {
    	Map<String,Object> paramMap=new HashMap<String, Object>();
    	paramMap=AesEncryptUtil.transStringToMap(param);
    	//获取预约产品的id
    	String productId=(String) paramMap.get("productId");
    	if(productId.isEmpty()) {
    		return ResultBean.error("产品id不能为空");
    	}
    	//获取预约时间
    	String date=(String) paramMap.get("date");
    	if(date.isEmpty()) {
    		return  ResultBean.error("预约时间不能为空");
    	}
    	String token = request.getHeader("authorization");
    	String openId = JWT.decode(token).getKeyId();
        // 根据openId 查询用户信息
        User user = userService.selectOneByOpenId(openId);
        Reservation reservation=new Reservation();
        reservation.setProductid(productId);
        reservation.setReservationuser(user.getUserId());
        try {
			reservation.setReservationtime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
			reservationService.insertDynamic(reservation);
		} catch (ParseException e) {
			return ResultBean.error("预约失败");
		}
    	return ResultBean.success();
    }
	
    
    @OperationLog("用户下单")
    @GetMapping("/wx/userOrder")
    @ResponseBody
    @ApiOperation(value = "用户下单", notes="用户下单")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = false, dataType = "String")
    @SecurityParameter(outEncode=false,inDecode=false)
    @Authorize(required=true)
    public ResultBean userOrder(HttpServletRequest request,@RequestParam String param) {
    	Map<String,Object> paramMap=new HashMap<String, Object>();
    	try {
			paramMap=AesEncryptUtil.transStringToMap(aesEncryptUtil.decrypt(param));
			String token = request.getHeader("authorization");
	    	String userId = JWT.decode(token).getKeyId();
	    	paramMap.put("userId", userId);
			return orderService.OrderPay(paramMap,request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    @OperationLog("取消订单")
    @PostMapping("/wx/cancelOrder")
    @ResponseBody
    @ApiOperation(value = "取消订单", notes="取消订单")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = false, dataType = "String")
    @SecurityParameter(outEncode=false,inDecode=false)
    @Authorize(required=true)
    public ResultBean cancelOrder(HttpServletRequest request,@RequestParam String param) {
    		Map<String,Object> paramMap=new HashMap<String, Object>();
    	try {
			paramMap=AesEncryptUtil.transStringToMap(aesEncryptUtil.decrypt(param));
	    	paramMap.put("endTime", new Date());
	    	paramMap.put("closeTime", new Date());
			return ResultBean.success(orderService.cancelOrder(paramMap));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResultBean.error("服务器繁忙!!");
    }
	
    @OperationLog("微信支付回调")
    @RequestMapping("/wx/notifyResultCoupon")
    @ResponseBody
    @ApiOperation(value = "微信支付回调", notes="微信支付回调")
    @SecurityParameter(outEncode=false,inDecode=false)
    @Authorize(required=false)
    public void notifyResultCoupon(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
					List<OrderProduct> productList=order.getProductList();
					for (OrderProduct orderProduct : productList) {
						Product product=productService.selectById(orderProduct.getProId());
						if(product.getType().equals("CARD")) {
							CardUser cardUser=new CardUser();
							cardUser.setUserid(order.getMemberId());
							cardUser.setProductid(product.getId());
							CardUser cardUsers=cardUserService.selectByUserCard(cardUser);
							if(cardUsers!=null) {
								Integer day=orderProduct.getQuantity()*365;
								cardUsers.setEndtime(DateUtils.getRearDate(day));//截止时间
							}else {
								cardUser.setAddtime(new Date());
								cardUser.setEndtime(DateUtils.getRearDate(365));//截止时间
								cardUser.setOrderNo(order.getSerialNumber());
								cardUser.setStatus(1);
								cardUserService.insertDynamic(cardUser);
							}
						}
					}
					/**
					String[] couponIds = order.getCouponId().split(",");
					for (int i = 0; i < couponIds.length; i++) {
						Map<String, Object> par = new HashMap<String, Object>();
						par.put("releaseId", couponIds[i]);
						par.put("memberId", memberId);
						// 将优惠劵绑定用户
						//couponInfoService.receiveCoupon(par);
					}**/

				}
			}
			// 如果订单修改成功,通知微信接口不要在回调这个接口
			WxPayHttp.responseXmlSuccess(response);
		}
    
    
    @OperationLog("数据核销")
    @PostMapping("/wx/dataVerification")
    @ResponseBody
    @ApiOperation(value = "数据核销", notes="数据核销")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=false,inDecode=false)
    @Authorize(required=false)
    public Map<String,Object> dataVerification(HttpServletRequest request,@RequestBody Map<String,Object> map) {
    	    Map<String,Object> result=new HashMap<String, Object>();
    	try {
    		map=MapToUtil.sortMapByKey(map);
    		//渠道号
            String channelId=(String) map.get("channelId");
    		//签名
            String sign=(String) map.get("sign");
            StringBuffer str=new StringBuffer();
            for (String key : map.keySet()) {
            	if(!key.equals("sign")) {
            		str.append(key+"="+map.get(key)+"&");
            	}
            }
            String syrs=str.substring(0, str.length()-1);
            Key key=keyMapper.selectById(channelId);
            if(!SignUtil.verifySign(syrs, sign,key.getPublickey())) {
            	result.put("retCode", -1);
            	result.put("retMsg", "签名校验失败");
            	return result;
            }
    		
            //获取流水号
            String reqId=(String) map.get("reqId");
            //订单号
            String orderNo=(String) map.get("orderNo");
            //景点编号
            String rightDtlCode=(String) map.get("rightDtlCode");
            //证件类型
            String idType=(String) map.get("idType");
            if(idType!=null&&!idType.isEmpty()) {
            	if(!idType.equals("0")&&!idType.equals("00")) {
                	result.put("retCode", -1);
                	result.put("retMsg", "证件类型需为0！");
                	return result;
                }
            }else {
            	idType="00";
            }
            
            //证件号码
            String idCard=(String) map.get("idCard");
            //景点名称
            String rightDtlName=(String) map.get("rightDtlName");
            //核销时间
            String checkTime=(String) map.get("checkTime");
            //核销方式
            String checkType=(String) map.get("checkType");
            if(checkType==null||checkType.isEmpty()) {
            	checkType="0";
            }
            //机具类型
            String machineType=(String) map.get("machineType");
            if(machineType==null||machineType.isEmpty()) {
            	machineType="0";
            }
            //入园方式
            String voucherType=(String) map.get("voucherType");
            if(voucherType==null||voucherType.isEmpty()) {
            	voucherType="1";
            }
            //核销记录编号
            String checkNo=(String) map.get("checkNo");
            
            //查询该核销记录是否已经存在
            WriteOff writeOff=writeOffService.selectById(map);
            if(writeOff!=null) {
            	result.put("retCode", -1);
                result.put("retMsg", "此条记录已核销过！");
                return result;
            }
            //通过身份证号查询用户信息
            User user=userService.selectUserByIdCard(aESUtil.decrypt(idCard));
            if(user==null) {
            	result.put("retCode", -1);
                result.put("retMsg", "请确定该用户是否已经购买激活过卡");
                return result;
            }
            //通过景点编号查询景点
            Product product=productService.selectByRightDtlCode(rightDtlCode);
            if(product==null) {
            	result.put("retCode", -1);
                result.put("retMsg", "景点编号错误！");
                return result;
            }
            WriteOff writeOffs=new WriteOff();
            writeOffs.setChannelid(channelId);
            writeOffs.setCheckno(checkNo);
            writeOffs.setChecktime(DateUtils.yyyyzDate(checkTime));
            writeOffs.setChecktype(checkType);
            writeOffs.setMachinetype(machineType);
            writeOffs.setReqid(reqId);
            writeOffs.setRightcode(rightDtlCode);
            writeOffs.setRightname(rightDtlName);
            writeOffs.setVouchertype(voucherType);
            writeOffs.setUserid(user.getUserId());
            writeOffs.setOrderNo(orderNo);
            writeOffService.insertDynamic(writeOffs);
            result.put("retCode", "00");
            result.put("retMsg", "核销成功");
    	}catch(Exception e) {
    		result.put("retCode", -1);
            result.put("retMsg", "核销失败");
    	}
		return result;
    }
    
    
    @OperationLog("查询核销数据")
    @PostMapping("/wx/findVerificationData")
    @ResponseBody
    @ApiOperation(value = "查询核销数据", notes="查询核销数据")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=false,inDecode=false)
    @Authorize(required=false)
    public Map<String,Object> findVerificationData(HttpServletRequest request,@RequestBody Map<String,Object> map) {
    	    Map<String,Object> result=new HashMap<String, Object>();
    	try {
    		map=MapToUtil.sortMapByKey(map);
    		//渠道号
            String channelId=(String) map.get("channelId");
    		//签名
            String sign=(String) map.get("sign");
            StringBuffer str=new StringBuffer();
            for (String key : map.keySet()) {
            	if(!key.equals("sign")) {
            		str.append(key+"="+map.get(key)+"&");
            	}
            }
            String syrs=str.substring(0, str.length()-1);
            Key key=keyMapper.selectById(channelId);
            if(!SignUtil.verifySign(syrs, sign,key.getPublickey())) {
            	result.put("retCode", -1);
            	result.put("retMsg", "签名校验失败");
            	return result;
            }
            //订单号
            String orderNo=(String) map.get("orderNo");
            //证件类型
            String idType=(String) map.get("idType");
            if(!idType.equals("0")&&!idType.equals("00")) {
            	result.put("retCode", -1);
            	result.put("retMsg", "证件类型需为0！");
            	return result;
            }
            //证件号码
            String idCard=(String) map.get("idCard");
            //通过身份证号查询用户信息
            User user=userService.selectUserByIdCard(aESUtil.decrypt(idCard));
            if(user==null) {
            	result.put("retCode", -1);
                result.put("retMsg", "没有该用户！");
                return result;
            }
            WriteOff writeOff=new WriteOff();
            writeOff.setChannelid(channelId);
            writeOff.setOrderNo(orderNo);
            List<WriteOffVo> list=writeOffService.findWithResult(writeOff);
            result.put("retCode", "00");
            result.put("retMsg", "查询成功");
            result.put("data", list);
    	}catch(Exception e) {
    		result.put("retCode", -1);
            result.put("retMsg", "查询失败");
            result.put("data", null);
    	}
		return result;
    }
    
    
    @OperationLog("查询激活数据")
    @PostMapping("/wx/findActivationData")
    @ResponseBody
    @ApiOperation(value = "查询激活数据", notes="查询激活数据")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=false,inDecode=false)
    @Authorize(required=false)
    public Map<String,Object> findActivationData(HttpServletRequest request,@RequestBody Map<String,Object> map) {
    	    Map<String,Object> result=new HashMap<String, Object>();
    	try {
    		map=MapToUtil.sortMapByKey(map);
    		//渠道号
            String channelId=(String) map.get("channelId");
            if(!channelId.equals("test")) {
            	result.put("retCode", -1);
            	result.put("retMsg", "测试渠道号需为test");
            	return result;
            }
    		//签名
            String sign=(String) map.get("sign");
            StringBuffer str=new StringBuffer();
            for (String key : map.keySet()) {
            	if(!key.equals("sign")) {
            		str.append(key+"="+map.get(key)+"&");
            	}
            }
            String syrs=str.substring(0, str.length()-1);
            Key key=keyMapper.selectById(channelId);
            if(!SignUtil.verifySign(syrs, sign,key.getPublickey())) {
            	result.put("retCode", -1);
            	result.put("retMsg", "签名校验失败");
            	return result;
            }
    		
            //证件类型
            String idType=(String) map.get("idType");
            if(!idType.equals("0")&&!idType.equals("00")) {
            	result.put("retCode", -1);
            	result.put("retMsg", "证件类型需为0！");
            	return result;
            }
            //证件号码
            String idCard=(String) map.get("idCard");
            //通过身份证号查询用户信息
            User user=userService.selectUserByIdCard(aESUtil.decrypt(idCard));
            if(user==null) {
            	result.put("retCode", -1);
                result.put("retMsg", "没有该用户！");
                return result;
            }
            List<CardUserVo> list=cardUserService.selectByUserCardVo(aESUtil.decrypt(idCard));
            for (CardUserVo cardUserVo : list) {
            	cardUserVo.setIdCard(aESUtil.encrypt(cardUserVo.getIdCard()));
            	cardUserVo.setPhoneNo(aESUtil.encrypt(cardUserVo.getPhoneNo()));
			}
            result.put("retCode", "00");
            result.put("retMsg", "查询成功");
            result.put("data", list);
    	}catch(Exception e) {
    		result.put("retCode", -1);
            result.put("retMsg", "查询成功");
            result.put("data", null);
    	}
		return result;
    }
    
    @OperationLog("订单列表")
	@PostMapping("/wx/orderList")
	@ResponseBody
	public List<Order> findOrderListByMember_id(HttpServletRequest request,@RequestBody Map<String,Object> map){
		//获取用户编号
    	String memberId= map.get("memberId").toString();

    	return orderService.selectByMemberId(memberId);
	}
    
    
    
   
}
