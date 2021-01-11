package com.net.system.controller.cardpack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.github.pagehelper.PageInfo;
import com.net.common.annotation.OperationLog;
import com.net.common.config.WxSPConfig;
import com.net.common.interceptor.Authorize;
import com.net.common.interceptor.SecurityParameter;
import com.net.common.util.AesEncryptUtil;
import com.net.common.util.DateUtils;
import com.net.common.util.HttpClientUtil;
import com.net.common.util.MapToUtil;
import com.net.common.util.PageResultBean;
import com.net.common.util.ResultBean;
import com.net.system.controller.product.AESUtil;
import com.net.system.controller.product.SignUtil;
import com.net.system.mapper.product.KeyMapper;
import com.net.system.model.CardUser;
import com.net.system.model.DiscountUser;
import com.net.system.model.Key;
import com.net.system.model.Order;
import com.net.system.model.Product;
import com.net.system.model.Reservation;
import com.net.system.model.User;
import com.net.system.model.WriteOff;
import com.net.system.service.cardpack.CardUserService;
import com.net.system.service.cardpack.WriteOffService;
import com.net.system.service.order.OrderService;
import com.net.system.service.product.DiscountUserService;
import com.net.system.service.product.ProductService;
import com.net.system.service.product.ReservationService;
import com.net.system.service.sysmange.UserService;

import cn.hutool.core.lang.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/cardUser")
@Api(tags = "CardUserController", description = "用户卡包管理类")
public class CardUserController {
	
	@Autowired
	private CardUserService cardUserService;
	
	@Autowired
	private ReservationService reservationService;
	
	@Autowired
	private DiscountUserService discountUserService;

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private WriteOffService writeOffService;
	
	@Autowired
	private KeyMapper keyMapper;
	
	private AESUtil aESUtil=new AESUtil();
	
	private AesEncryptUtil aesEncryptUtil=AesEncryptUtil.getAesEncryptUtil();
	
    @OperationLog("查询产品列表")
    @RequestMapping("/wx/findProductList")
    @ResponseBody
    @ApiOperation(value = "查询产品列表", notes="查询产品列表")
    @ApiImplicitParam(name = "map", value = "参数实体",  required = true, dataType = "map")
    public PageResultBean<Product> findProductList(HttpServletRequest request,@RequestParam Map<String,Object> map) {
    	List<Product> list= productService.findPageWithResult(map);
    	PageInfo<Product> productPageInfo = new PageInfo<>(list);
    	return new PageResultBean<>(productPageInfo.getTotal(), productPageInfo.getList());
    }
	
    @OperationLog("获取用户卡包列表")
    @PostMapping("/wx/findPageWithCardResult")
    @ResponseBody
    @ApiOperation(value = "获取用户卡包列表", notes="获取用户卡包列表")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=true,inDecode=false)
    @Authorize(required=true)
    public PageResultBean<CardUser> findPageWithCardResult(HttpServletRequest request,@RequestParam String param) throws Exception {
    	Map<String,Object> paramMap=new HashMap<String, Object>();
    	paramMap=AesEncryptUtil.transStringToMap(aesEncryptUtil.decrypt(param));
    	String token = request.getHeader("authorization");
    	String userId = JWT.decode(token).getKeyId();
    	paramMap.put("userid", userId);
    	List<CardUser> list= cardUserService.findPageWithResult(paramMap);
    	PageInfo<CardUser> cardUserPageInfo = new PageInfo<>(list);
    	return new PageResultBean<>(cardUserPageInfo.getTotal(), cardUserPageInfo.getList());
    }
    
    @OperationLog("获取用户预约列表")
    @PostMapping("/wx/findReservationList")
    @ResponseBody
    @ApiOperation(value = "获取用户预约列表", notes="获取用户预约列表")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=true,inDecode=false)
    @Authorize(required=true)
    public PageResultBean<Reservation> findReservationList(HttpServletRequest request,@RequestParam  String param) throws Exception {
    	Map<String,Object> paramMap=new HashMap<String, Object>();
    	paramMap=AesEncryptUtil.transStringToMap(aesEncryptUtil.decrypt(param));
    	List<Reservation> list= reservationService.findPageWithResult(paramMap);
    	PageInfo<Reservation> reservationPageInfo = new PageInfo<>(list);
    	return new PageResultBean<>(reservationPageInfo.getTotal(), reservationPageInfo.getList());
    }
    
    
    @OperationLog("获取用户优惠券列表")
    @PostMapping("/wx/findDiscountUserList")
    @ResponseBody
    @ApiOperation(value = "获取用户优惠券列表", notes="获取用户优惠券列表")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=true,inDecode=false)
    @Authorize(required=true)
    public PageResultBean<DiscountUser> findDiscountUserList(HttpServletRequest request,@RequestParam String param) {
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
    	paramMap.put("userId", userId);
    	List<DiscountUser> list= discountUserService.findPageWithResult(paramMap);
    	PageInfo<DiscountUser> discountUserPageInfo = new PageInfo<>(list);
    	return new PageResultBean<>(discountUserPageInfo.getTotal(), discountUserPageInfo.getList());
    }
    
    @OperationLog("获取用户订单列表")
    @PostMapping("/wx/findOrderList")
    @ResponseBody
    @ApiOperation(value = "获取用户订单列表", notes="获取用户订单列表")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=true,inDecode=false)
    @Authorize(required=true)
    public PageResultBean<Order> findOrderList(HttpServletRequest request,@RequestParam String param) throws Exception {
    	Map<String,Object> paramMap=new HashMap<String, Object>();
    	paramMap=AesEncryptUtil.transStringToMap(aesEncryptUtil.decrypt(param));
    	String token = request.getHeader("authorization");
    	String userId = JWT.decode(token).getKeyId();
    	paramMap.put("memberId", userId);
    	List<Order> list= orderService.findPageWithResult(paramMap);
    	PageInfo<Order> orderPageInfo = new PageInfo<>(list);
    	return new PageResultBean<>(orderPageInfo.getTotal(), orderPageInfo.getList());
    }
    
    @OperationLog("用户预约")
    @PostMapping("/wx/userReservation")
    @ResponseBody
    @ApiOperation(value = "用户预约", notes="用户预约")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=true,inDecode=false)
    @Authorize(required=true)
    public ResultBean userReservation(HttpServletRequest request,@RequestParam String param) {
    	System.out.println(param);
    	Map<String,Object> paramMap=new HashMap<String, Object>();
    	try {
			paramMap=AesEncryptUtil.transStringToMap(aesEncryptUtil.decrypt(param));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
    	paramMap.put("userId", openId);
        // 根据openId 查询用户信息
        User user = userService.selectOne(Integer.parseInt(openId));
        Reservation reservation=new Reservation();
        reservation.setProductid(productId);
        reservation.setReservationuser(user.getUserId());
    	//查询是否购买了旅游卡
        Integer sum=cardUserService.selectIsCard(paramMap);
        if(sum<1) {
        	return ResultBean.error("请先购买旅游卡再进行预约！");
        }
        //先查询该用户在该景点下是否还有未核销的
        Integer count=reservationService.findUserReservation(paramMap);
        if(count>0) {
        	return ResultBean.error("该景点有已预约的订单未核销！");
        }
        try {
			reservation.setReservationtime(new SimpleDateFormat("yyyy-MM-dd").parse(date));
			reservationService.insertDynamic(reservation);
		} catch (ParseException e) {
			return ResultBean.error("预约失败");
		}
    	return ResultBean.success();
    }
    
    
    @OperationLog("用户激活")
    @PostMapping("/wx/userActivation")
    @ResponseBody
    @ApiOperation(value = "用户激活", notes="用户激活")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=true,inDecode=false)
    @Authorize(required=true)
    public ResultBean userActivation(HttpServletRequest request,@RequestParam String param) throws Exception {
    	Map<String,Object> paramMap=new HashMap<String, Object>();
    	paramMap=AesEncryptUtil.transStringToMap(aesEncryptUtil.decrypt(param));
    	//获取要激活的产品的id
    	String id=(String) paramMap.get("id");
    	if(id.isEmpty()) {
    		return ResultBean.error("卡id不能为空");
    	}
    	//获取订单号
    	CardUser cardUser=cardUserService.selectById(id);
  //  	Order order=orderService.selectByOrderNo(cardUser.getOrderNo());
    	String token = request.getHeader("authorization");
    	String userId = JWT.decode(token).getKeyId();
        // 根据openId 查询用户信息
        User user = userService.selectOne(Integer.parseInt(userId));
        Product product=productService.selectById(cardUser.getProductid());
        Map<String,Object> map=new LinkedHashMap<String, Object>();
        Integer day=DateUtils.daysBetween(cardUser.getAddtime(), cardUser.getEndtime());
        map.put("activityDays", day);
        map.put("activityTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        map.put("cardType", product.getCardType());
        map.put("channelId",product.getChannelId());
        map.put("idCard",aESUtil.encrypt(user.getIdCard()));//身份证加密
        map.put("idType", "00");
        map.put("name", user.getUsername());
        map.put("orderNo", cardUser.getOrderNo());
        map.put("phoneNo", aESUtil.encrypt(user.getPhone()));//手机号加密
        map.put("userImgUrl", "http://www.baidu.com");
        StringBuffer str=new StringBuffer();
        for (String key : map.keySet()) {
        	str.append(key+"="+map.get(key)+"&");
         }
        String syrs=str.substring(0, str.length()-1);
        Key key=keyMapper.selectById(product.getChannelId());
        map.put("sign",SignUtil.sign256(syrs,key.getPrivatekey()));
        System.out.println(JSONObject.toJSONString(map));
        try {
        	 String params=HttpClientUtil.doPostJson("http://t3.htaonet.com/right/doThirdCreateRight", JSONObject.toJSONString(map));
        	 System.out.println(params);
             JSONObject json=JSONObject.parseObject(params);
             //激活成功
             if(json.get("retCode").equals("00")) {
             	CardUser cardUsers=new CardUser();
             	cardUsers.setId(cardUser.getId());
             	cardUsers.setEffecttime(new Date());
             	cardUsers.setStatus(2);
             	cardUserService.updateDynamic(cardUsers);
             	return ResultBean.success("激活成功！");
             }else {
             	return ResultBean.error(json.getString("retMsg"));
             }
        }catch(Exception e) {
        	return ResultBean.error("系统繁忙！请稍后再试..."); 
        }
       
    }
	
    
    @OperationLog("数据核销")
    @PostMapping("/wx/writeOff")
    @ResponseBody
    @ApiOperation(value = "数据核销", notes="数据核销")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=true,inDecode=false)
    @Authorize(required=true)
    public ResultBean writeOff(HttpServletRequest request,@RequestParam String param) throws Exception {
    	Map<String,Object> paramMap=new HashMap<String, Object>();
    	paramMap=AesEncryptUtil.transStringToMap(aesEncryptUtil.decrypt(param));
    	//获取要核销的预约景点id
    	String id=(String) paramMap.get("id");
    	if(id.isEmpty()) {
    		return ResultBean.error("id不能为空");
    	}
    	Reservation reservation=reservationService.selectById(id);
    	//获取订单号
    	Product product=productService.selectById(reservation.getProductid());
    	String token = request.getHeader("authorization");
    	String userId = JWT.decode(token).getKeyId();
    	CardUser cardUser=new CardUser();
    	cardUser.setUserid(Integer.parseInt(userId));
    	cardUser.setProductid(product.getSupId());
    	CardUser cardUserfind=cardUserService.selectByUserCard(cardUser);
        // 根据openId 查询用户信息
        User user = userService.selectOne(Integer.parseInt(userId));
        
        Product products=productService.selectById(product.getSupId());
        Map<String,Object> map=new LinkedHashMap<String, Object>();
        map.put("channelId", products.getChannelId());
        map.put("checkNo", cardUserfind.getOrderNo());//核销记录编号
        map.put("checkTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        map.put("checkType", "0");//核销方式
        map.put("idCard", aESUtil.encrypt(user.getIdCard()));//身份证加密
        map.put("idType", "00");//证件类型
        map.put("machineType", "0");//机具类型
        map.put("orderNo",cardUserfind.getOrderNo());//订单号
        map.put("reqId", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));//流水号
        map.put("rightDtlCode", product.getProductSn());//景点编号
        map.put("rightDtlName", product.getProductname());//景点名称
        map.put("voucherType", "1");//入园方式
        StringBuffer str=new StringBuffer();
        for (String key : map.keySet()) {
        	str.append(key+"="+map.get(key)+"&");
        }
        String syrs=str.substring(0, str.length()-1);
        Key key=keyMapper.selectById(product.getChannelId());
        map.put("sign",SignUtil.sign256(syrs,key.getPrivatekey()));
        System.out.println(JSONObject.toJSONString(map));
        String params=HttpClientUtil.doPostJson("http://t3.htaonet.com/right/doRecCheckInfo", JSONObject.toJSONString(map));
        JSONObject json=JSONObject.parseObject(params);
        //核销成功
        if(json.get("retCode").equals("00")) {
        	 WriteOff writeOffs=new WriteOff();
             writeOffs.setChannelid("test");
             writeOffs.setCheckno(cardUserfind.getOrderNo());
             writeOffs.setChecktime(new Date());
             writeOffs.setChecktype("0");
             writeOffs.setMachinetype("0");
             writeOffs.setReqid((String)map.get("reqId"));
             writeOffs.setRightcode(String.valueOf(product.getProductSn()));
             writeOffs.setRightname(product.getProductname());
             writeOffs.setVouchertype("1");
             writeOffs.setUserid(user.getUserId());
             writeOffs.setOrderNo(cardUserfind.getOrderNo());
             writeOffService.insertDynamic(writeOffs);
             reservation.setStatus(2);
             reservationService.updateDynamic(reservation);
        	 return ResultBean.success("核销成功！");
        }else {
        	return ResultBean.error(json.getString("retMsg"));
        }
    }
    
    @OperationLog("核销数据查询")
    @PostMapping("/wx/selectWriteOff")
    @ResponseBody
    @ApiOperation(value = "核销数据查询", notes="核销数据查询")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=true,inDecode=false)
    @Authorize(required=true)
    public ResultBean selectWriteOff(HttpServletRequest request,@RequestParam String param) throws Exception {
    	Map<String,Object> paramMap=new HashMap<String, Object>();
    	paramMap=AesEncryptUtil.transStringToMap(aesEncryptUtil.decrypt(param));
    	//获取要查询数据的旅游卡id
    	String id=(String) paramMap.get("id");
    	if(id.isEmpty()) {
    		return ResultBean.error("id不能为空");
    	}
    	String token = request.getHeader("authorization");
    	String userId = JWT.decode(token).getKeyId();
    	CardUser cardUserfind=cardUserService.selectById(id);
        // 根据openId 查询用户信息
        User user = userService.selectOne(Integer.parseInt(userId));
        Map<String,Object> map=new LinkedHashMap<String, Object>();
        map.put("channelId", cardUserfind.getProduct().getChannelId());
        map.put("idCard", aESUtil.encrypt(user.getIdCard()));//身份证加密
        map.put("idType", "00");//证件类型
        map.put("orderNo",cardUserfind.getOrderNo());//订单号
        StringBuffer str=new StringBuffer();
        for (String key : map.keySet()) {
        	str.append(key+"="+map.get(key)+"&");
        }
        String syrs=str.substring(0, str.length()-1);
        Key key=keyMapper.selectById(cardUserfind.getProduct().getChannelId());
        map.put("sign",SignUtil.sign256(syrs,key.getPrivatekey()));
        System.out.println(JSONObject.toJSONString(map));
        String params=HttpClientUtil.doPostJson("http://t3.htaonet.com/right/doQueryCheckInfos", JSONObject.toJSONString(map));
    	System.out.println(params);
        JSONObject json=JSONObject.parseObject(params);
        //核销成功
        if(json.get("retCode").equals("00")) {
        	 return ResultBean.success("核销成功！");
        }else {
        	return ResultBean.error(json.getString("retMsg"));
        }
    }
	
	public static String[] arraySort(String[] input){       
        for (int i=0;i<input.length-1;i++){
            for (int j=0;j<input.length-i-1;j++) {
            	if(input[j].compareTo(input[j+1])>0){
                    String temp=input[j];
                    input[j]=input[j+1];
                    input[j+1]=temp;
                }
            }
        }
        return input;
    }

    @OperationLog("完善用户信息")
    @PostMapping("/wx/perfectUserInfo")
    @ResponseBody
    @ApiOperation(value = "完善用户信息", notes="完善用户信息")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=true,inDecode=false)
    @Authorize(required=true)
    public ResultBean perfectUserInfo(HttpServletRequest request,@RequestParam String param) throws Exception {
    	Map<String,Object> paramMap=new HashMap<String, Object>();
    	paramMap=AesEncryptUtil.transStringToMap(aesEncryptUtil.decrypt(param));
    	//获取用户的身份证号
    	String idCard=(String) paramMap.get("idCard");
    	if(idCard.isEmpty()) {
    		return ResultBean.error("身份证号不能为空");
    	}
    	String username=(String) paramMap.get("username");
    	if(username.isEmpty()) {
    		return ResultBean.error("姓名不能为空");
    	}
    	String token = request.getHeader("authorization");
    	String userId = JWT.decode(token).getKeyId();
        User use=new User();
        use.setIdCard(idCard);
        use.setRealName(username);
        use.setUserId(Integer.parseInt(userId));
        userService.updateByPrimaryKeySelective(use);
        User user=userService.selectOne(Integer.parseInt(userId));
        if(user.getIdCard()!=null&&!user.getIdCard().isEmpty()) {
       	  user.setIdCard(user.getIdCard().substring(0, 6)+"*****"+user.getIdCard().substring(14, 18)); 
        }
        return ResultBean.success(user);
    }
    
    
    @OperationLog("跳转预约")
    @GetMapping("/wx/jumpReservation")
    @ResponseBody
    @ApiOperation(value = "跳转预约", notes="跳转预约")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=false,inDecode=false)
    @Authorize(required=true)
    public ResultBean jumpReservation(HttpServletRequest request,@RequestParam String param){
    	Map<String,Object> result=new HashMap<String, Object>();
    	try {
	    	Map<String,Object> paramMap=new HashMap<String, Object>();
	    	paramMap=AesEncryptUtil.transStringToMap(aesEncryptUtil.decrypt(param));
	    	//获取卡id
	    	String cardId=(String) paramMap.get("id");
	    	if(cardId.isEmpty()) {
	    		return ResultBean.error("系统繁忙，请稍后再试！");
	    	}
	    	CardUser cardUser=cardUserService.selectById(cardId);
	    	String token = request.getHeader("authorization");
	    	String userId = JWT.decode(token).getKeyId();
	        //根据用户id查询用户信息
	        User user = userService.selectOne(Integer.parseInt(userId));
	        Map<String,Object> map=new LinkedHashMap<String, Object>();
	        map.put("order_sn", cardUser.getOrderNo());
	        map.put("goods_type",cardUser.getProduct().getCardType());
	        map.put("card_no",user.getIdCard());//身份证加密
	        map.put("mobile",user.getPhone());
	        map.put("timestamp",new Date().getTime());
	        String syrs=JSONObject.toJSONString(map);
	        System.out.println("签名前："+syrs);
	        System.out.println("签名后："+aESUtil.encrypt(syrs));
	        result.put("secretToken", aESUtil.encrypt(syrs));
	        result.put("channelId", cardUser.getProduct().getChannelId());
    	}catch(Exception e) {
    		return ResultBean.error("系统繁忙，请稍后再试！");
    	}
		return ResultBean.success(result);
    }
    
    @OperationLog("接收激活订单数据")
    @PostMapping("/wx/getuserActivation")
    @ResponseBody
    @ApiOperation(value = "接收激活订单数据", notes="接收激活订单数据")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=false,inDecode=false)
    @Authorize(required=false)
    public Map<String,Object> getuserActivation(HttpServletRequest request,@RequestBody Map<String,Object> map) {
    	    Map<String,Object> result=new HashMap<String, Object>();
    	try {
    		map=MapToUtil.sortMapByKey(map);
    		//渠道号
            String channelId=(String) map.get("channelId");
            if(!channelId.equals("test")) {
            	result.put("retCode", -1);
            	result.put("retMsg", "渠道号错误！");
            	return result;
            }
            //获取订单号
            String orderNo=(String) map.get("orderNo");
            //姓名
            String name=(String) map.get("name");
            //手机号
            String phoneNo=(String) map.get("phoneNo");
            //证件类型
            String idType=(String) map.get("idType");
            //证件号码
            String idCard=(String) map.get("idCard");
            //卡类型
            String cardType=(String) map.get("cardType");
            //生效开始时间
            String activityTime=(String) map.get("activityTime");
            //有效天数
            String activityDays=(String) map.get("activityDays");
            
            String userImgUrl=(String) map.get("userImgUrl");
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
            //通过渠道号查询卡
            Product product=productService.selectBychannelId(channelId);
            if(product!=null&&!product.getCardType().equals(cardType)) {
            	result.put("retCode", -1);
            	result.put("retMsg", "卡类型错误");
            	return result;
            }
            //查询该订单号是否已经存在
            CardUser cardUsers=cardUserService.selectByOrderNo(orderNo);
            if(cardUsers!=null) {
            	result.put("retCode", -1);
                result.put("retMsg", "该订单号已存在！");
                return result;
            }
            CardUser cardUser=new CardUser();
            //通过身份证和手机号查询用户信息,如果不存在，则添加为新用户
            User user=userService.selectUserByPhone(aESUtil.decrypt(phoneNo));
            if(user!=null) {
            	cardUser.setUserid(user.getUserId());
            }else {
            	//添加为新用户
            	user=new User();
            	user.setPhone(aESUtil.decrypt(phoneNo));
            	user.setIdCard(aESUtil.decrypt(idCard));
            	user.setUsername(name);
            	user.setRealName(name);
            	user.setCreateTime(new Date());
            	user.setStatus("1");
            	user.setImageUrl(userImgUrl);
            	user.setPassword(aESUtil.decrypt(phoneNo));
            	userService.insertWeChatUser(user);
            	cardUser.setUserid(user.getUserId());
            }
            cardUser.setId(UUID.randomUUID().toString().replace("-", ""));
            cardUser.setOrderNo(orderNo);
            cardUser.setProductid(product.getId());
            cardUser.setStatus(2);
            cardUser.setEffecttime(DateUtils.yyyyzDate(activityTime));
            cardUser.setAddtime(new Date());
            cardUser.setEndtime(DateUtils.getRearDate(Integer.parseInt(activityDays)));
            cardUser.setDay(activityDays);
            cardUserService.insertDynamic(cardUser);
            result.put("retCode", "00");
            result.put("retMsg", "激活成功");
    	}catch(Exception e) {
    		result.put("retCode", -1);
            result.put("retMsg", "激活失败");
    	}
		return result;
    }
    
    
    @OperationLog("获取openId")
    @PostMapping("/wx/getuserOpenId")
    @ResponseBody
    @ApiOperation(value = "获取openId", notes="获取openId")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=false,inDecode=false)
    @Authorize(required=false)
    public ResultBean getuserOpenId(HttpServletRequest request,@RequestParam String code) {
    	String token = request.getHeader("authorization");
    	String userId = JWT.decode(token).getKeyId();
    	String params=HttpClientUtil.doGet("https://api.weixin.qq.com/sns/oauth2/access_token?appid="+WxSPConfig.APP_ID_PUBLIC+"&secret="+WxSPConfig.SECRET_PUBLIC+"&code="+code+"&grant_type=authorization_code");
    	JSONObject json=JSONObject.parseObject(params);
    	System.out.println(params);
    	User user=new User();
    	user.setUserId(Integer.parseInt(userId));
    	user.setOpenId(json.getString("openid"));
    	userService.updateByPrimaryKeySelective(user);
    	return ResultBean.success(json.getString("openid"));
    }
    
    
    @OperationLog("无预约数据核销")
    @PostMapping("/wx/consumption")
    @ResponseBody
    @ApiOperation(value = "无预约数据核销", notes="无预约数据核销")
    @ApiImplicitParam(name = "param", value = "参数实体",  required = true, dataType = "String")
    @SecurityParameter(outEncode=true,inDecode=false)
    @Authorize(required=true)
    public ResultBean consumption(HttpServletRequest request,@RequestParam String param) throws Exception {
    	Map<String,Object> paramMap=new HashMap<String, Object>();
    	paramMap=AesEncryptUtil.transStringToMap(aesEncryptUtil.decrypt(param));
    	//获取要核销的景点id
    	String id=(String) paramMap.get("id");
    	if(id.isEmpty()) {
    		return ResultBean.error("id不能为空");
    	}
    	//获取订单号
    	Product product=productService.selectById(id);
    	String token = request.getHeader("authorization");
    	String userId = JWT.decode(token).getKeyId();
    	CardUser cardUser=new CardUser();
    	cardUser.setUserid(Integer.parseInt(userId));
    	cardUser.setProductid(product.getSupId());
    	CardUser cardUserfind=cardUserService.selectByUserCard(cardUser);
        // 根据openId 查询用户信息
        User user = userService.selectOne(Integer.parseInt(userId));
        
        Product products=productService.selectById(product.getSupId());
        Map<String,Object> map=new LinkedHashMap<String, Object>();
        map.put("channelId", products.getChannelId());
        map.put("checkNo", cardUserfind.getOrderNo());//核销记录编号
        map.put("checkTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        map.put("checkType", "0");//核销方式
        map.put("idCard", aESUtil.encrypt(user.getIdCard()));//身份证加密
        map.put("idType", "00");//证件类型
        map.put("machineType", "0");//机具类型
        map.put("orderNo",cardUserfind.getOrderNo());//订单号
        map.put("reqId", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));//流水号
        map.put("rightDtlCode", product.getProductSn());//景点编号
        map.put("rightDtlName", product.getProductname());//景点名称
        map.put("voucherType", "1");//入园方式
        StringBuffer str=new StringBuffer();
        for (String key : map.keySet()) {
        	str.append(key+"="+map.get(key)+"&");
        }
        String syrs=str.substring(0, str.length()-1);
        Key key=keyMapper.selectById(products.getChannelId());
        map.put("sign",SignUtil.sign256(syrs,key.getPrivatekey()));
        System.out.println(JSONObject.toJSONString(map));
        String params=HttpClientUtil.doPostJson("http://t3.htaonet.com/right/doRecCheckInfo", JSONObject.toJSONString(map));
        JSONObject json=JSONObject.parseObject(params);
        //核销成功
        if(json.get("retCode").equals("00")) {
        	 WriteOff writeOffs=new WriteOff();
             writeOffs.setChannelid("test");
             writeOffs.setCheckno(cardUserfind.getOrderNo());
             writeOffs.setChecktime(new Date());
             writeOffs.setChecktype("0");
             writeOffs.setMachinetype("0");
             writeOffs.setReqid((String)map.get("reqId"));
             writeOffs.setRightcode(String.valueOf(product.getProductSn()));
             writeOffs.setRightname(product.getProductname());
             writeOffs.setVouchertype("1");
             writeOffs.setUserid(user.getUserId());
             writeOffs.setOrderNo(cardUserfind.getOrderNo());
             writeOffService.insertDynamic(writeOffs);
        	 return ResultBean.success("核销成功！");
        }else {
        	return ResultBean.error(json.getString("retMsg"));
        }
    }

	
	public static void main(String[] args) {
		
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("channelId", "test");
        map.put("idCard", "DYEeaaWf6uNSRBSmvYe6UR+fJZ6GS1rZLpDpXS8Hq68=");
        map.put("checkTime", "20201218101534");
        map.put("orderNo", "TZ20200806151851708233");
        map.put("reqId", "20201218095944321");
        map.put("rightDtlCode", "100002");
        map.put("checkType", "0");
        map.put("machineType", "0");
        map.put("voucherType", "1");
        map.put("checkNo", "100002");
        map.put("rightDtlName", "泰州");
        
        String dd=(String) map.get("idType");
        if(dd==null||dd.isEmpty()) {
        	 System.out.println(1);
        }
       
        map=MapToUtil.sortMapByKey(map);
        StringBuffer str=new StringBuffer();
        for (String key : map.keySet()) {
        	if(!key.equals("sign")) {
        		str.append(key+"="+map.get(key)+"&");
        	}
        }
        String syrs=str.substring(0, str.length()-1);
        String params=HttpClientUtil.doPostJson("http://x35h243242.wicp.vip:14580/reaction/order/wx/dataVerification", JSONObject.toJSONString(map));
    	System.out.println(params);
    }
    
}
