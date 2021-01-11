package com.net.system.controller.sysmange;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.net.common.annotation.OperationLog;
import com.net.common.annotation.RefreshFilterChain;
import com.net.common.util.ResultBean;
import com.net.system.model.Operator;
import com.net.system.service.sysmange.OperatorService;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/operator")
@Api(tags = "OperatorController", description = "操作权限管理类")
public class OperatorController {

    @Resource
    private OperatorService operatorService;

    @OperationLog("查看操作日志")
    @GetMapping("/index")
    @ApiIgnore
    public String index() {
        return "operator/operator-list";
    }

    @GetMapping
    @ApiIgnore
    public String add() {
        return "operator/operator-add";
    }

    @RefreshFilterChain
    @PostMapping
    @ResponseBody
    @OperationLog("新增操作权限")
    @ApiOperation(value = "新增操作权限", notes="新增操作权限")
    @ApiImplicitParam(name = "operator", value = "操作权限实体",  required = true, dataType = "Operator")
    public ResultBean add(Operator operator) {
        operatorService.add(operator);
        return ResultBean.success();
    }

    @GetMapping("/{operatorId}")
    @ApiIgnore
    public String update(Model model, @PathVariable("operatorId") Integer operatorId) {
        Operator operator = operatorService.selectByPrimaryKey(operatorId);
        model.addAttribute("operator", operator);
        return "operator/operator-add";
    }

    @RefreshFilterChain
    @PutMapping
    @ResponseBody
    @OperationLog("编辑操作权限")
    @ApiOperation(value = "编辑操作权限", notes="编辑操作权限")
    @ApiImplicitParam(name = "operator", value = "操作权限实体",  required = true, dataType = "Operator")
    public ResultBean update(Operator operator) {
        operatorService.updateByPrimaryKey(operator);
        return ResultBean.success();
    }

    @GetMapping("/list")
    @ResponseBody
    @OperationLog("根据菜单ID查询操作权限")
    @ApiOperation(value = "根据菜单ID查询操作权限", notes="根据菜单ID查询操作权限")
    @ApiImplicitParam(name = "menuId", value = "菜单ID",  required = false, dataType = "Integer")
    public ResultBean getList(@RequestParam(required = false) Integer menuId) {
        List<Operator> operatorList = operatorService.selectByMenuId(menuId);
        return ResultBean.success(operatorList);
    }

    @RefreshFilterChain
    @DeleteMapping("/{operatorId}")
    @ResponseBody
    @OperationLog("根据id删除操作权限")
    @ApiOperation(value = "根据id删除操作权限", notes="根据id删除操作权限")
    @ApiImplicitParam(name = "operatorId", value = "操作权限ID",  required = true, dataType = "Integer")
    public ResultBean delete(@PathVariable("operatorId") Integer operatorId) {
        operatorService.deleteByPrimaryKey(operatorId);
        return ResultBean.success();
    }


    @GetMapping("/tree")
    @ResponseBody
    @OperationLog("获取所有菜单的操作权限(树形结构)")
    @ApiOperation(value = "获取所有菜单的操作权限(树形结构)", notes="获取所有菜单的操作权限(树形结构)")
    public ResultBean tree() {
        return ResultBean.success(operatorService.getALLMenuAndOperatorTree());
    }

}
