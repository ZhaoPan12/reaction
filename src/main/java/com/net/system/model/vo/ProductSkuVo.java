package com.net.system.model.vo;

import java.util.List;

import com.net.system.model.ProductSku;

public class ProductSkuVo extends ProductSku{
	
	private List<ProductSkuKeyVo> specsList;
	
	private List<ProductSku> skuList;
	
	private Integer proStock;
	
	private Integer status;
	
	
	
	
	
	public Integer getProStock() {
		return proStock;
	}

	public void setProStock(Integer proStock) {
		this.proStock = proStock;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
