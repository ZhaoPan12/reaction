package com.net.system.model;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

public class UserOnline implements Serializable {

    private static final long serialVersionUID = 3828664348416633856L;

    @ApiModelProperty(value="ID",name="id")
    private String id;
    
    @ApiModelProperty(value="用户ID",name="userId")
    private Integer userId;
    
    @ApiModelProperty(value="用户名",name="username")
    private String username;
    
    @ApiModelProperty(value="IP",name="ip")
    private String ip;
    
    @ApiModelProperty(value="状态",name="status")
    private String status;
    
    @ApiModelProperty(value="登录时间",name="startTimestamp")
    private Date startTimestamp;
    
    @ApiModelProperty(value="最后访问时间",name="lastAccessTime")
    private Date lastAccessTime;
    
    @ApiModelProperty(value="登出时间",name="timeout")
    private Long timeout;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Date startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }
}
