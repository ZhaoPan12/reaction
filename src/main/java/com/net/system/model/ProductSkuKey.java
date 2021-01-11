package com.net.system.model;


public class ProductSkuKey {
	
    private Integer id;

    private String name;

    /**
     * 排序号
     */
    private Integer sort;
    
    private String productId;
    
    
    

    public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}