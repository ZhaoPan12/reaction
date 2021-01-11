package com.net.system.model;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class SysLog {
	@ApiModelProperty(value="ID",name="id")
    private Integer id;

    /**
	* 用户名
	*/
    @ApiModelProperty(value="用户名",name="username")
    private String username;

    /**
	* 操作
	*/
    @ApiModelProperty(value="操作",name="operation")
    private String operation;

    /**
	* 响应时间/耗时
	*/
    @ApiModelProperty(value="响应时间/耗时",name="time")
    private Integer time;

    /**
	* 请求方法
	*/
    @ApiModelProperty(value="请求方法",name="method")
    private String method;

    /**
	* 请求参数
	*/
    @ApiModelProperty(value="请求参数",name="params")
    private String params;

    /**
	* IP
	*/
    @ApiModelProperty(value="IP",name="ip")
    private String ip;

    /**
	* 创建时间
	*/
    @ApiModelProperty(value="创建时间",name="createTime")
    private Date createTime;
    
    @ApiModelProperty(value="用户ID",name="userId")
    private Integer userId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "SysLog [id=" + id + ", username=" + username + ", operation=" + operation + ", time=" + time
				+ ", method=" + method + ", params=" + params + ", ip=" + ip + ", createTime=" + createTime + "]";
	}
    
}