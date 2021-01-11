package com.net.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.net.common.aop.AuthorizationInterceptor;

public class ResourceHandlerConfig extends WebMvcConfigurationSupport{

	/**
	 * 注册 拦截器
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	   registry.addInterceptor(new AuthorizationInterceptor())
	         .addPathPatterns("/wx/**").excludePathPatterns("/error/**");
	}
}
