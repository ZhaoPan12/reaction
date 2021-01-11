package com.net.system.controller.sysmange;

import cn.hutool.core.util.IdUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.net.common.annotation.OperationLog;
import com.net.common.aop.MyRequestBodyAdvice;
import com.net.common.enumerate.UserConstantInterface;
import com.net.common.exception.CaptchaIncorrectException;
import com.net.common.interceptor.Authorize;
import com.net.common.interceptor.SecurityParameter;
import com.net.common.shiro.ShiroActionProperties;
import com.net.common.util.AesEncryptUtil;
import com.net.common.util.BaseUtil;
import com.net.common.util.CaptchaUtil;
import com.net.common.util.HttpClientUtil;
import com.net.common.util.ResultBean;
import com.net.common.util.WechatUtil;
import com.net.system.model.User;
import com.net.system.service.sysmange.MailService;
import com.net.system.service.sysmange.UserService;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@Api(tags = "LoginController", description = "登录管理类")
public class LoginController {

    @Resource
    private UserService userService;

    @Resource
    private MailService mailService;

    @Resource
    private TemplateEngine templateEngine;

    @Resource
    private ShiroActionProperties shiroActionProperties;
    
    private AesEncryptUtil aesEncryptUtil=AesEncryptUtil.getAesEncryptUtil();
    

    @GetMapping("/login")
    @ApiIgnore
    public String login(Model model) {
        model.addAttribute("loginVerify", shiroActionProperties.getLoginVerify());
        return "login";
    }

    @GetMapping("/register")
    @ApiIgnore
    public String register() {
        return "register";
    }

    @PostMapping("/login")
    @ResponseBody
    @ApiOperation(value = "用户登录", notes="用户登录")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "user", value = "用户实体", required = true, dataType = "User"),
    	@ApiImplicitParam(name = "captcha", value = "验证码", required = true, dataType = "String")
    })
    public ResultBean login(User user, @RequestParam(value = "captcha", required = false) String captcha) {
        Subject subject = SecurityUtils.getSubject();
/**
        // 如果开启了登录校验
        if (shiroActionProperties.getLoginVerify()) {
            String realCaptcha = (String) SecurityUtils.getSubject().getSession().getAttribute("captcha");
            // session 中的验证码过期了
            if (realCaptcha == null || !realCaptcha.equals(captcha.toLowerCase())) {
                throw new CaptchaIncorrectException();
            }
        }
**/
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        subject.login(token);
        userService.updateLastLoginTimeByUsername(user.getUsername());
        return ResultBean.success("登录成功");
    }
    @PostMapping("/loginNoVerify")
    @ResponseBody
    @SecurityParameter(inDecode = false, outEncode = true)
    public ResultBean loginNoVerify(HttpServletRequest request,@RequestBody User user) {
    	 Subject subject = SecurityUtils.getSubject();
    	 UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
         subject.login(token);
         userService.updateLastLoginTimeByUsername(user.getUsername());
         User users=userService.selectOneByUserName(user.getUsername());
         //uuid生成唯一key，用于维护微信小程序用户与服务端的会话
          String skey = UUID.randomUUID().toString();
         // 已存在，更新用户登录时间
          users.setLastLoginTime(new Date());
          // 重新设置会话skey
          users.setSkey(skey);
          users.setSessionId(request.getSession().getId());
          userService.updateWeChatUser(users);
         return ResultBean.success();
    }
    @OperationLog("注销")
    @GetMapping("/logout")
    @ApiIgnore
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:login";
    }

    @PostMapping("/register")
    @ResponseBody
    @ApiOperation(value = "用户注册", notes="用户注册")
    @ApiImplicitParam(name = "user", value = "用户实体", required = true, dataType = "User")
    public ResultBean register(User user) {
        userService.checkUserNameExistOnCreate(user.getUsername());
        String activeCode = IdUtil.fastSimpleUUID();
        user.setActiveCode(activeCode);
        user.setStatus("0");

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String url = request.getScheme() + "://"
                + request.getServerName()
                + ":"
                + request.getServerPort()
                + "/active/"
                + activeCode;
        Context context = new Context();
        context.setVariable("url", url);
        String mailContent = templateEngine.process("mail/registerTemplate", context);
        new Thread(() ->
                mailService.sendHTMLMail(user.getEmail(), "Shiro-Action 激活邮件", mailContent))
                .start();

        // 注册后默认的角色, 根据自己数据库的角色表 ID 设置
        Integer[] initRoleIds = {2};
        return ResultBean.success(userService.add(user, initRoleIds));
    }

    @GetMapping("/captcha")
    @ApiIgnore
    public void captcha(HttpServletResponse response) throws IOException {
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CaptchaUtil.Captcha captcha = CaptchaUtil.createCaptcha(140, 38, 4, 10, 30);
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("captcha", captcha.getCode());

        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(captcha.getImage(), "png", os);
    }

    @OperationLog("激活注册账号")
    @GetMapping("/active/{token}")
    @ApiIgnore
    public String active(@PathVariable("token") String token, Model model) {
        User user = userService.selectByActiveCode(token);
        String msg;
        if (user == null) {
            msg = "请求异常, 激活地址不存在!";
        } else if ("1".equals(user.getStatus())) {
            msg = "用户已激活, 请勿重复激活!";
        } else {
            msg = "激活成功!";
            user.setStatus("1");
            userService.activeUserByUserId(user.getUserId());
        }
        model.addAttribute("msg", msg);
        return "active";
    }
    
    /**
         * 微信用户登录详情
     */
    @PostMapping("/wx/login")
    @ResponseBody
    //@Authorize(required=true)
    @SecurityParameter(outEncode=true)
    public Map user_login(@RequestBody  String requestData) {
    	Map<String,Object> map=new HashMap<String, Object>();
    	Map<String,Object> paramMap=new HashMap<String, Object>();
    	
    	try {
			String data=aesEncryptUtil.desEncrypt(requestData);
			paramMap=AesEncryptUtil.transStringToMap(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	String code=(String) paramMap.get("code");
    	String rawData=(String) paramMap.get("rawData");
    	String signature=(String) paramMap.get("signature");
    	String encrypteData=(String) paramMap.get("encrypteData");
    	String iv=(String) paramMap.get("iv");
        // 用户非敏感信息：rawData
        // 签名：signature
        JSONObject rawDataJson = JSON.parseObject(rawData);
        // 1.接收小程序发送的code
        // 2.开发者服务器 登录凭证校验接口 appi + appsecret + code
        JSONObject SessionKeyOpenId = WechatUtil.getSessionKeyOrOpenId(code);
        // 3.接收微信接口服务 获取返回的参数
        String openid = SessionKeyOpenId.getString("openid");
        String sessionKey = SessionKeyOpenId.getString("session_key");
 
        // 4.校验签名 小程序发送的签名signature与服务器端生成的签名signature2 = sha1(rawData + sessionKey)
        String signature2 = DigestUtils.sha1Hex(rawData + sessionKey);
        if (!signature.equals(signature2)) {
        	map.put("code", 0);
        	map.put("msg", "签名校验失败");
            return map;
        }
        // 5.根据返回的User实体类，判断用户是否是新用户，是的话，将用户信息存到数据库；不是的话，更新最新登录时间
        User user = userService.selectOneByOpenId(openid);
        // uuid生成唯一key，用于维护微信小程序用户与服务端的会话
        String skey = UUID.randomUUID().toString();
        if (user == null) {
            // 用户信息入库
            String nickName = rawDataJson.getString("nickName");
//            String avatarUrl = rawDataJson.getString("avatarUrl");
//            String gender = rawDataJson.getString("gender");
//            String city = rawDataJson.getString("city");
//            String country = rawDataJson.getString("country");
//            String province = rawDataJson.getString("province");
            user = new User();
            user.setStatus("1");
            user.setOpenId(openid);
            user.setSkey(skey);
            user.setCreateTime(new Date());
            user.setLastLoginTime(new Date());
            user.setSessionId(sessionKey);
            user.setUsername(nickName);
            user.setPassword(openid);//使用openid作为密码
            userService.insertWeChatUser(user);
        } else {
            // 已存在，更新用户登录时间
            user.setLastLoginTime(new Date());
            // 重新设置会话skey
            user.setSkey(skey);
            userService.updateWeChatUser(user);
        }
        //encrypteData比rowData多了appid和openid
        //JSONObject userInfo = WechatUtil.getUserInfo(encrypteData, sessionKey, iv);
        //6. 把新的skey返回给小程序BaseUtil.getToken(user)
        map.put("code", 0);
    	map.put("msg", "登录成功!");
    	map.put("token",BaseUtil.getToken(user));
//    	Subject subject = SecurityUtils.getSubject();
//    	user.setPassword(openid);
//   	UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
//      subject.login(token);
    	System.out.println(map);
        return map;
    }
    
    @PostMapping("/wx/tokenVerification")
    @ResponseBody
    @Authorize(required = true)
    public ResultBean wxTokenVerification() {
		return ResultBean.success();
    }
    
    
    @PostMapping("/tokenVerification")
    @ResponseBody
   // @Authorize(required=false)
  //  @SecurityParameter(inDecode = true, outEncode = true)
    public ResultBean tokenVerification(@RequestBody String Params) {
    	System.out.println(Params);
		return ResultBean.success();
    }

}
