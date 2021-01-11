package com.net.system.service.sysmange;

import java.util.List;

import com.net.system.model.Role;


public interface RoleService {

   
    public Role selectOne(Integer roleId);

    public List<Role> selectAll(int page, int limit, Role roleQuery);

    public List<Role> selectAll();

    public List<Role> selectAllByQuery(Role roleQuery);

    
    public void add(Role role);

    
    public void update(Role role);


    /**
     * 为角色分配菜单
     * @param roleId    角色 ID
     * @param menuIds   菜单 ID 数组
     */
    
    public void grantMenu(Integer roleId, Integer[] menuIds);

    /**
     * 为角色分配操作权限
     * @param roleId    角色 ID
     * @param operatorIds   操作权限 ID 数组
     */
    
    public void grantOperator(Integer roleId, Integer[] operatorIds);

    public int count();

    public void delete(Integer roleId);

    public Integer[] getMenusByRoleId(Integer roleId);

    public Integer[] getOperatorsByRoleId(Integer roleId);

    public void clearRoleAuthCache(Integer roleId);
}