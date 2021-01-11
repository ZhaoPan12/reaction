package com.net.system.service.sysmange;

import com.github.pagehelper.PageHelper;
import com.net.common.exception.DuplicateNameException;
import com.net.common.shiro.ShiroActionProperties;
import com.net.common.util.TreeUtil;
import com.net.system.mapper.sysmange.UserMapper;
import com.net.system.mapper.sysmange.UserRoleMapper;
import com.net.system.model.Menu;
import com.net.system.model.User;

import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public interface UserService {


    public List<User> selectAllWithDept(int page, int rows, User userQuery);

    public Integer[] selectRoleIdsById(Integer userId);

    
    public Integer add(User user, Integer[] roleIds);

    public void updateLastLoginTimeByUsername(String username);
    
    public boolean disableUserByID(Integer id);

    public boolean enableUserByID(Integer id);

    /**
     * 根据用户 ID 激活账号.
     * @param userId    用户 ID
     */
    public void activeUserByUserId(Integer userId);

    
    public boolean update(User user, Integer[] roleIds);

    public User selectOne(Integer id);

    /**
     * 新增时校验用户名是否重复
     * @param username  用户名
     */
    public void checkUserNameExistOnCreate(String username);

    public void checkUserNameExistOnUpdate(User user);

    public void offlineBySessionId(String sessionId);

    /**
     * 删除所有此用户的在线用户
     */
    public void offlineByUserId(Integer userId);

    
    public void grantRole(Integer userId, Integer[] roleIds);

    public User selectByActiveCode(String activeCode);

    public int count();
    
    public void delete(Integer userId);

    /**
     * 获取用户拥有的所有菜单权限和操作权限.
     * @param username      用户名
     * @return              权限标识符号列表
     */
    public Set<String> selectPermsByUsername(String username);

    public Set<String> selectRoleNameByUserName(String username);

    public User selectOneByUserName(String username);

    public void updatePasswordByUserId(Integer userId, String password);

    public String generateSalt();
    
    /**
     * 根据openId获取用户
     */
    User selectOneByOpenId(@Param("openId") String openId);
    
    /**
     * 新增微信用户
     */
    int insertWeChatUser(User user);
    
    /**
     * 修改微信用户
     */
    int updateWeChatUser(User user);
    
    User selectUserByPhone(String phone);
    
    User selectUserByIdCard(String IdCard);
    
    int updateByPrimaryKeySelective(User user);
    
    
    
    int updateUserBalance(User user);
}