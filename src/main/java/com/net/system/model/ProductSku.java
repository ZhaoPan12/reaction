package com.net.system.model;

import java.math.BigDecimal;

/**
 * 商品SKU
 * @author Administrator
 *
 */
public class ProductSku {
	
    /**
     * sukid
     */
    private String skuId;

    /**
     * 商品id
     */
    private String productId;

    /**
     * 商品地址
     */
    private String attrSymbolPath;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 预警库存
     */
    private Integer warningStock;

    /**
     * 自定义属性title
     */
    private String customTitle;

    /**
     * 自定义属性value
     */
    private String customValue;
    
    
    
    private String describe;
    
   
	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getAttrSymbolPath() {
        return attrSymbolPath;
    }

    public void setAttrSymbolPath(String attrSymbolPath) {
        this.attrSymbolPath = attrSymbolPath;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getWarningStock() {
        return warningStock;
    }

    public void setWarningStock(Integer warningStock) {
        this.warningStock = warningStock;
    }

    public String getCustomTitle() {
        return customTitle;
    }

    public void setCustomTitle(String customTitle) {
        this.customTitle = customTitle;
    }

    public String getCustomValue() {
        return customValue;
    }

    public void setCustomValue(String customValue) {
        this.customValue = customValue;
    }
}