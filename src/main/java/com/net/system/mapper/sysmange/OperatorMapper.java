package com.net.system.mapper.sysmange;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.net.system.model.Operator;

import java.util.List;

@Mapper
public interface OperatorMapper {
    int deleteByPrimaryKey(Integer operatorId);

    int insert(Operator operator);

    Operator selectByPrimaryKey(Integer operatorId);

    int updateByPrimaryKey(Operator operator);

    List<Operator> selectByMenuId(@Param("menuId") Integer menuId);

    List<Operator> selectAll();

    int deleteByMenuId(Integer menuId);
}