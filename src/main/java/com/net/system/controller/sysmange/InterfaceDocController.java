package com.net.system.controller.sysmange;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class InterfaceDocController {

		@GetMapping("/swagger-ui")
	    public String login() {
	        return "swagger-ui";
	    }
}
