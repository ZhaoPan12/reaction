package com.net.system.controller.sysmange;

import com.github.pagehelper.PageInfo;
import com.net.common.annotation.OperationLog;
import com.net.common.util.PageResultBean;
import com.net.common.util.ResultBean;
import com.net.system.model.Role;
import com.net.system.service.sysmange.RoleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/role")
@Api(tags = "RoleController", description = "角色管理类")
public class RoleController {

    @Resource
    private RoleService roleService;

    @GetMapping("/index")
    @ApiIgnore
    public String index() {
        return "role/role-list";
    }

    @OperationLog("分页查询角色列表")
    @GetMapping("/list")
    @ResponseBody
    @ApiOperation(value = "分页查询角色列表", notes="分页查询角色列表")
    @ApiImplicitParams({
		   @ApiImplicitParam(name = "page", value = "当前页",  required = true, dataType = "Integer"),
		   @ApiImplicitParam(name = "limit", value = "每页数量", required = true, dataType = "Integer"),
		   @ApiImplicitParam(name = "roleQuery", value = "角色实体", required = false, dataType = "Role")
	})
    public PageResultBean<Role> getList(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "limit", defaultValue = "10")int limit,
                                        Role roleQuery) {
        List<Role> roles = roleService.selectAll(page, limit, roleQuery);
        PageInfo<Role> rolePageInfo = new PageInfo<>(roles);
        return new PageResultBean<>(rolePageInfo.getTotal(), rolePageInfo.getList());
    }

    @GetMapping
    @ApiIgnore
    public String add() {
        return "role/role-add";
    }

    @OperationLog("新增角色")
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "新增角色", notes="新增角色")
    @ApiImplicitParam(name = "role", value = "角色实体",  required = true, dataType = "Role")
    public ResultBean add(Role role) {
        roleService.add(role);
        return ResultBean.success();
    }

    @GetMapping("/{roleId}")
    @ApiIgnore
    public String update(@PathVariable("roleId") Integer roleId, Model model) {
        Role role = roleService.selectOne(roleId);
        model.addAttribute("role", role);
        return "role/role-add";
    }

    @OperationLog("修改角色")
    @PutMapping
    @ResponseBody
    @ApiOperation(value = "修改角色", notes="修改角色")
    @ApiImplicitParam(name = "role", value = "角色实体",  required = true, dataType = "Role")
    public ResultBean update(Role role) {
        roleService.update(role);
        return ResultBean.success();
    }


    @OperationLog("删除角色")
    @DeleteMapping("/{roleId}")
    @ResponseBody
    @ApiOperation(value = "删除角色", notes="删除角色")
    @ApiImplicitParam(name = "roleId", value = "角色Id",  required = true, dataType = "Integer")
    public ResultBean delete(@PathVariable("roleId") Integer roleId) {
        roleService.delete(roleId);
        return ResultBean.success();
    }

    @OperationLog("为角色授予菜单")
    @PostMapping("/{roleId}/grant/menu")
    @ResponseBody
    @ApiOperation(value = "为角色授予菜单", notes="为角色授予菜单")
    @ApiImplicitParams({
		   @ApiImplicitParam(name = "roleId", value = "角色Id",  required = true, dataType = "Integer"),
		   @ApiImplicitParam(name = "menuIds", value = "菜单Id数组", required = true)
	})
    public ResultBean grantMenu(@PathVariable("roleId") Integer roleId, @RequestParam(value = "menuIds[]", required = false) Integer[] menuIds) {
        roleService.grantMenu(roleId, menuIds);
        return ResultBean.success();
    }


    @OperationLog("为角色授予操作权限")
    @PostMapping("/{roleId}/grant/operator")
    @ResponseBody
    @ApiOperation(value = "为角色授予操作权限", notes="为角色授予操作权限")
    @ApiImplicitParams({
		   @ApiImplicitParam(name = "roleId", value = "角色Id",  required = true, dataType = "Integer"),
		   @ApiImplicitParam(name = "operatorIds", value = "操作权限Id数组", required = true)
	})
    public ResultBean grantOperator(@PathVariable("roleId") Integer roleId, @RequestParam(value = "operatorIds[]", required = false) Integer[] operatorIds) {
        roleService.grantOperator(roleId, operatorIds);
        return ResultBean.success();
    }

    /**
     * 获取角色拥有的菜单
     */
    @OperationLog("为角色授予操作权限")
    @GetMapping("/{roleId}/own/menu")
    @ResponseBody
    @ApiOperation(value = "获取角色拥有的菜单", notes="获取角色拥有的菜单")
    @ApiImplicitParam(name = "roleId", value = "角色Id", required = true, dataType = "Integer")
    public ResultBean getRoleOwnMenu(@PathVariable("roleId") Integer roleId) {
        return ResultBean.success(roleService.getMenusByRoleId(roleId));
    }

    /**
     * 获取角色拥有的操作权限
     */
    @OperationLog("获取角色拥有的操作权限")
    @GetMapping("/{roleId}/own/operator")
    @ResponseBody
    @ApiOperation(value = "获取角色拥有的操作权限", notes="获取角色拥有的操作权限")
    @ApiImplicitParam(name = "roleId", value = "角色Id", required = true, dataType = "Integer")
    public ResultBean getRoleOwnOperator(@PathVariable("roleId") Integer roleId) {
        Integer[] operatorIds = roleService.getOperatorsByRoleId(roleId);
        for (int i = 0; i < operatorIds.length; i++) {
            operatorIds[i] = operatorIds[i] + 10000;
        }
        return ResultBean.success(operatorIds);
    }
}
