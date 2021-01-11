package com.net.system.service.product;

import java.util.List;
import java.util.Map;

import com.net.common.util.ResultBean;
import com.net.system.model.ProductAttrVal;
import com.net.system.model.vo.ProductAttrValVo;
import com.net.system.model.vo.ProductSkuVo;

public interface ProductAttrValService {

	int delete(ProductAttrVal productAttrVal);

	ResultBean insert(ProductAttrVal productAttrVal);

	ResultBean update(ProductAttrVal productAttrVal);

	ProductAttrVal selectById(Integer id);

	List<ProductAttrValVo> findPageWithResult(Map<String, Object> map);
	
	List<ProductAttrValVo> findByAttrId(Integer attrId);
	
	
	

}
