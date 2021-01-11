package com.net.system.controller.sysmange;

import com.github.pagehelper.PageInfo;
import com.net.common.annotation.OperationLog;
import com.net.common.util.PageResultBean;
import com.net.common.util.ResultBean;
import com.net.common.validate.groups.Create;
import com.net.system.model.User;
import com.net.system.service.sysmange.RoleService;
import com.net.system.service.sysmange.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/user")
@Api(tags = "UserController", description = "用户管理类")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @GetMapping("/index")
    @ApiIgnore
    public String index() {
        return "user/user-list";
    }

    @OperationLog("获取用户列表")
    @GetMapping("/list")
    @ResponseBody
    @ApiOperation(value = "获取用户列表", notes="分页获取用户列表")
    @ApiImplicitParams({
    				   @ApiImplicitParam(name = "page", value = "当前页",  required = true, dataType = "Integer"),
    				   @ApiImplicitParam(name = "limit", value = "每页数量", required = true, dataType = "Integer"),
    				   @ApiImplicitParam(name = "userQuery", value = "用户实体", required = false, dataType = "User")
    })
    public PageResultBean<User> getList(@RequestParam(value = "page", defaultValue = "1") int page,
                                        @RequestParam(value = "limit", defaultValue = "10") int limit,
                                        User userQuery) {
        List<User> users = userService.selectAllWithDept(page, limit, userQuery);
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        return new PageResultBean<>(userPageInfo.getTotal(), userPageInfo.getList());
    }

    @GetMapping
    @ApiIgnore
    public String add(Model model) {
        model.addAttribute("roles", roleService.selectAll());
        return "user/user-add";
    }

    @GetMapping("/{userId}")
    @ApiIgnore
    public String update(@PathVariable("userId") Integer userId, Model model) {
        model.addAttribute("roleIds", userService.selectRoleIdsById(userId));
        model.addAttribute("user", userService.selectOne(userId));
        model.addAttribute("roles", roleService.selectAll());
        return "user/user-add";
    }

    @OperationLog("编辑用户")
    @PutMapping
    @ResponseBody
    @ApiOperation(value="编辑用户", notes="编辑用户")
    public ResultBean update(@Valid User user, @RequestParam(value = "role[]", required = false) Integer[] roleIds) {
        userService.update(user, roleIds);
        return ResultBean.success();
    }

    @OperationLog("新增用户")
    @PostMapping
    @ResponseBody
    @ApiOperation(value="新增用户", notes="新增用户")
    @ApiImplicitParam(name = "user", value = "用户实体", required = true, dataType = "User")
    public ResultBean add(@Validated(Create.class) User user, @RequestParam(value = "role[]", required = false) Integer[] roleIds) {
        return ResultBean.success(userService.add(user, roleIds));
    }

    @OperationLog("禁用账号")
    @PostMapping("/{userId:\\d+}/disable")
    @ResponseBody
    @ApiOperation(value="禁用账号", notes="禁用账号")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer")
    public ResultBean disable(@PathVariable("userId") Integer userId) {
        return ResultBean.success(userService.disableUserByID(userId));
    }

    @OperationLog("激活账号")
    @PostMapping("/{userId}/enable")
    @ResponseBody
    @ApiOperation(value="激活账号", notes="激活账号")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer")
    public ResultBean enable(@PathVariable("userId") Integer userId) {
        return ResultBean.success(userService.enableUserByID(userId));
    }

    @OperationLog("删除账号")
    @DeleteMapping("/{userId}")
    @ResponseBody
    @ApiOperation(value="删除账号", notes="删除账号")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer")
    public ResultBean delete(@PathVariable("userId") Integer userId) {
        userService.delete(userId);
        return ResultBean.success();
    }

    @GetMapping("/{userId}/reset")
    @ApiIgnore
    public String resetPassword(@PathVariable("userId") Integer userId, Model model) {
        model.addAttribute("userId", userId);
        return "user/user-reset-pwd";
    }
    
    @OperationLog("重置密码")
    @PostMapping("/{userId}/reset")
    @ResponseBody
    @ApiOperation(value="重置密码", notes="重置密码")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer"),
    	@ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String")
    })
    public ResultBean resetPassword(@PathVariable("userId") Integer userId, String password) {
        userService.updatePasswordByUserId(userId, password);
        return ResultBean.success();
    }
}