package com.net.system.mapper.order;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.Cart;

@Mapper
public interface CartMapper {
	
	int delete(Map<String,Object> map);
	
	int deleteBySku(Cart cart);

	int insert(Map<String,Object> map);

	int insertDynamic(Map<String,Object> map);

	int updateDynamic(Map<String,Object> map);

	int updateNumber(Map<String,Object> map);

	Cart selectById(String id);
	
	Cart selectBySkuId(Map<String,Object> map);
	
	Map<String,Object> findCartById(Map<String,Object> map);

	List<Map<String,Object>> findPageWithResult(Map<String,Object> map);
	
	int countCartNumber(Map<String,Object> map);
	
	
	

}
