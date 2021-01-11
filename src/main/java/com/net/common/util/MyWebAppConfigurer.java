package com.net.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 资源映射路径
 */
@Configuration
public class MyWebAppConfigurer implements WebMvcConfigurer {
	
	//访问映射路径
	 @Value("${photo.resourceHandler}")
	 private String resourceHandler;

	 //资源绝对路径
	 @Value("${photo.resourceLocations}")
	 private String resourceLocations;
	 
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	System.out.println("资源映射路径已执行。。。。。");
        registry.addResourceHandler("/"+resourceHandler+"/**").addResourceLocations("file:"+resourceLocations+"/");
    }
}
