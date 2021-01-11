package com.net.system.model;


public class Tichtext {
    /**
     * id
     */
    private String id;

    /**
     * 字典表id
     */
    private String itemId;

    /**
     * 内容
     */
    private String content;

    /**
     * 产品id
     */
    private String productid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }
}
