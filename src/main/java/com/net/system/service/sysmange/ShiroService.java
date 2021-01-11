package com.net.system.service.sysmange;

import java.util.Map;


public interface ShiroService {


    /**
     * 从数据库加载用户拥有的菜单权限和 API 权限.
     */
    public Map<String, String> getUrlPermsMap();

    /**
     * 更新 Shiro 过滤器链
     */
    public void updateFilterChain();
}