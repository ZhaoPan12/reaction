package com.net.system.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author 双木临夕
 *
 */
public class UserAuths {
    /**
     * 主键
     */
	@ApiModelProperty(value="Id",name="id")
    private Integer id;

    /**
     * 用户 ID
     */
	@ApiModelProperty(value="用户 ID",name="userId")
    private Integer userId;

    /**
     * 登录类型
     */
	@ApiModelProperty(value="登录类型",name="identityType")
    private String identityType;

    /**
     * 第三方登录的用户名
     */
	@ApiModelProperty(value="第三方登录的用户名",name="identifier")
    private String identifier;

    /**
     * 第三方登录 token
     */
	@ApiModelProperty(value="第三方登录 token",name="credential")
    private String credential;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }
}