package com.net.system.model.vo;

import java.util.List;

import com.net.system.model.Files;
import com.net.system.model.Label;
import com.net.system.model.Product;
import com.net.system.model.ProductSku;

public class ProductSkuInfoVo extends Product {
	
	private List<Files> fileList;
	
	private List<Label> labelList;

	private List<ProductSkuKeyVo> specsList;

	private List<ProductSku> skuList;
	
	

	public List<Label> getLabelList() {
		return labelList;
	}

	public void setLabelList(List<Label> labelList) {
		this.labelList = labelList;
	}

	public List<Files> getFileList() {
		return fileList;
	}

	public void setFileList(List<Files> fileList) {
		this.fileList = fileList;
	}

	public List<ProductSkuKeyVo> getSpecsList() {
		return specsList;
	}

	public void setSpecsList(List<ProductSkuKeyVo> specsList) {
		this.specsList = specsList;
	}

	public List<ProductSku> getSkuList() {
		return skuList;
	}

	public void setSkuList(List<ProductSku> skuList) {
		this.skuList = skuList;
	}

}
