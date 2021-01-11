package com.net.system.service.order;

import java.util.Map;

import com.github.pagehelper.PageInfo;

public interface CartService {

	int delete(Map<String,Object> map);

	Integer addCart(Map<String,Object> map);
	
	Integer countCartNumber(Map<String,Object> map);
	
	Integer updateCartProductNumber(Map<String,Object> map);
	
	
	
	Map<String,Object> findCartById(Map<String,Object> map);
	
	PageInfo<Map<String, Object>> findCartPage(Map<String,Object> map);

	int update(Map<String,Object> map);





}
