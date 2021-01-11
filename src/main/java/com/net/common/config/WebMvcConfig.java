package com.net.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.net.common.aop.AuthorizationInterceptor;
import com.net.common.interceptor.LogMDCInterceptor;
import com.net.common.interceptor.RequestLogHandlerInterceptor;

import javax.annotation.Resource;
import java.util.Arrays;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private RequestLogHandlerInterceptor logHandlerInterceptor;

    @Resource
    private LogMDCInterceptor shiroMDCInterceptor;
    @Resource
    private AuthorizationInterceptor authorizationInterceptor;

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(shiroMDCInterceptor)
                .excludePathPatterns(Arrays.asList("/css/**", "/fonts/**", "/images/**", "/js/**", "/lib/**", "/error"));

        registry.addInterceptor(logHandlerInterceptor)
                .excludePathPatterns(Arrays.asList("/css/**", "/fonts/**", "/images/**", "/js/**", "/lib/**", "/error"));
        
        registry.addInterceptor(authorizationInterceptor)
        		.excludePathPatterns(Arrays.asList("/css/**", "/fonts/**", "/images/**", "/js/**", "/lib/**", "/error"));
    }

    @Override
	public void addCorsMappings(CorsRegistry registry) {
		//设置允许跨域的路径
        registry.addMapping("/**")
                //设置允许跨域请求的域名
                .allowedOrigins("*")
                //是否允许证书 不再默认开启
                .allowCredentials(true)
                //设置允许的方法
                .allowedMethods("*")
                //跨域允许时间
                .maxAge(3600);
	}
}