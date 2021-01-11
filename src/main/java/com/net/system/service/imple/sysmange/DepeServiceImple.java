package com.net.system.service.imple.sysmange;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.net.common.util.ShiroUtil;
import com.net.system.mapper.sysmange.DeptMapper;
import com.net.system.model.Dept;
import com.net.system.model.User;
import com.net.system.service.sysmange.DeptService;

@Service
public class DepeServiceImple implements DeptService{

	@Resource
    private DeptMapper deptMapper;
	
	public Dept insert(Dept dept) {
        int maxOrderNum = deptMapper.selectMaxOrderNum();
        dept.setOrderNum(maxOrderNum + 1);
        dept.setCreateTime(new Date());
        deptMapper.insert(dept);
        return dept;
    }

    public int deleteByPrimaryKey(Integer deptId) {
        return deptMapper.deleteByPrimaryKey(deptId);
    }

    public Dept updateByPrimaryKey(Dept dept) {
        deptMapper.updateByPrimaryKey(dept);
        return dept;
    }

    public Dept selectByPrimaryKey(Integer deptId) {
        return deptMapper.selectByPrimaryKey(deptId);
    }


    /**
     * 删除当前部门及子部门.
     */
    public void deleteCascadeByID(Integer deptId) {

        List<Integer> childIDList = deptMapper.selectChildrenIDByPrimaryKey(deptId);
        for (Integer childId : childIDList) {
            deleteCascadeByID(childId);
        }
        deleteByPrimaryKey(deptId);
    }

    /**
     * 根据父 ID 查询部门
     */
    public List<Dept> selectByParentId(Integer parentId) {
        return deptMapper.selectByParentId(parentId);
    }

    /**
     * 查找所有的部门的树形结构
     */
    public List<Dept> selectAllDeptTree() {
        return deptMapper.selectAllTree();
    }

    /**
     * 获取所有菜单并添加一个根节点 (树形结构)
     */
    public List<Dept> selectAllDeptTreeAndRoot() {
    	User user=ShiroUtil.getCurrentUser();
    	//通过用户查询
    	Map<String,Object> map=new HashMap<String, Object>();
    	Dept dept=deptMapper.selectByPrimaryKey(user.getDeptId());
    	map.put("parent_id",user.getDeptId());
        List<Dept> deptList = deptMapper.selectAllTreeById(map);
        /**
        Dept root = new Dept();
        root.setDeptId(0);
        root.setDeptName("根部门");
        root.setChildren(deptList);**/
        dept.setChildren(deptList);      
        List<Dept> rootList = new ArrayList<>();
        rootList.add(dept);
        return rootList;
    }

    public void swapSort(Integer currentId, Integer swapId) {
        deptMapper.swapSort(currentId, swapId);
    }
}
