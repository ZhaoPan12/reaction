package com.net.system.controller.product;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.net.common.annotation.OperationLog;
import com.net.common.interceptor.Authorize;
import com.net.common.interceptor.SecurityParameter;
import com.net.common.util.AesEncryptUtil;
import com.net.common.util.FileUtil;
import com.net.common.util.ResultBean;
import com.net.system.model.Category;
import com.net.system.model.Dept;
import com.net.system.model.Product;
import com.net.system.model.User;
import com.net.system.service.product.CategoryService;
import com.net.system.service.product.ProductService;
import com.net.system.service.sysmange.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/category")
@Api(tags = "CategoryController", description = "商品分类管理类")
public class CategoryController {
	@Resource
	private CategoryService categoryService;
	
	@Resource
    private ProductService productService;
	
	private AesEncryptUtil aesEncryptUtil=AesEncryptUtil.getAesEncryptUtil();;
	
	 @Value("${photo.resourceLocations}")
	private   String IMG_PATH_PREFIX;
	    
	@Value("${photo.photoPath}")
	private   String photoPath;
	@Resource
	private UserService userService;
	@GetMapping("/index")
    @ApiIgnore
    public String index() {
        return "category/category-list";
    }
	
	@GetMapping
    @ApiIgnore
    public String add() {
        return "category/category-add";
    }
	
	@OperationLog("根据父 ID 查询商品分类")
    @GetMapping("/list")
    @ResponseBody
    @ApiOperation(value = "根据父 ID 查询商品分类", notes="根据父 ID 查询商品分类")
    @ApiImplicitParam(name = "fParentid", value = "父ID", required = false, dataType = "String")
    public ResultBean getList(@RequestParam(required = false) String fParentid) {
        List<Category> categoryList = categoryService.selectByfparentid(fParentid);
        return ResultBean.success(categoryList);
    }
	
	@OperationLog("获取所有分类并添加一个根节点 (树形结构)")
    @GetMapping("/tree/root")
    @ResponseBody
    @ApiOperation(value = "获取所有分类并添加一个根节点 (树形结构)", notes="获取所有分类并添加一个根节点 (树形结构)")
    public ResultBean treeAndRoot() {
        return ResultBean.success(categoryService.selectAllCategoryTreeAndRoot());
    }
	
	@OperationLog("查找所有的商品分类的树形结构")
    @GetMapping("/tree")
    @ResponseBody
    @ApiOperation(value = "查找所有的商品分类的树形结构", notes="查找所有的商品分类的树形结构")
    public ResultBean tree() {
        return ResultBean.success(categoryService.selectAllCategoryTree());
    }

	@OperationLog("新增商品分类")
    @PostMapping
    @ResponseBody
    @ApiOperation(value = "新增商品分类", notes="新增商品分类")
    @ApiImplicitParam(name = "category", value = "商品分类实体", required = false, dataType = "Category")
    public ResultBean add(Category category) {
		
        return ResultBean.success(categoryService.insert(category));
    }
	
	@OperationLog("删除商品分类")
    @DeleteMapping("/{fCateid}")
    @ResponseBody
    @ApiOperation(value = "删除商品分类", notes="删除商品分类")
    @ApiImplicitParam(name = "fCateid", value = "商品分类ID", required = true, dataType = "String")
    public ResultBean delete(@PathVariable("fCateid") String fCateid) {
		categoryService.delete(fCateid);
        return ResultBean.success();
    }
	

    @OperationLog("修改商品分类")
    @PutMapping
    @ResponseBody
    @ApiOperation(value = "修改商品分类", notes="修改商品分类")
    @ApiImplicitParam(name = "fCateid", value = "商品分类ID", required = true, dataType = "String")
    public ResultBean update(Category category) {
    	categoryService.update(category);
        return ResultBean.success();
    }
    
    @ApiIgnore
    @GetMapping("/{fCateid}")
    public String update(@PathVariable("fCateid") String fCateid, Model model) {
    	System.out.println("2222333"+fCateid);
    	Category category = categoryService.selectByFCateid(fCateid);
        model.addAttribute("category", category);
        return "category/category-add";
    }
    
    
	
	@OperationLog("小程序查找所有的商品分类的树形结构")
    @PostMapping("/wx/tree")
	@SecurityParameter(outEncode=true)
    @ResponseBody
    @ApiOperation(value = "小程序查找所有的商品分类的树形结构", notes="小程序查找所有的商品分类的树形结构")
    public ResultBean wxtree() {
        return ResultBean.success(categoryService.selectAllCategoryTree());
    }
	
	@OperationLog("根据父 ID 查询商品分类")
	@PostMapping("/wx/list")
    @ResponseBody
    @SecurityParameter(outEncode=true)
    @ApiOperation(value = "根据父 ID 查询商品分类", notes="根据父 ID 查询商品分类")
    @ApiImplicitParam(name = "fParentid", value = "父ID", required = false, dataType = "String")
    public ResultBean getWxList(@RequestParam String param) {
        Map<String,Object> paramMap=new HashMap<String, Object>();
		try {
            paramMap=AesEncryptUtil.transStringToMap(aesEncryptUtil.decrypt(param));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String id= (String) paramMap.get("fParentid");
		if(id.equals("")) {
			return ResultBean.error("id不能为空");
		}
        List<Category> categoryList = categoryService.selectByfparentid(id);
        return ResultBean.success(categoryList);
    }
	@OperationLog("调整商品分类排序")
    @PostMapping("/swap")
    @ResponseBody
    @ApiOperation(value = "调整商品分类排序", notes="调整商品分类排序")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "currentId", value = "选中商品分类ID", required = true, dataType = "String"),
    	@ApiImplicitParam(name = "swapId", value = "交换商品分类ID", required = true, dataType = "String")
    })
    public ResultBean swapSort(String currentId, String swapId) {
		categoryService.swapSort(currentId, swapId);
        return ResultBean.success();
    }

}
