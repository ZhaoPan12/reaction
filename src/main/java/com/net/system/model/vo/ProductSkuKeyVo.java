package com.net.system.model.vo;

import java.util.List;

import com.net.system.model.ProductSkuKey;
import com.net.system.model.ProductSkuVal;

public class ProductSkuKeyVo extends ProductSkuKey{
	
	
	private List<ProductSkuVal> valList;

	public List<ProductSkuVal> getValList() {
		return valList;
	}

	public void setValList(List<ProductSkuVal> valList) {
		this.valList = valList;
	}
	
	
	
	

}
