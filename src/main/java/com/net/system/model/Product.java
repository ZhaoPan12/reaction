package com.net.system.model;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Product implements Serializable {
    /**
     * 商品id
     */
    private String id;

    /**
     * 产品名称
     */
    private String productname;

    /**
     * 主图地址
     */
    private String mainimage;

    /**
     * 大标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 产品类型
     */
    private String type;

    /**
     * 地址
     */
    private String address;

    /**
     * 状态(0.不可用 1.可用)
     */
    private Integer status;
    
    /**
     * 权重值
     */
    private Integer weights;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 创建人
     */
    private Integer createuser;

    /**
     * 更新时间
     */
    private Date updatetime;
    /**
         * 说明
     */
    private String description;
    
    /**
         * 景区列表
     */
    private String tabletext;

    /**
         * 分类id
     */
    private String classify;
    
    /**
         * 库存
     */
    private Integer stock;
    
    
    private String unit;
    
    private String supId;
    
    private String supName;
    
    private List<Files> imageList;
    
    
    private List<Label> labelList;
    
    private String classifyName;

    
    private String productSn;

    private String cardType;
    
    private String channelId;
    
    private Integer isToll;
    
    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getMainimage() {
        return mainimage;
    }

    public void setMainimage(String mainimage) {
        this.mainimage = mainimage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getCreateuser() {
        return createuser;
    }

    public void setCreateuser(Integer createuser) {
        this.createuser = createuser;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

	public Integer getWeights() {
		return weights;
	}

	public void setWeights(Integer weights) {
		this.weights = weights;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTabletext() {
		return tabletext;
	}

	public void setTabletext(String tabletext) {
		this.tabletext = tabletext;
	}

	public String getClassify() {
		return classify;
	}

	public void setClassify(String classify) {
		this.classify = classify;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public List<Files> getImageList() {
		return imageList;
	}

	public void setImageList(List<Files> imageList) {
		this.imageList = imageList;
	}

	public List<Label> getLabelList() {
		return labelList;
	}

	public void setLabelList(List<Label> labelList) {
		this.labelList = labelList;
	}

	public String getClassifyName() {
		return classifyName;
	}

	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getSupId() {
		return supId;
	}

	public void setSupId(String supId) {
		this.supId = supId;
	}

	public String getSupName() {
		return supName;
	}

	public void setSupName(String supName) {
		this.supName = supName;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	
    
    public String getProductSn() {
		return productSn;
	}

	public void setProductSn(String productSn) {
		this.productSn = productSn;
	}

	public Integer getIsToll() {
		return isToll;
	}

	public void setIsToll(Integer isToll) {
		this.isToll = isToll;
	}

    
    
}