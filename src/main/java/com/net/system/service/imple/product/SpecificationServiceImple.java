package com.net.system.service.imple.product;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.net.system.mapper.product.SpecificationMapper;
import com.net.system.model.Specification;
import com.net.system.service.product.SpecificationService;

@Service
public class SpecificationServiceImple implements SpecificationService {

	@Autowired
	private SpecificationMapper specificationMapper;
	
	
	@Override
	public int delete(Integer id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(Specification specification) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertDynamic(Specification specification) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateDynamic(Specification specification) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Specification specification) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Specification selectById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Specification> findPageWithResult(Map<String, Object> map) {
		String page=(String) map.get("page");
		String rows=(String) map.get("limit");
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		// TODO Auto-generated method stub
		return specificationMapper.findPageWithResult(map);
	}

	@Override
	public Integer findPageWithCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

}
