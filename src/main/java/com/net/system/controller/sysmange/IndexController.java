package com.net.system.controller.sysmange;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.net.common.annotation.OperationLog;
import com.net.common.util.TreeUtil;
import com.net.system.model.Menu;
import com.net.system.service.*;
import com.net.system.service.sysmange.LoginLogService;
import com.net.system.service.sysmange.MenuService;
import com.net.system.service.sysmange.RoleService;
import com.net.system.service.sysmange.SysLogService;
import com.net.system.service.sysmange.UserOnlineService;
import com.net.system.service.sysmange.UserService;

import cn.hutool.http.HttpRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

@Controller
@Api(tags = "IndexController", description = "首页管理类")
public class IndexController {

    @Resource
    private MenuService menuService;

    @Resource
    private LoginLogService loginLogService;

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private SysLogService sysLogService;

    @Resource
    private UserOnlineService userOnlineService;

    @ApiIgnore
    @GetMapping(value = {"/", "/main"})
    public String index(HttpServletRequest request,Model model) {
    	String webName=request.getServletContext().getContextPath();
        List<Menu> menuTreeVOS = menuService.selectCurrentUserMenuTree();
        model.addAttribute("menus", TreeUtil.getMenu(webName, menuTreeVOS));
        model.addAttribute("webName", webName);
        return "index";
    }

    @OperationLog("访问我的桌面")
    @GetMapping("/welcome")
    @ApiIgnore
    public String welcome(Model model) {
        int userCount = userService.count();
        int roleCount = roleService.count();
        int menuCount = menuService.count();
        int loginLogCount = loginLogService.count();
        int sysLogCount = sysLogService.count();
        int userOnlineCount = userOnlineService.count();

        model.addAttribute("userCount", userCount);
        model.addAttribute("roleCount", roleCount);
        model.addAttribute("menuCount", menuCount);
        model.addAttribute("loginLogCount", loginLogCount);
        model.addAttribute("sysLogCount", sysLogCount);
        model.addAttribute("userOnlineCount", userOnlineCount);
        return "welcome";
    }

    @OperationLog("查看近七日登录统计图")
    @GetMapping("/weekLoginCount")
    @ResponseBody
    @ApiOperation(value = "查看近七日登录统计图", notes="查看近七日登录统计图")
    public List<Integer> recentlyWeekLoginCount() {
        return loginLogService.recentlyWeekLoginCount();
    }
}
