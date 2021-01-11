package com.net.system.controller.sysmange;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.net.common.annotation.OperationLog;
import com.net.common.util.PageResultBean;
import com.net.common.util.ResultBean;
import com.net.system.model.UserOnline;
import com.net.system.service.sysmange.UserOnlineService;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/online")
@Api(tags = "UserOnlineController", description = "在线用户管理类")
public class UserOnlineController {

    @Resource
    private UserOnlineService userOnlineService;

    @GetMapping("/index")
    @ApiIgnore
    public String index() {
        return "online/user-online-list";
    }

    @OperationLog("获取在线用户列表")
    @GetMapping("/list")
    @ResponseBody
    @ApiOperation(value = "获取在线用户列表", notes="获取在线用户列表")
    public PageResultBean<UserOnline> online() {
        List<UserOnline> list = userOnlineService.list();
        return new PageResultBean<>(list.size(), list);
    }

    @OperationLog("根据SessionId踢出用户")
    @PostMapping("/kickout")
    @ResponseBody
    @ApiOperation(value = "根据SessionId踢出用户", notes="根据SessionId踢出用户")
    @ApiImplicitParam(name = "sessionId", value = "Session会话Id",  required = true)
    public ResultBean forceLogout(String sessionId) {
        userOnlineService.forceLogout(sessionId);
        return ResultBean.success();
    }
}
