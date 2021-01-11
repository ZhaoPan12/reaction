package com.net.system.model.vo;

import java.util.List;

import com.net.system.model.ProductAttrVal;

public class ProductAttrValVo extends ProductAttrVal{
	
	private String  parentName;
	
	private List<ProductAttrVal> valueList;
	
	public List<ProductAttrVal> getValueList() {
		return valueList;
	}

	public void setValueList(List<ProductAttrVal> valueList) {
		this.valueList = valueList;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
	

}
