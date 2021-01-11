package com.net.system.controller.report;
import cn.hutool.json.JSONObject;

import com.net.common.interceptor.SecurityParameter;
import com.net.common.util.BaseUtil;
import com.net.common.util.HttpUtil;
import com.net.common.util.MessageUtils;
import com.net.common.util.ResultBean;
import com.net.common.util.WeixinCheckoutUtil;
import com.net.system.model.User;
import com.net.system.model.vo.TextMessage;
import com.net.system.service.sysmange.UserService;
import com.zhenzi.sms.ZhenziSmsClient;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author: YuXinXin
 * @Description:
 * @Date:Created in 21:01 2020/8/1
 **/
@RestController
@RequestMapping("/message")
public class CodeController {
	
	
  @Autowired
  private  RedisTemplate<String, String> redisTemplate;
  
  @Resource
  private UserService userService;
    
  private  Logger logger = LoggerFactory.getLogger(CodeController.class);
    
  private String apiUrl="https://sms_developer.zhenzikj.com";
  private String appId = "106750";
  private String appSecret = "85f78715-6d71-444d-b30e-b560417b4906";

  @ApiOperation(value="短信接口", notes="短信接口")
  @RequestMapping(value = "/phone/code",method = RequestMethod.GET)
  public ResultBean getCode(@RequestParam("phone") String phone, HttpServletRequest request) {
    try{
      JSONObject json;
      String code = String.valueOf(new Random().nextInt(899999) + 100000);
      String result=sendMessage(code,phone);
      json = new JSONObject(result);
      if(json.getInt("status")!=0) {
    	  return ResultBean.error("发送短信失败");
      }else {
    	  json = new JSONObject();
          json.append("memPhone",phone);
          json.append("code",code);
          json.append("createTime",System.currentTimeMillis());
          // 将认证码存入Redis
          redisTemplate.opsForValue().set(phone,code,60 * 5,TimeUnit.SECONDS);
      }
    
      return ResultBean.success("发送短信成功");
    }catch(Exception e){
      e.printStackTrace();
      return ResultBean.error(e.getMessage());
    }
  }
  
  @ApiOperation(value="短信验证码验证", notes="短信验证码验证")
  @RequestMapping(value = "/phone/verify")
  @SecurityParameter(outEncode=false,inDecode=false)
  public ResultBean loginNoVerify(@RequestParam("code") String code,@RequestParam("phone") String phone, HttpServletRequest request) {
    try{
        String codes = (String)redisTemplate.opsForValue().get(phone);
        if(codes!=null) {
        	 if(codes.equals(code)) {
             	redisTemplate.delete(phone);
             	//验证成功则通过id登录
             	User user=userService.selectUserByPhone(phone);
             	//如果存在该用户则直接登录
             	if(user!=null) {
                     //uuid生成唯一key，用于维护微信小程序用户与服务端的会话
                     String skey = UUID.randomUUID().toString();
                     // 已存在，更新用户登录时间
                     user.setLastLoginTime(new Date());
                      // 重新设置会话skey
                     user.setSkey(skey);
                     user.setSessionId(request.getSession().getId());
                     userService.updateWeChatUser(user);
                     if(user.getIdCard()!=null&&!user.getIdCard().isEmpty()) {
                    	 user.setIdCard(user.getIdCard().substring(0, 6)+"*****"+user.getIdCard().substring(14, 18)); 
                     }
             	}else {//否则先新增再登录
             		user=new User();
             		user.setPhone(phone);
             		user.setCreateTime(new Date());
             		user.setLastLoginTime(new Date());
             		user.setUsername(phone);
             		//uuid生成唯一key，用于维护微信小程序用户与服务端的会话
                    String skey = UUID.randomUUID().toString();
                    user.setSkey(skey);
                    user.setSessionId(request.getSession().getId());
                    user.setPassword(phone);
                    user.setStatus("1");
             		userService.insertWeChatUser(user);
             	}
             	Map<String,Object> map=new HashMap<String, Object>();
             	map.put("user", user);
             	map.put("token", BaseUtil.getToken(user));
             	return  ResultBean.success(map);
              }else {
             	 return ResultBean.error("验证码错误");
              } 
        }
        return ResultBean.error("验证码已过期");
    }catch(Exception e){
      e.printStackTrace();
      return ResultBean.error(e.getMessage());
    }
  }
  
  @ApiOperation(value="短信验证码验证", notes="短信验证码验证")
  @RequestMapping("/message")
  @ResponseBody
  public String testMessage(HttpServletRequest request,
		  @RequestParam(required = false) String echostr,
          @RequestParam(required = false) String signature,
    @RequestParam(required = false) String timestamp,
    @RequestParam(required =false) String nonce) {
	  try {
          //只需要把微信请求的 echostr, 返回给微信就可以了
          logger.info("测试来过===================" + echostr);
          logger.info("测试来过===================" + signature);
          logger.info("测试来过===================" + timestamp);
          logger.info("测试来过===================" + nonce);
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
 		 if (signature != null && WeixinCheckoutUtil.checkSignature(signature, timestamp, nonce)) {
 			System.out.println("验证成功");
            return echostr;
          }else {
        	  System.out.println("验证失败");
        	  return "错误";
          }
 		
      } catch (Exception e) {
          logger.info("测试微信公众号的接口配置信息发生异常：", e);
          return "错误！！！";
      }
  }
  

  @RequestMapping(value = "wx/index", method = { RequestMethod.GET, RequestMethod.POST })
  public void index(HttpServletRequest request, HttpServletResponse response) throws IOException {

      // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
      request.setCharacterEncoding("UTF-8"); // 微信服务器POST消息时用的是UTF-8编码，在接收时也要用同样的编码，否则中文会乱码；
      response.setCharacterEncoding("UTF-8"); // 在响应消息（回复消息给用户）时，也将编码方式设置为UTF-8，原理同上；

      String method = request.getMethod().toLowerCase();
      logger.info("method: {}", method);

      // 验证是否是微信请求
      if ("get".equals(method)) {
          doGet(request, response);
          return;
      }

      // POST请求接收消息，且给客户响应消息
      doPost(request, response);
  }

  /**
   * Post请求用于接收消息且处理消息之后回传消息
   * 
   * @param request
   * @param response
   * @throws IOException
   */
  private void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
      PrintWriter out = response.getWriter();
      try {
          Map<String, String> map = MessageUtils.xmlToMap(request);
          String fromUserName = map.get("FromUserName");
          String toUserName = map.get("ToUserName");
          String msgType = map.get("MsgType");
          String content = map.get("Content");
          logger.info("map: {}", map);

          if (StringUtils.isNotBlank(content)) {
              System.out.println("https://www.zyyg0523.cn/tzsmt/login.html");
          }

          String message = null;
          if ("text".equals(msgType)) {
              TextMessage textMessage = new TextMessage();
              // 回传消息，所以讲fromuser和toUser交换
              textMessage.setFromUserName(toUserName);
              textMessage.setToUserName(fromUserName);
              textMessage.setMsgType(msgType);
              textMessage.setCreateTime(new Date().getTime());
              textMessage.setContent("https://www.zyyg0523.cn/tzsmt/login.html");
              logger.info("textMessage: {}", textMessage);

              message = MessageUtils.textMessageToXml(textMessage);
          }

          out.print(message);// 把消息发送到客户端
      } catch (DocumentException e) {
          logger.error("dispose post request error", e);
      } finally {
          out.close();
      }
  }
  
  /**
   * Get请求用于微信配置验证
   * 
   * @param request
   * @param response
   * @throws IOException
   */
  private void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      String signature = request.getParameter("signature");// 微信加密签名
      String timestamp = request.getParameter("timestamp");// 时间戳
      String nonce = request.getParameter("nonce");// 随机数
      String echostr = request.getParameter("echostr");// 随机字符串
      logger.info("signature: {}, timestamp: {}, nonce: {}, echostr: {}", signature, timestamp, nonce, echostr);

      if (StringUtils.isNoneBlank(signature, timestamp, nonce)
              && WeixinCheckoutUtil.checkSignature(signature, timestamp, nonce)) {
          response.getWriter().write(echostr);
      }
  }
  
  
  @RequestMapping(value = "wx/getToken", method = { RequestMethod.GET, RequestMethod.POST })
  public void getToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

      
  }
  
	public String sendMessage(String code,String phone) {
		String account="922582";
		String mobile=phone;
		String content="【登陆验证】:您的验证码是"+code+"，五分钟内有效，请勿泄漏给他人使用。";
		String extno="106901582";
	//	String password=MD5Util.MD5("YXxnr6"+extno+content+mobile);
		String password="e48CPH";
		String param=HttpUtil.get("http://180.101.185.166:7862/sms?action=send&account="+account+"&password="+password+"&mobile="+mobile+"&content="+content+"&extno="+extno+"&rt=json");
		return param;
	}
}


