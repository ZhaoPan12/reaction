package com.net.common.aop;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.net.common.interceptor.Authorize;
import com.net.system.model.User;
import com.net.system.service.sysmange.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
 
import javax.servlet.http.HttpServletRequest;	
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
 
/**
 * @Author: Yangke
 * @Date: 2019/3/28 20:00
 *
 * 获取token并验证token
 **/
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;
 
    //拦截器：请求之前preHandle
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
 
        //检查有没有需要用户权限的注解
        //如果有注解Authorize，就需要验证token
        if (method.isAnnotationPresent(Authorize.class)) {
            Authorize userLoginToken = method.getAnnotation(Authorize.class);
            if (userLoginToken.required()) {
                String token = httpServletRequest.getHeader("authorization");// 从 http 请求头中取出 token
 
                // 执行认证
                if (token == null) {
                    throw new RuntimeException("无token，请重新登录");
                }
                // 获取 token 中的 open_id
                String openId;
                try {
                    // 获取 open_id
                	openId = JWT.decode(token).getKeyId();
                    // 添加request参数，用于传递openId
                    httpServletRequest.setAttribute("currentUser", openId);
                    // 根据userId 查询用户信息
                    User user = userService.selectOne(Integer.parseInt(openId));
                    if (user == null) {
                        throw new RuntimeException("用户不存在，请重新登录");
                    }
 
                    try {
                        String session_key = JWT.decode(token).getClaim("session_key").as(String.class);
                        // 添加request参数，用于传递openId
                        httpServletRequest.setAttribute("sessionKey", session_key);
                    }
                    catch (Exception e){
                    }
                    // 验证 sKey
                    JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getSkey())).build();
                    try {
                        jwtVerifier.verify(token);
                    } catch (JWTVerificationException e) {
                        throw new RuntimeException("401");
                    }
                } catch (JWTDecodeException j) {
                    throw new RuntimeException("401");
                }
 
                return true;
            }
        }
        return true;
    }
 
 
    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {
 
    }
 
    //拦截器：请求之后：afterCompletion
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}
