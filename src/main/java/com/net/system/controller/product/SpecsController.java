package com.net.system.controller.product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.net.common.annotation.OperationLog;
import com.net.common.util.PageResultBean;
import com.net.common.util.ResultBean;
import com.net.system.model.ProductAttrKey;
import com.net.system.model.ProductAttrVal;
import com.net.system.model.vo.ProductAttrKeyVo;
import com.net.system.model.vo.ProductAttrValVo;
import com.net.system.model.vo.ProductSkuVo;
import com.net.system.service.product.ProductAttrKeyService;
import com.net.system.service.product.ProductAttrValService;
import com.net.system.service.product.ProductSkuService;

import cn.hutool.json.JSONObject;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/specs")
public class SpecsController {

	@Resource
	private ProductAttrValService productAttrValService;

	@Resource
	private ProductAttrKeyService productAttrKeyServie;

	@Resource
	private ProductSkuService productSkuService;
	
	@Value("${photo.resourceLocations}")
	private   String IMG_PATH_PREFIX;
	
	@Value("${photo.photoPath}")
	private   String photoPath;

	@GetMapping("/index/{attrId}")
	@ApiIgnore
	public String index(@PathVariable("attrId") Integer attrId, Model model) {

		model.addAttribute("attrId", attrId);
		return "attribute/attributeVal-list";
	}

	@GetMapping("/add")
	@ApiIgnore
	public String add(Model model) {
		List<ProductAttrKeyVo> list = productAttrKeyServie.findAttrKeyAll();
		model.addAttribute("attrKeyList", list);
		return "attribute/attributeVal-add";
	}

	@ApiIgnore
	@GetMapping("/edit/{id}")
	public String update(@PathVariable("id") Integer id, Model model) {
		List<ProductAttrKeyVo> list = productAttrKeyServie.findAttrKeyAll();
		ProductAttrVal productAttrVal = productAttrValService.selectById(id);
		model.addAttribute("productAttrVal", productAttrVal);
		model.addAttribute("attrKeyList", list);
		return "attribute/attributeVal-add";
	}

	@OperationLog("修改商品属性类型")
	@PutMapping
	@ResponseBody
	public ResultBean update(ProductAttrVal productAttrVal) {
		return productAttrValService.update(productAttrVal);
	}

	@OperationLog("新增商品属性类型")
	@PostMapping
	@ResponseBody
	public ResultBean add(ProductAttrVal productAttrVal) {
		return productAttrValService.insert(productAttrVal);
	}

	@OperationLog("删除规格")
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResultBean delete(@PathVariable("id") Integer id) {
		ProductAttrVal productAttrVal = new ProductAttrVal();
		productAttrVal.setId(id);
		return ResultBean.success(productAttrValService.delete(productAttrVal));
	}

	@OperationLog("查询属性规格列表")
	@GetMapping("/findPageWithResult")
	@ResponseBody
	public PageResultBean<ProductAttrValVo> findPageWithResult(@RequestParam Map<String, Object> map) {
		List<ProductAttrValVo> list = productAttrValService.findPageWithResult(map);
		PageInfo<ProductAttrValVo> pageInfo = new PageInfo<ProductAttrValVo>(list);
		return new PageResultBean<ProductAttrValVo>(pageInfo.getTotal(), pageInfo.getList());
	}

	@OperationLog("查询属性值")
	@GetMapping("/findByAttrId")
	@ResponseBody
	public ResultBean findByAttrId(@RequestParam Map<String, Object> map) {
		List<ProductAttrValVo> list = productAttrValService
				.findByAttrId(Integer.parseInt(map.get("attrId").toString()));
		return ResultBean.success(list);
	}

	@OperationLog("设置商品规格")
	@PostMapping("/setSpecs")
	@ResponseBody
	public ResultBean setSpecs(@RequestBody ProductSkuVo skuVo) {

		return ResultBean.success(productSkuService.setSpecs(skuVo));
	}
	
	@OperationLog("修改商品规格")
	@PostMapping("/update")
	@ResponseBody
	public ResultBean update(@RequestBody ProductSkuVo skuVo) {

		return ResultBean.success(productSkuService.update(skuVo));
	}
	
	
	
	@PostMapping(value="/upload/images")
    @ResponseBody
    public ResultBean uploadimages(MultipartFile file) throws IOException {
        return ResultBean.successData(uploadImage(file));
    }

	public String uploadImage(MultipartFile file) throws IllegalStateException, IOException {
		String filename = UUID.randomUUID().toString().replaceAll("-", "");
		String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
		String filenames = filename + ext;
		String tagFilePath = IMG_PATH_PREFIX + filenames;// 单文件
		File dest = new File(tagFilePath);
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();
		}
		// 执行流写入
		file.transferTo(dest);
		return photoPath + filenames;
	}

}
