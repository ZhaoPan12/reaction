package com.net.system.model;


import java.util.Date;

public class Specification {
    /**
     * id
     */
    private Integer id;

    /**
     * 分类id
     */
    private String cateid;

    /**
     * 上级id
     */
    private Integer pid;

    /**
     * 名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer solr;

    /**
     * 图片地址
     */
    private String pic;

    /**
     * 可选值
     */
    private String sevalue;

    /**
     * 是否使用列表的值
     */
    private Integer istable;

    /**
     * 是否支持手动新增
     */
    private Integer ismanual;

    /**
     * 创建时间
     */
    private Date createtime;
    
    private Integer count;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCateid() {
        return cateid;
    }

    public void setCateid(String cateid) {
        this.cateid = cateid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSolr() {
        return solr;
    }

    public void setSolr(Integer solr) {
        this.solr = solr;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSevalue() {
        return sevalue;
    }

    public void setSevalue(String sevalue) {
        this.sevalue = sevalue;
    }

    public Integer getIstable() {
        return istable;
    }

    public void setIstable(Integer istable) {
        this.istable = istable;
    }

    public Integer getIsmanual() {
        return ismanual;
    }

    public void setIsmanual(Integer ismanual) {
        this.ismanual = ismanual;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
    
}