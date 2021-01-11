package com.net.system.service.imple.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.net.system.mapper.product.ProductAttrKeyMapper;
import com.net.system.mapper.product.ProductAttrValMapper;
import com.net.system.model.ProductAttrKey;
import com.net.system.model.ProductAttrVal;
import com.net.system.model.vo.ProductAttrKeyVo;
import com.net.system.service.product.ProductAttrKeyService;

@Service
public class ProductAttrKeyServiceImpl implements ProductAttrKeyService {

	@Resource
	private ProductAttrKeyMapper productAttrKeyMapper;

	@Resource
	private ProductAttrValMapper productAttrValMapper;

	@Override
	public List<ProductAttrKeyVo> findPageWithResult(Map<String, Object> map) {
		Integer page = Integer.parseInt(map.get("page").toString());
		Integer limit = Integer.parseInt(map.get("limit").toString());
		PageHelper.startPage(page, limit);
		return productAttrKeyMapper.findPageWithResult(map);
	}

	@Override
	public ProductAttrKey selectById(Integer id) {
		// TODO Auto-generated method stub
		return productAttrKeyMapper.selectById(id);
	}

	@Override
	public int delete(Integer id) {
		ProductAttrVal productAttrVal = new ProductAttrVal();
		productAttrVal.setAttrId(id);
		productAttrValMapper.delete(productAttrVal);
		return productAttrKeyMapper.delete(id);
	}

	@Override
	public int insert(ProductAttrKey productAttrKey) {
		// TODO Auto-generated method stub
		return productAttrKeyMapper.insert(productAttrKey);
	}

	@Override
	public int update(ProductAttrKey productAttrKey) {
		// TODO Auto-generated method stub
		return productAttrKeyMapper.update(productAttrKey);
	}

	@Override
	public List<ProductAttrKeyVo> findAttrKeyAll() {
		
		return productAttrKeyMapper.findPageWithResult(new HashMap<String, Object>());
	}

}
