package com.net.system.service.product;

import java.util.List;
import java.util.Map;

import com.net.system.model.ProductAttrKey;
import com.net.system.model.vo.ProductAttrKeyVo;

public interface ProductAttrKeyService {

	List<ProductAttrKeyVo> findPageWithResult(Map<String, Object> map);
	
	List<ProductAttrKeyVo> findAttrKeyAll();

	ProductAttrKey selectById(Integer id);

	int delete(Integer id);

	int insert(ProductAttrKey productAttrKey);

	int update(ProductAttrKey productAttrKey);

}
