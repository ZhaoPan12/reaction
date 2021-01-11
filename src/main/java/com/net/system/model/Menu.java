package com.net.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Menu implements Serializable {
    private static final long serialVersionUID = 5561561457068906366L;

    @JsonProperty("id")
	@ApiModelProperty(value="菜单Id",name="menuId")
    private Integer menuId;

    @ApiModelProperty(value="上级Id",name="parentId")
    private Integer parentId;

    @JsonProperty("name")
    @ApiModelProperty(value="菜单名",name="menuName")
    private String menuName;

    @ApiModelProperty(value="菜单URL",name="url")
    private String url;

    @ApiModelProperty(value="权限标识符",name="perms")
    private String perms;

    @ApiModelProperty(value="排序号",name="orderNum")
    private Integer orderNum;

    @JsonIgnore
    @ApiModelProperty(value="创建时间",name="ip")
    private Date createTime;

    @JsonIgnore
    @ApiModelProperty(value="修改时间",name="modifyTime")
    private Date modifyTime;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String checkArr = "0";

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Menu> children;

    private String icon;

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
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

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
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

    public String getCheckArr() {
        return checkArr;
    }

    public void setCheckArr(String checkArr) {
        this.checkArr = checkArr;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}