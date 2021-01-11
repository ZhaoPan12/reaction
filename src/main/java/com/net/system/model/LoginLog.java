package com.net.system.model;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class LoginLog{
	@ApiModelProperty(value="ID",name="id")
    private Integer id;

	@ApiModelProperty(value="登录时间",name="loginTime")
    private Date loginTime;

	@ApiModelProperty(value="登录用户",name="username")
    private String username;

	@ApiModelProperty(value="登录状态",name="loginStatus")
    private String loginStatus;

	@ApiModelProperty(value="登录ip",name="ip")
    private String ip;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}