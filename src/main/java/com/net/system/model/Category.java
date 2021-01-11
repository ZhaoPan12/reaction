package com.net.system.model;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class Category {
    /**
     * 类别id
     */
    private String fCateid;

    /**
     * 父类别id当id=0时说明是根节点,一级类别
     */
    private String fParentid;

    /**
     * 类别名称
     */
    private String fName;

    /**
     * 类别状态1-正常,2-已废弃
     */
    private Integer fStatus;

    /**
     * 排序编号,同类展示顺序,数值相等则自然排序
     */
    private Integer fSortorder;

    /**
     * 创建时间
     */
    private Date fCreatetime;

    /**
     * 更新时间
     */
    private Date fUpdatetime;
    
    private String imageUrl;
    
    private MultipartFile files;
    
    private List<Category> children;
    
    private String fileimg;

    private String originalUrl;
    
    private Integer count;
    public String getfCateid() {
        return fCateid;
    }

    public void setfCateid(String fCateid) {
        this.fCateid = fCateid;
    }

    public String getfParentid() {
        return fParentid;
    }

    public void setfParentid(String fParentid) {
        this.fParentid = fParentid;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public Integer getfStatus() {
        return fStatus;
    }

    public void setfStatus(Integer fStatus) {
        this.fStatus = fStatus;
    }

    public Integer getfSortorder() {
        return fSortorder;
    }

    public void setfSortorder(Integer fSortorder) {
        this.fSortorder = fSortorder;
    }

    public Date getfCreatetime() {
        return fCreatetime;
    }

    public void setfCreatetime(Date fCreatetime) {
        this.fCreatetime = fCreatetime;
    }

    public Date getfUpdatetime() {
        return fUpdatetime;
    }

    public void setfUpdatetime(Date fUpdatetime) {
        this.fUpdatetime = fUpdatetime;
    }

	public List<Category> getChildren() {
		return children;
	}

	public void setChildren(List<Category> children) {
		this.children = children;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public MultipartFile getFiles() {
		return files;
	}

	public void setFiles(MultipartFile files) {
		this.files = files;
	}

	public String getFileimg() {
		return fileimg;
	}

	public void setFileimg(String fileimg) {
		this.fileimg = fileimg;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
    
}
