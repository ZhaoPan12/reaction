package com.net.system.service.sysmange;

import java.util.List;

import com.net.system.model.LoginLog;

/**
 * 登陆日志 Service
 */

public interface LoginLogService {


    public void addLog(String username, boolean isAuthenticated, String ip);

    /**
     * 最近一周登陆次数
     */
    public List<Integer> recentlyWeekLoginCount();

    public List<LoginLog> selectAll(int page, int limit);

    public int count();
}