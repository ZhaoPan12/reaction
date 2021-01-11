package com.net.system.service.imple.order;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.net.common.util.DateUtils;
import com.net.system.mapper.order.CartMapper;
import com.net.system.model.Cart;
import com.net.system.service.order.CartService;

import javassist.expr.NewArray;

@Service
public class CartServiceImpl implements CartService {

	@Resource
	private CartMapper cartMapper;

	@Override
	public int delete(Map<String, Object> map) {
		map.put("id", map.get("id").toString().split(","));
		return cartMapper.delete(map);
	}

	@Override
	public Integer addCart(Map<String, Object> map) {

		Integer quantity = Integer.parseInt(map.get("quantity").toString());
		Cart cart = cartMapper.selectBySkuId(map);
        
		if (cart != null) {
			cart.setUpdatetime(new Date());
			map.put("quantity", quantity + cart.getQuantity());
			map.put("id", cart.getId());
			map.put("updatetime", DateUtils.getTime());
			return cartMapper.updateNumber(map);
		} else {
			map.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
			map.put("quantity", quantity);
			map.put("createtime", new Date());
			map.put("updatetime",new Date());
			return cartMapper.insert(map);
		}
	}

	@Override
	public int update(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Integer countCartNumber(Map<String, Object> map) {

		return cartMapper.countCartNumber(map);
	}

	@Override
	public Integer updateCartProductNumber(Map<String, Object> map) {
		map.put("updatetime",new Date());
		return cartMapper.updateNumber(map);
	}

	@Override
	public PageInfo<Map<String, Object>> findCartPage(Map<String, Object> map) {
		List<Map<String, Object>> result = null;

		Integer page = Integer.parseInt(map.get("page").toString());
		Integer limit = Integer.parseInt(map.get("limit").toString());
		PageHelper.startPage(page, limit);
		result = cartMapper.findPageWithResult(map);
		// 自定义返回参数
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(result);
		return pageInfo;
	}

	

	@Override
	public Map<String, Object> findCartById(Map<String, Object> map) {
		
		return cartMapper.findCartById(map);
	}

}
