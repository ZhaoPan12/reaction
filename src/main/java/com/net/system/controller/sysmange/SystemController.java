package com.net.system.controller.sysmange;

import springfox.documentation.annotations.ApiIgnore;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.net.common.annotation.OperationLog;
import com.net.common.information.Server;

@Controller
@ApiIgnore
public class SystemController {

    @OperationLog("查看系统信息")
    @GetMapping("/system/index")
    public String index(Model model) throws Exception {
        Server server = new Server();
        server.copyTo();
        model.addAttribute("server", server);
        return "system/index";
    }
}
