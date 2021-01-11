package com.net.common.aop;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.net.common.util.IPUtils;
import com.net.system.model.User;
import com.net.system.service.sysmange.LoginLogService;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@ConditionalOnProperty(value = "shiro-action.log.login", havingValue = "true")
public class LoginLogAspect {

    @Resource
    private LoginLogService loginLogService;

    @Pointcut("execution(com.net.common.util.ResultBean com.net.system.controller.*..LoginController.login(com.net.system.model.User, String) )")
    public void loginLogPointCut() {}

    @After("loginLogPointCut()")
    public void recordLoginLog(JoinPoint joinPoint) throws IOException {
    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest(); 
        // 获取登陆参数
        Object[] args = joinPoint.getArgs();
        User user = (User) args[0];

        Subject subject = SecurityUtils.getSubject();

        String ip = IPUtils.getIpAddress(request);
        loginLogService.addLog(user.getUsername(), subject.isAuthenticated(), ip);
    }
}