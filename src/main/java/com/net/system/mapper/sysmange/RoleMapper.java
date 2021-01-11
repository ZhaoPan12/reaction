package com.net.system.mapper.sysmange;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.Role;

import java.util.List;

@Mapper
public interface RoleMapper {
    int deleteByPrimaryKey(Integer roleId);

    int insert(Role role);

    Role selectByPrimaryKey(Integer roleId);

    int updateByPrimaryKey(Role role);

    List<Role> selectAll();

    List<Role> selectAllByQuery(Role roleQuery);

    int count();
}