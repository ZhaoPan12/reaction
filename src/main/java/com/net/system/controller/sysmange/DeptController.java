package com.net.system.controller.sysmange;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.net.common.annotation.OperationLog;
import com.net.common.util.ResultBean;
import com.net.system.model.Dept;
import com.net.system.service.sysmange.DeptService;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/dept")
@Api(tags = "DeptController", description = "部门管理类")
public class DeptController {

    @Resource
    private DeptService deptService;

    @GetMapping("/index")
    @ApiIgnore
    public String index() {
        return "dept/dept-list";
    }

    @OperationLog("根据父 ID 查询部门")
    @GetMapping("/list")
    @ResponseBody
    @ApiOperation(value = "根据父 ID 查询部门", notes="根据父 ID 查询部门")
    @ApiImplicitParam(name = "parentId", value = "父ID", required = false, dataType = "Integer")
    public ResultBean getList(@RequestParam(required = false) Integer parentId) {
        List<Dept> deptList = deptService.selectByParentId(parentId);
        return ResultBean.success(deptList);
    }
    @OperationLog("获取所有菜单并添加一个根节点 (树形结构)")
    @GetMapping("/tree/root")
    @ResponseBody
    @ApiOperation(value = "获取所有菜单并添加一个根节点 (树形结构)", notes="获取所有菜单并添加一个根节点 (树形结构)")
    public ResultBean treeAndRoot() {
        return ResultBean.success(deptService.selectAllDeptTreeAndRoot());
    }
    @OperationLog("查找所有的部门的树形结构")
    @GetMapping("/tree")
    @ResponseBody
    @ApiOperation(value = "查找所有的部门的树形结构", notes="查找所有的部门的树形结构")
    public ResultBean tree() {
        return ResultBean.success(deptService.selectAllDeptTree());
    }

    @GetMapping
    @ApiIgnore
    public String add() {
        return "dept/dept-add";
    }
    
    

    @OperationLog("新增部门")
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "新增部门", notes="新增部门")
    @ApiImplicitParam(name = "dept", value = "部门实体", required = false, dataType = "Dept")
    public ResultBean add(Dept dept) {
        return ResultBean.success(deptService.insert(dept));
    }

    @OperationLog("删除部门")
    @DeleteMapping("/{deptId}")
    @ResponseBody
    @ApiOperation(value = "删除部门", notes="删除部门")
    @ApiImplicitParam(name = "deptId", value = "部门ID", required = true, dataType = "Integer")
    public ResultBean delete(@PathVariable("deptId") Integer deptId) {
        deptService.deleteCascadeByID(deptId);
        return ResultBean.success();
    }

    @OperationLog("修改部门")
    @PutMapping
    @ResponseBody
    @ApiOperation(value = "修改部门", notes="修改部门")
    @ApiImplicitParam(name = "deptId", value = "部门ID", required = true, dataType = "Integer")
    public ResultBean update(Dept dept) {
        deptService.updateByPrimaryKey(dept);
        return ResultBean.success();
    }
    
    

    @ApiIgnore
    @GetMapping("/{deptId}")
    public String update(@PathVariable("deptId") Integer deptId, Model model) {
        Dept dept = deptService.selectByPrimaryKey(deptId);
        model.addAttribute("dept", dept);
        return "dept/dept-add";
    }

    @OperationLog("调整部门排序")
    @PostMapping("/swap")
    @ResponseBody
    @ApiOperation(value = "调整部门排序", notes="调整部门排序")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "currentId", value = "选中部门ID", required = true, dataType = "Integer"),
    	@ApiImplicitParam(name = "swapId", value = "交换部门ID", required = true, dataType = "Integer")
    })
    public ResultBean swapSort(Integer currentId, Integer swapId) {
        deptService.swapSort(currentId, swapId);
        return ResultBean.success();
    }

}
