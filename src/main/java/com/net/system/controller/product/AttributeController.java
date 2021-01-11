package com.net.system.controller.product;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.net.common.annotation.OperationLog;
import com.net.common.util.PageResultBean;
import com.net.common.util.ResultBean;
import com.net.system.model.Dept;
import com.net.system.model.ProductAttrKey;
import com.net.system.model.vo.ProductAttrKeyVo;
import com.net.system.service.product.ProductAttrKeyService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/attribute")
public class AttributeController {

	@Resource
	private ProductAttrKeyService productAttrKeyServie;

	@GetMapping("/index")
	@ApiIgnore
	public String index(Model model) {
		
		return "attribute/attributeKey-list";
	}

	@GetMapping("/add")
	@ApiIgnore
	public String add() {
		return "attribute/attributeKey-add";
	}

	@ApiIgnore
	@GetMapping("/edit/{id}")
	public String update(@PathVariable("id") Integer id, Model model) {
		ProductAttrKey productAttrKey = productAttrKeyServie.selectById(id);
		model.addAttribute("productAttrKey", productAttrKey);
		return "attribute/attributeKey-add";
	}

	@OperationLog("修改商品属性类型")
	@PutMapping
	@ResponseBody
	public ResultBean update(ProductAttrKey productAttrKey) {
		return ResultBean.success(productAttrKeyServie.update(productAttrKey));
	}

	@OperationLog("新增商品属性类型")
	@PostMapping
	@ResponseBody
	public ResultBean add(ProductAttrKey productAttrKey) {
		return ResultBean.success(productAttrKeyServie.insert(productAttrKey));
	}
	
	@OperationLog("删除部门")
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResultBean delete(@PathVariable("id") Integer deptId) {
        return ResultBean.success(productAttrKeyServie.delete(deptId));
    }

	@OperationLog("查询商品属性类型列表")
	@GetMapping("/findPageWithResult")
	@ResponseBody
	public PageResultBean<ProductAttrKeyVo> findPageWithResult(@RequestParam Map<String, Object> map) {
		List<ProductAttrKeyVo> list = productAttrKeyServie.findPageWithResult(map);
		PageInfo<ProductAttrKeyVo> pageInfo = new PageInfo<ProductAttrKeyVo>(list);
		return new PageResultBean<ProductAttrKeyVo>(pageInfo.getTotal(), pageInfo.getList());
	}
	
	
}
