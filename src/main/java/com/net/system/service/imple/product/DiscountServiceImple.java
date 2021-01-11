package com.net.system.service.imple.product;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.net.system.model.DiscountUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.net.system.mapper.product.DiscountMapper;
import com.net.system.model.Discount;
import com.net.system.service.product.DiscountService;

import cn.hutool.core.lang.UUID;

@Service
public class DiscountServiceImple implements DiscountService {
	
	@Autowired
	private DiscountMapper discountMapper;

	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(Discount discount) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertDynamic(Discount discount) {
		discount.setStatus(1);
		discount.setAddTime(new Date());
		discount.setId(UUID.randomUUID().toString().replace("-", ""));
		// TODO Auto-generated method stub
		return discountMapper.insertDynamic(discount);
	}

	@Override
	public int updateDynamic(Discount discount) {
		// TODO Auto-generated method stub
		return discountMapper.updateDynamic(discount);
	}

	@Override
	public int update(Discount discount) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Discount selectById(String id) {
		// TODO Auto-generated method stub
		return discountMapper.selectById(id);
	}

	@Override
	public List<Discount> findPageWithResult(Map<String, Object> map) {
		String page=(String) map.get("page");
		String rows=(String) map.get("limit");
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		return discountMapper.findPageWithResult(map);
	}

	@Override
	public Integer findPageWithCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public List<Discount> selectMyDiscountList(Map<String, Object> map) {
		String page=(String) map.get("page");
		String rows= (String)map.get("limit");
		if(page!=null) {
			PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		}
        return discountMapper.selectMyDiscountList(map);
    }

	@Override
	public List<Discount> selectIsReceiveDiscountList(Map<String, Object> map) {
		String page= (String) map.get("page");
		String rows= (String)map.get("limit");
		if(page!=null) {
			PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		}
		return discountMapper.selectIsReceiveDiscountList(map);
	}

}
