package com.net.system.model;

/**
 * 商品类型
 * @author Administrator
 *
 */
public class ProductAttrKey {
	  private Integer id;

	    /**
	     * 类型名称
	     */
	    private String name;

	    /**
	     * 排序号
	     */
	    private Integer sort;

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