package com.net.system.controller.cardpack;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.github.pagehelper.PageInfo;
import com.net.common.annotation.OperationLog;
import com.net.common.config.WxSPConfig;
import com.net.common.interceptor.Authorize;
import com.net.common.interceptor.SecurityParameter;
import com.net.common.util.AccountTypeBasic;
import com.net.common.util.AesEncryptUtil;
import com.net.common.util.DateUtils;
import com.net.common.util.Des3Utils;
import com.net.common.util.HttpUtil;
import com.net.common.util.PageResultBean;
import com.net.common.util.ResultBean;
import com.net.common.util.Signature;
import com.net.common.util.WxPayHttp;
import com.net.common.util.XmlUtil;
import com.net.system.controller.order.OrderController;
import com.net.system.model.CardUser;
import com.net.system.model.DiscountUser;
import com.net.system.model.Order;
import com.net.system.model.OrderProduct;
import com.net.system.model.Product;
import com.net.system.model.User;
import com.net.system.model.mall.RechargeRecord;
import com.net.system.model.vo.AccountType;
import com.net.system.model.vo.ConsumptionVo;
import com.net.system.service.cardpack.RechargeRecordService;
import com.net.system.service.sysmange.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/canteen")
@Api(tags = "CanteenController", description = "食堂接口管理类")
public class CanteenController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RechargeRecordService rechargeRecordService;
	
	private AesEncryptUtil aesEncryptUtil=AesEncryptUtil.getAesEncryptUtil();
	
	private Logger logger = LoggerFactory.getLogger(CanteenController.class);
	
    @OperationLog("查询食堂账户类型")
    @PostMapping("/wx/accledgerQuery")
    @ResponseBody
    @ApiOperation(value = "查询食堂账户类型", notes="查询食堂账户类型")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=false,inDecode=false)
    @Authorize(required=true)
    public ResultBean accledgerQuery(HttpServletRequest request) {
    	try {
    		Map<String,Object> map=new LinkedHashMap<String, Object>();
    		map.put("reqcode", AccountTypeBasic.ACCOUNTTYPE);
    		map.put("oper_id", AccountTypeBasic.OPER_ID);
    		map=Des3Utils.sing(map);
    		map.put("key", "reqcode,oper_id");
    		System.out.println("签名结果："+map);
    		String param=HttpUtil.doPostJson(AccountTypeBasic.CANTEEN_URL, JSONObject.toJSONString(map));
    		JSONObject json=JSONObject.parseObject(param);
    		List<AccountType> accountType=JSONArray.parseArray(json.getString("data"), AccountType.class);
    		for (AccountType accountType2 : accountType) {
    			System.out.println(accountType2);
    		}
        	return ResultBean.success(accountType);
    	}catch(Exception e) {
    		return ResultBean.error("系统繁忙！请稍后再试");
    	}
    	
    }
    
    
    @OperationLog("查询是否开通食堂账户")
    @PostMapping("/wx/accledgerOpen")
    @ResponseBody
    @ApiOperation(value = "查询是否开通食堂账户", notes="查询是否开通食堂账户")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=false,inDecode=false)
    @Authorize(required=true)
    public ResultBean accledgerOpen(HttpServletRequest request,@RequestParam String param) {
    	try {
    		String token = request.getHeader("authorization");
        	String userId = JWT.decode(token).getKeyId();
        	User user=userService.selectOne(Integer.parseInt(userId));
    		if(user.getIdCard()==null||user.getIdCard().isEmpty()) {
    			return ResultBean.error("请先完善个人信息！");
    		}
    		Map<String,Object> paramMap=new HashMap<String, Object>();
        	paramMap=AesEncryptUtil.transStringToMap(aesEncryptUtil.decrypt(param));
        	Map<String,Object> map=new LinkedHashMap<String, Object>();
    		map.put("reqcode", AccountTypeBasic.ACCOUNTOPENING);
    		map.put("cert_no", user.getIdCard());
    		map.put("oper_id", AccountTypeBasic.OPER_ID);
    		map=Des3Utils.sing(map);
    		map.put("key", "reqcode,cert_no,oper_id");
    		System.out.println("签名结果："+map);
    		String params=HttpUtil.doPostJson(AccountTypeBasic.CANTEEN_URL, JSONObject.toJSONString(map));
    		JSONObject json=JSONObject.parseObject(params);
    		if(json.get("result").equals("0")) {
    			return ResultBean.success(json.get("acc_kind"));
    		}else {
    			return ResultBean.error(json.getString("msg"));
    		}
    	}catch(Exception e) {
    		return ResultBean.error("系统繁忙！请稍后再试");
    	}
    	
    }
    @OperationLog("用户充值下单")
    @RequestMapping("/wx/userOrder")
    @ResponseBody
    @ApiOperation(value = "用户充值下单", notes="用户充值下单")
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
			return rechargeRecordService.RechargePay(paramMap,request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
			RechargeRecord rechargeRecord=rechargeRecordService.selectByOrderNo(paramsToMap.get("out_trade_no").toString());
			System.out.println("------------------------------------------------------:"+rechargeRecord);
				logger.info("微信回调订单实体===>" + rechargeRecord.toString());
				// 判断是否是未支付状态
				if (rechargeRecord.getStatus() == 1) {
					// 更新订单状态
					rechargeRecord.setStatus(2);
					rechargeRecord.setPaytype(1);
					//1.微信支付
					rechargeRecord.setPaytime(DateUtils.dateNumberToDateStr(paramsToMap.get("time_end").toString()));
					Object jsonObj = JSONObject.toJSON(paramsToMap);
					//修改用户账户余额
					User user=new User();
					user.setBalance(rechargeRecord.getRechargeName());
					user.setUserId(rechargeRecord.getUserid());
					// 调用更新订单状态接口
					rechargeRecordService.updateOrderStatus(rechargeRecord);
					userService.updateByPrimaryKeySelective(user);
					User u=userService.selectOne(user.getUserId());
					//调用充值接口
					ResultBean resultBean=accledgerRecharge(u.getIdCard(),rechargeRecord.getAcckind(),String.valueOf(rechargeRecord.getRechargeName()),"2");
					rechargeRecord.setNotifyResult(resultBean.getMsg());
					
				}
			}
			// 如果订单修改成功,通知微信接口不要在回调这个接口
			WxPayHttp.responseXmlSuccess(response);
	}
    
    
    @OperationLog("获取用户充值记录列表")
    @PostMapping("/wx/findRechargeRecordList")
    @ResponseBody
    @ApiOperation(value = "获取用户充值记录列表", notes="获取用户充值记录列表")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=true,inDecode=false)
    @Authorize(required=true)
    public PageResultBean<RechargeRecord> findRechargeRecordList(HttpServletRequest request,@RequestParam String param) {
    	Map<String,Object> paramMap=new HashMap<String, Object>();
    	try {
			paramMap=AesEncryptUtil.transStringToMap(aesEncryptUtil.decrypt(param));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	String token = request.getHeader("authorization");
    	String userId = JWT.decode(token).getKeyId();
    	paramMap.put("userid", userId);
    	List<RechargeRecord> list= rechargeRecordService.findPageWithResult(paramMap);
    	PageInfo<RechargeRecord> rechargeRecordPageInfo = new PageInfo<>(list);
    	return new PageResultBean<>(rechargeRecordPageInfo.getTotal(), rechargeRecordPageInfo.getList());
    }
    
    
    /**
         * 账户充值
     * @param accledgeUser
     * @return
     */
    public ResultBean accledgerRecharge(String idCard,String accKind,String amt,String paySource) {
    	Map<String,Object> map=new LinkedHashMap<String, Object>();
		map.put("reqcode", AccountTypeBasic.RECHARGE);
		map.put("cert_no", idCard);
		map.put("oper_id", AccountTypeBasic.OPER_ID);
		map.put("amt", amt);
		map.put("paySource", paySource);
		map.put("acc_kind", accKind);
		map=Des3Utils.sing(map);
		map.put("key", "reqcode,cert_no,oper_id,amt,paySource,acc_kind");
		System.out.println("签名结果："+map);
		//String param=HttpUtil.doPostJson("http://58.222.216.146:10003/json/JSONServlet", JSONObject.toJSONString(map));
		String param=HttpUtil.doPostJson(AccountTypeBasic.CANTEEN_URL, JSONObject.toJSONString(map));
		JSONObject json=JSONObject.parseObject(param);
		return ResultBean.success(json.get("msg").toString());
    }
    
    
    @OperationLog("获取用户信息")
    @PostMapping("/wx/getUserInfo")
    @ResponseBody
    @ApiOperation(value = "获取用户信息", notes="获取用户信息")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=true,inDecode=false)
    @Authorize(required=true)
    public ResultBean getUserInfo(HttpServletRequest request) {
    	String token = request.getHeader("authorization");
    	String userId = JWT.decode(token).getKeyId();
    	User user=userService.selectOne(Integer.parseInt(userId));
    	if(user.getIdCard()!=null&&!user.getIdCard().isEmpty()) {
        	 user.setIdCard(user.getIdCard().substring(0, 6)+"*****"+user.getIdCard().substring(14, 18)); 
        }
    	return ResultBean.success(user);
    }
    
    
    @OperationLog("查询用户余额")
    @PostMapping("/wx/findUserBalance")
    @ResponseBody
    @ApiOperation(value = "查询用户余额", notes="查询用户余额")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=true,inDecode=false)
    @Authorize(required=true)
    public ResultBean findUserBalance(HttpServletRequest request) {
    	String token = request.getHeader("authorization");
    	String userId = JWT.decode(token).getKeyId();
    	User user=userService.selectOne(Integer.parseInt(userId));
    	Map<String,Object> map=new LinkedHashMap<String, Object>();
		map.put("reqcode", AccountTypeBasic.ACCOUNTOPENING);
		map.put("cert_no", user.getIdCard());
		map.put("oper_id", AccountTypeBasic.OPER_ID);
		map=Des3Utils.sing(map);
		map.put("key", "reqcode,cert_no,oper_id");
		String param=HttpUtil.doPostJson(AccountTypeBasic.CANTEEN_URL, JSONObject.toJSONString(map));
		System.out.println(param);
		JSONObject json=JSONObject.parseObject(param);
		if(json.get("result").equals("0")) {
			String balanceStr=json.getString("balance");
			BigDecimal balance=new BigDecimal(balanceStr);
			user.setBalance(balance);
			//修改余额
			userService.updateUserBalance(user);
		}
		User u=userService.selectOne(user.getUserId());
		if(u.getIdCard()!=null&&!u.getIdCard().isEmpty()) {
       	   u.setIdCard(u.getIdCard().substring(0, 6)+"*****"+u.getIdCard().substring(14, 18)); 
         }
    	return ResultBean.success(u);
    }
    @OperationLog("查询用户消费记录")
    @PostMapping("/wx/findUserConsumption")
    @ResponseBody
    @ApiOperation(value = "查询用户消费记录", notes="查询用户消费记录")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=true,inDecode=false)
    @Authorize(required=true)
    public ResultBean findUserConsumption(HttpServletRequest request,@RequestParam String param) {
    	Map<String,Object> paramMap=new HashMap<String, Object>();
    	try {
			paramMap=AesEncryptUtil.transStringToMap(aesEncryptUtil.decrypt(param));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResultBean.error("系统繁忙!");
		}
    	String date=(String) paramMap.get("date");
    	String token = request.getHeader("authorization");
    	String userId = JWT.decode(token).getKeyId();
    	User user=userService.selectOne(Integer.parseInt(userId));
    	Map<String,Object> map=new LinkedHashMap<String, Object>();
		map.put("reqcode", AccountTypeBasic.ACCOUNTQUERY_ACOUNT);
		map.put("cert_no", user.getIdCard());
		map.put("oper_id", AccountTypeBasic.OPER_ID);
		map.put("beginDate", date);
		map.put("endDate", date);
		map=Des3Utils.sing(map);
		map.put("key", "reqcode,cert_no,oper_id,beginDate,endDate");
		String params=HttpUtil.doPostJson(AccountTypeBasic.CANTEEN_URL, JSONObject.toJSONString(map));
		JSONObject json=JSONObject.parseObject(params);
		String result=(String) json.get("result");
		List<ConsumptionVo> consumption=new ArrayList<ConsumptionVo>();
		if(result.equals("0")) {
			 consumption=JSONObject.parseArray(json.get("info").toString(), ConsumptionVo.class);
		}
		return ResultBean.success(consumption);
    }
    
    public static void main(String[] args) {
    	Map<String,Object> map=new LinkedHashMap<String, Object>();
		map.put("reqcode", AccountTypeBasic.ACCOUNTQUERY_ACOUNT);
		map.put("cert_no", "321028197809296648");
		map.put("oper_id", AccountTypeBasic.OPER_ID);
		map.put("beginDate", "2020-12-03");
		map.put("endDate", "2020-12-03");
		map=Des3Utils.sing(map);
		map.put("key", "reqcode,cert_no,oper_id,beginDate,endDate");
		System.out.println("签名结果："+map);
		//String param=HttpUtil.doPostJson("http://58.222.216.146:10003/json/JSONServlet", JSONObject.toJSONString(map));
		String param=HttpUtil.doPostJson(AccountTypeBasic.CANTEEN_URL, JSONObject.toJSONString(map));
		JSONObject json=JSONObject.parseObject(param);
		String result=(String) json.get("result");
		List<ConsumptionVo> consumption=new ArrayList<ConsumptionVo>();
		if(result.equals("0")) {
			 consumption=JSONObject.parseArray(json.get("info").toString(), ConsumptionVo.class);
		}
		System.out.println(consumption.size());
    }
}
