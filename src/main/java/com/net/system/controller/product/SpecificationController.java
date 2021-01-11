package com.net.system.controller.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.net.common.annotation.OperationLog;
import com.net.common.util.PageResultBean;
import com.net.system.model.Product;
import com.net.system.model.Specification;
import com.net.system.model.Tup;
import com.net.system.service.product.SpecificationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/specification")
@Api(tags = "SpecificationController", description = "分类参数管理类")
public class SpecificationController {

	@Autowired
	private SpecificationService specificationService;
	
    @GetMapping("/index")
    @ApiIgnore
    public String index(Model model) {
        return "attributes/attributes-list";
    }
    
    
    @OperationLog("查询分类参数列表")
    @GetMapping("/findPageWithResult")
    @ResponseBody
    @ApiOperation(value = "查询分类参数列表", notes="查询分类参数列表")
    @ApiImplicitParam(name = "map", value = "参数实体",  required = true, dataType = "map")
    public PageResultBean<Specification> findPageWithResult(HttpServletRequest request,@RequestParam Map<String,Object> map) {
    	List<Specification> list= specificationService.findPageWithResult(map);
    	PageInfo<Specification> specificationInfo = new PageInfo<>(list);
    	return new PageResultBean<>(specificationInfo.getTotal(), specificationInfo.getList());
    }
}
