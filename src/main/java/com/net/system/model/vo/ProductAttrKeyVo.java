package com.net.system.model.vo;

import java.util.List;

import com.net.system.model.ProductAttrKey;
import com.net.system.model.ProductAttrVal;
import com.net.system.model.ProductSku;

public class ProductAttrKeyVo extends ProductAttrKey{
	
	private Integer count;

	private List<ProductAttrVal> attrValList;
	


	public List<ProductAttrVal> getAttrValList() {
		return attrValList;
	}

	public void setAttrValList(List<ProductAttrVal> attrValList) {
		this.attrValList = attrValList;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	
	
	
	

}
