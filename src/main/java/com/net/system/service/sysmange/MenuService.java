package com.net.system.service.sysmange;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.net.common.shiro.ShiroActionProperties;
import com.net.common.util.ShiroUtil;
import com.net.common.util.TreeUtil;
import com.net.system.mapper.sysmange.MenuMapper;
import com.net.system.mapper.sysmange.OperatorMapper;
import com.net.system.mapper.sysmange.RoleMenuMapper;
import com.net.system.model.Menu;
import com.net.system.model.User;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


public interface MenuService {


    public Menu selectByPrimaryKey(Integer id);

    /**
     * 获取所有菜单
     */
    public List<Menu> selectAll();

    /**
     * 根据父 ID 获取所有菜单
     */
    public List<Menu> selectByParentId(Integer parentId);

    /**
     * 获取所有菜单 (树形结构)
     */
    public List<Menu> getALLTree();

    /**
     * 获取所有菜单并添加一个根节点 (树形结构)
     */
    public List<Menu> getALLMenuTreeAndRoot();

    /**
     * 获取所有菜单并统计菜单下的操作权限数 (树形结构)
     */
    public List<Menu> getALLMenuAndCountOperatorTree();

    /**
     * 获取当前登陆用户拥有的树形菜单 (admin 账户拥有所有权限.)
     */
    public List<Menu> selectCurrentUserMenuTree();

    /**
     * 获取指定用户拥有的树形菜单 (admin 账户拥有所有权限.)
     */
    public List<Menu> selectMenuTreeVOByUsername(String username);

    /**
     * 获取导航菜单中的所有叶子节点
     */
    public List<Menu> getLeafNodeMenu();
    
    public void insert(Menu menu);

    public void updateByPrimaryKey(Menu menu);


    /**
     * 删除当前菜单以及其子菜单
     */
    @Transactional
    public void deleteByIDAndChildren(Integer menuId);

    public int count();
    
    public void swapSort(Integer currentId, Integer swapId);

    /**
     * 转换为树形结构
     */
    public List<Menu> toTree(List<Menu> menuList);

    public List<Menu> getALLMenuAndCountOperatorTreeAndRoot();

    /**
     * 将树形结构添加到一个根节点下.
     */
    public List<Menu> addRootNode(String rootName, Integer rootId, List<Menu> children);
}