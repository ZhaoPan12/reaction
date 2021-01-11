package com.net.system.model;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class Operator {
    /**
     * 菜单 ID
     */
	@ApiModelProperty(value="菜单 ID",name="operatorId")
    private Integer operatorId;

    /**
     * 所属菜单 ID
     */
	@ApiModelProperty(value="所属菜单 ID",name="menuId")
    private Integer menuId;

    /**
     * 资源名称
     */
	@ApiModelProperty(value="资源名称",name="operatorName")
    private String operatorName;

    /**
     * 资源 URL
     */
	@ApiModelProperty(value="资源 URL",name="url")
    private String url;

    /**
     * 权限标识符
     */
	@ApiModelProperty(value="权限标识符",name="perms")
    private String perms;

    /**
     * 资源需要的 HTTP 请求方法
     */
	@ApiModelProperty(value="资源需要的 HTTP 请求方法",name="httpMethod")
    private String httpMethod;

    /**
     * 创建时间
     */
	@ApiModelProperty(value="创建时间",name="createTime")
    private Date createTime;

    /**
     * 修改时间
     */
	@ApiModelProperty(value="修改时间",name="modifyTime")
    private Date modifyTime;

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}