package com.net.system.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.net.common.validate.groups.Create;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel(description="用户对象user")
public class User implements Serializable {

    private static final long serialVersionUID = -3200103254689137288L;
    
    @ApiModelProperty(value="用户ID",name="userId")
    private Integer userId;
    
    @ApiModelProperty(value="用户名",name="username")
    @NotBlank(message = "用户名不能为空")
    private String username;

    
    @NotBlank(message = "密码不能为空", groups = Create.class)
    private String password;

    @JsonIgnore
    private String salt;
    
    @ApiModelProperty(value="用户邮箱",name="email")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @ApiModelProperty(value="用户状态",name="status")
    private String status;
    
    @ApiModelProperty(value="最后登录时间",name="lostLoginTime")
    private Date lastLoginTime;
    
    @ApiModelProperty(value="创建时间",name="createTime")
    private Date createTime;

    @JsonIgnore
    @ApiModelProperty(value="修改时间",name="modifyTime")
    private Date modifyTime;

    @JsonIgnore
    @ApiModelProperty(value="注册激活码",name="activeCode")
    private String activeCode;

    @ApiModelProperty(value="部门ID",name="deptId")
    private Integer deptId;
    
    @ApiModelProperty(value="部门名",name="deptName")
    private String deptName;

    private String openId;
    
    @ApiModelProperty(value="key",name="skey")
    private String skey;
    
    @ApiModelProperty(value="sessionid",name="sessionId")
    private String sessionId;
    
    private Integer sex;
    
    @ApiModelProperty(value="身份证",name="idCard")
    private String idCard;
    
    private String phone;
    
    private String realName;
    
    private BigDecimal balance;
    
    private String imageUrl;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
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

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    
    

    public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getSkey() {
		return skey;
	}

	public void setSkey(String skey) {
		this.skey = skey;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	
	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", salt=" + salt
				+ ", email=" + email + ", status=" + status + ", lastLoginTime=" + lastLoginTime + ", createTime="
				+ createTime + ", modifyTime=" + modifyTime + ", activeCode=" + activeCode + ", deptId=" + deptId
				+ ", deptName=" + deptName + ", openId=" + openId + ", skey=" + skey + ", sessionId=" + sessionId
				+ ", sex=" + sex + ", idCard=" + idCard + ", phone=" + phone + ", realName=" + realName + ", balance="
				+ balance + ", imageUrl=" + imageUrl + "]";
	}



}