package com.net.system.service.sysmange;

import java.util.List;

import com.net.system.model.Dept;

/**
 * 部门
 * @author 双木临夕
 *
 */

public interface DeptService {

	public Dept insert(Dept dept);
	
	public int deleteByPrimaryKey(Integer deptId);

	public Dept updateByPrimaryKey(Dept dept);
	
	public Dept selectByPrimaryKey(Integer deptId);
	
	public void deleteCascadeByID(Integer deptId);
	
	public List<Dept> selectByParentId(Integer parentId);
	
	public List<Dept> selectAllDeptTree();
	
	public List<Dept> selectAllDeptTreeAndRoot();
	
	public void swapSort(Integer currentId, Integer swapId);
}
