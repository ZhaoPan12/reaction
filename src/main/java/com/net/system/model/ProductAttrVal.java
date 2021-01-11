package com.net.system.model;


/**
 * 属性值
 * @author Administrator
 *
 */
public class ProductAttrVal {
	 private Integer id;

	    /**
	     * 属性名称
	     */
	    private String name;

	    private Integer attrId;

	    /**
	     * 属性的入录方式\r\n1、列表选取\r\n2、自定义
	     */
	    private Integer type;

	    /**
	     * 属性值
	     */
	    private String value;

	    /**
	     * 排序号
	     */
	    private Integer sort;
	    
	    
	    private String img;

	    
	    
	    public String getImg() {
			return img;
		}

		public void setImg(String img) {
			this.img = img;
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

	    public Integer getAttrId() {
	        return attrId;
	    }

	    public void setAttrId(Integer attrId) {
	        this.attrId = attrId;
	    }

	    public Integer getType() {
	        return type;
	    }

	    public void setType(Integer type) {
	        this.type = type;
	    }

	    public String getValue() {
	        return value;
	    }

	    public void setValue(String value) {
	        this.value = value;
	    }

	    public Integer getSort() {
	        return sort;
	    }

	    public void setSort(Integer sort) {
	        this.sort = sort;
	    }
}