package com.net.system.controller.sysmange;

import io.swagger.annotations.Api;
import springfox.documentation.annotations.ApiIgnore;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.net.common.util.ResultBean;

/**
 * 安全相关 Controller
 */
@Controller
@RequestMapping("/security")
@ApiIgnore
public class SecurityController {

    /**
     * 判断当前登录用户是否有某权限
     */
    @GetMapping("/hasPermission/{perms}")
    @ResponseBody
    public ResultBean hasPermission(@PathVariable("perms") String perms) {
        return ResultBean.success(SecurityUtils.getSubject().isPermitted(perms));
    }

}
