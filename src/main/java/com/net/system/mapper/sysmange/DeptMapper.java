package com.net.system.mapper.sysmange;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.net.system.model.Dept;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeptMapper {

    int deleteByPrimaryKey(Integer deptId);

    int insert(Dept dept);

    Dept selectByPrimaryKey(Integer deptId);

    int updateByPrimaryKey(Dept dept);

    List<Dept> selectByParentId(@Param("parentId") Integer parentId);

    List<Dept> selectAllTree();
    
    List<Dept> selectAllTreeById(Map<String,Object> map);

    List<Integer> selectChildrenIDByPrimaryKey(@Param("deptId") Integer deptId);

    int selectMaxOrderNum();

    int swapSort(@Param("currentId") Integer currentId, @Param("swapId") Integer swapId);
    
    List<String> getDeptChildNode(String deptId);
    
    

}