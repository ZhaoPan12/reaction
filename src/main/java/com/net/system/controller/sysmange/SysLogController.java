package com.net.system.controller.sysmange;

import com.github.pagehelper.PageInfo;
import com.net.common.annotation.OperationLog;
import com.net.common.util.PageResultBean;
import com.net.system.model.SysLog;
import com.net.system.service.sysmange.SysLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/log/sys")
@Api(tags = "SysLogController", description = "操作日志管理类")
public class SysLogController {

    @Resource
    private SysLogService sysLogService;

    @GetMapping("/index")
    @ApiIgnore
    public String index() {
        return "log/sys-logs";
    }

    @OperationLog("查看操作日志")
    @GetMapping("/list")
    @ResponseBody
    @ApiOperation(value = "查看操作日志", notes="查看操作日志")
    @ApiImplicitParams({
		   @ApiImplicitParam(name = "page", value = "当前页",  required = true, dataType = "Integer"),
		   @ApiImplicitParam(name = "limit", value = "每页数量", required = true, dataType = "Integer")
	})
    public PageResultBean<SysLog> getList(@RequestParam(value = "page", defaultValue = "1") int page,
                                          @RequestParam(value = "limit", defaultValue = "10")int limit) {
        List<SysLog> loginLogs = sysLogService.selectAll(page, limit);
        PageInfo<SysLog> rolePageInfo = new PageInfo<>(loginLogs);
        return new PageResultBean<>(rolePageInfo.getTotal(), rolePageInfo.getList());
    }

}
