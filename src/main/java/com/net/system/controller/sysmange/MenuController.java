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
import com.net.common.annotation.RefreshFilterChain;
import com.net.common.util.ResultBean;
import com.net.system.model.Menu;
import com.net.system.service.sysmange.MenuService;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/menu")
@Api(tags = "MenuController", description = "菜单管理类")
public class MenuController {

    @Resource
    private MenuService menuService;

    @GetMapping("/index")
    @ApiIgnore
    public String index() {
        return "menu/menu-list";
    }

    @OperationLog("获取菜单列表")
    @GetMapping("/list")
    @ResponseBody
    @ApiOperation(value = "获取菜单列表", notes="获取菜单列表")
    @ApiImplicitParam(name = "parentId", value = "菜单ID",  required = false, dataType = "Integer")
    public ResultBean getList(@RequestParam(required = false) Integer parentId) {
        List<Menu> menuList = menuService.selectByParentId(parentId);
        return ResultBean.success(menuList);
    }

    @GetMapping
    @ApiIgnore
    public String add(Model model) {
        return "menu/menu-add";
    }

    @OperationLog("获取所有菜单")
    @GetMapping("/tree")
    @ResponseBody
    @ApiOperation(value = "获取所有菜单", notes="获取所有菜单")
    public ResultBean tree() {
        return ResultBean.success(menuService.getALLTree());
    }
    
    @OperationLog("获取所有菜单并添加一个根节点 (树形结构)")
    @GetMapping("/tree/root")
    @ResponseBody
    @ApiIgnore
    //@ApiOperation(value = "获取所有菜单并添加一个根节点 (树形结构)", notes="获取所有菜单并添加一个根节点 (树形结构)")
    public ResultBean treeAndRoot() {
        return ResultBean.success(menuService.getALLMenuTreeAndRoot());
    }

    @GetMapping("/tree/root/operator")
    @ResponseBody
    @ApiIgnore
    public ResultBean menuAndCountOperatorTreeAndRoot() {
        return ResultBean.success(menuService.getALLMenuAndCountOperatorTreeAndRoot());
    }

    @OperationLog("新增菜单")
    @RefreshFilterChain
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "新增菜单", notes="新增菜单")
    @ApiImplicitParam(name = "menu", value = "菜单实体",  required = false, dataType = "Menu")
    public ResultBean add(Menu menu) {
        menuService.insert(menu);
        return ResultBean.success();
    }

    @OperationLog("删除菜单")
    @RefreshFilterChain
    @DeleteMapping("/{menuId}")
    @ResponseBody
    @ApiOperation(value = "删除菜单", notes="删除菜单")
    @ApiImplicitParam(name = "menuId", value = "菜单ID",  required = false, dataType = "Integer")
    public ResultBean delete(@PathVariable("menuId") Integer menuId) {
        menuService.deleteByIDAndChildren(menuId);
        return ResultBean.success();
    }

    @GetMapping("/{menuId}")
    @ApiIgnore
    public String updateMenu(@PathVariable("menuId") Integer menuId, Model model) {
        Menu menu = menuService.selectByPrimaryKey(menuId);
        model.addAttribute("menu", menu);
        return "menu/menu-add";
    }

    @OperationLog("修改菜单")
    @RefreshFilterChain
    @PutMapping
    @ResponseBody
    @ApiOperation(value = "修改菜单", notes="修改菜单")
    public ResultBean update(Menu menu) {
        menuService.updateByPrimaryKey(menu);
        return ResultBean.success();
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
        menuService.swapSort(currentId, swapId);
        return ResultBean.success();
    }
}