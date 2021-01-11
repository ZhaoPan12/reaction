package com.net.system.service.product;

import java.util.List;
import java.util.Map;

import com.net.system.model.ProductSku;
import com.net.system.model.ProductSkuKey;
import com.net.system.model.vo.ProductSkuKeyVo;
import com.net.system.model.vo.ProductSkuVo;

public interface ProductSkuService {
	
	int setSpecs(ProductSkuVo skuVo);
	
	int update(ProductSkuVo skuVo);
	
	
	List<ProductSkuKeyVo> findSpecsByProductId(ProductSkuKey productSkuKey);
	
	List<ProductSku> findSkuByProductId(Map<String,Object> map);
	
	

}
