package com.net.system.service.imple.sysmange;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.net.common.util.ShiroUtil;
import com.net.system.mapper.sysmange.LoginLogMapper;
import com.net.system.model.LoginLog;
import com.net.system.model.User;
import com.net.system.service.sysmange.LoginLogService;

@Service
public class LoginLogServiceImple implements LoginLogService{

		@Resource
	    private LoginLogMapper loginLogMapper;

	    public void addLog(String username, boolean isAuthenticated, String ip) {
	        LoginLog loginLog = new LoginLog();
	        loginLog.setLoginTime(new Date());
	        loginLog.setUsername(username);
	        loginLog.setLoginStatus(isAuthenticated ? "1" : "0");
	        loginLog.setIp(ip);
	        loginLogMapper.insert(loginLog);
	    }

	    /**
	     * 最近一周登陆次数
	     */
	    public List<Integer> recentlyWeekLoginCount() {
	        User user = ShiroUtil.getCurrentUser();
	        return loginLogMapper.recentlyWeekLoginCount(user.getUsername());
	    }

	    public List<LoginLog> selectAll(int page, int limit) {
	        PageHelper.startPage(page, limit);
	        return loginLogMapper.selectAll();
	    }

	    public int count() {
	        return loginLogMapper.count();
	    }
}
