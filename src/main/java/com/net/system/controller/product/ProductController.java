package com.net.system.controller.product;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageInfo;
import com.net.common.annotation.OperationLog;
import com.net.common.util.FileUtil;
import com.net.common.util.PageResultBean;
import com.net.common.util.ResultBean;
import com.net.system.model.Category;
import com.net.system.model.Files;
import com.net.system.model.Flgure;
import com.net.system.model.Label;
import com.net.system.model.Product;
import com.net.system.model.ProductSku;
import com.net.system.model.ProductSkuKey;
import com.net.system.model.Tup;
import com.net.system.model.vo.ProductAttrKeyVo;
import com.net.system.model.vo.ProductSkuKeyVo;
import com.net.system.model.vo.ProeratorVo;
import com.net.system.service.product.CategoryService;
import com.net.system.service.product.FileService;
import com.net.system.service.product.ProductAttrKeyService;
import com.net.system.service.product.ProductService;
import com.net.system.service.product.ProductSkuService;

import cn.hutool.json.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/product")
@Api(tags = "ProductController", description = "产品管理类")
public class ProductController {
	
	@Value("${photo.resourceLocations}")
	private   String IMG_PATH_PREFIX;
	
	@Value("${photo.photoPath}")
	private   String photoPath;
	
    @Resource
    private ProductService productService;
    
    @Resource
    private FileService fileService;
    
    @Resource
    private CategoryService categoryService;
    
    @Resource
	private ProductAttrKeyService productAttrKeyServie;
    
    @Resource
   	private ProductSkuService productSkuServie;

    @GetMapping("/specsSet/{productId}")
    @ApiIgnore
    public String specsSet(@PathVariable("productId") String productId,Model model) {
    	Map<String,Object> map=new HashMap<String,Object>();
    	ProductSkuKey productSkuKey =new ProductSkuKey();
    	productSkuKey.setProductId(productId);
    	map.put("productId",productId);
    	List<ProductSku> skuList=productSkuServie.findSkuByProductId(map);
    	if(skuList.size()==0) {
    		List<ProductSkuKeyVo> keyVoList=productSkuServie.findSpecsByProductId(productSkuKey);
        	List<ProductAttrKeyVo> list=productAttrKeyServie.findAttrKeyAll();
        	model.addAttribute("productId", productId);
    		model.addAttribute("attrKeyList", list);
    		model.addAttribute("product", productService.selectById(productId));
    		model.addAttribute("specsList", keyVoList);
    		model.addAttribute("skuList", skuList);
    		 return "product/specs-set";
    	}else {
    		model.addAttribute("product", productService.selectById(productId));
    		model.addAttribute("productId", productId);
    		model.addAttribute("skuList", skuList);
    		 return "product/specs-list";
    	}
    	
       
    }
    
    
    @GetMapping("/index")
    @ApiIgnore
    public String index(Model model) {
    	Map<String,Object> map=new HashMap<String, Object>();
    	map.put("label", "PRODUCTTYPE");
    	List<Tup> list=productService.findTupList(map);
    	model.addAttribute("tupList", list);
        return "product/product-list";
    }
    
    @GetMapping("/add")
    @ApiIgnore
    public String add(Model model) {
    	Map<String,Object> map=new HashMap<String, Object>();
    	map.put("label", "PRODUCTTYPE");
    	List<Tup> list=productService.findTupList(map);
    	model.addAttribute("tupList", list);
    	map.put("type", "CARD");
    	model.addAttribute("list", productService.findProductCard(map));
        return "product/product-add";
    }
    
    @GetMapping("/labelAdd")
    @ApiIgnore
    public String labelAdd() {
        return "label/label-add";
    }
    
    @GetMapping("/edit/{productId}")
    @ApiIgnore
    public String update(@PathVariable("productId") String productId, Model model) {
    	Map<String,Object> map=new HashMap<String, Object>();
    	map.put("label", "PRODUCTTYPE");
    	List<Tup> list=productService.findTupList(map);
    	model.addAttribute("tupList", list);
    	model.addAttribute("product", productService.selectById(productId));
    	model.addAttribute("labelList", productService.selectLabelByProductId(productId));
    	map.put("type", "CARD");
    	model.addAttribute("list", productService.findProductCard(map));
        return "product/product-add";
    }
    
    @OperationLog("查询产品列表")
    @PostMapping("/findPageWithResult")
    @ResponseBody
    @ApiOperation(value = "查询产品列表", notes="查询产品列表")
    @ApiImplicitParam(name = "map", value = "参数实体",  required = true, dataType = "map")
    public PageResultBean<Product> findPageWithResult(HttpServletRequest request,@RequestParam Map<String,Object> map) {
    	List<Product> list= productService.findPageWithResult(map);
    	PageInfo<Product> productPageInfo = new PageInfo<>(list);
    	return new PageResultBean<>(productPageInfo.getTotal(), productPageInfo.getList());
    }
    
    
    @OperationLog("新增商品")
    @GetMapping("/insert")
    @ResponseBody
    @ApiOperation(value = "新增商品", notes="新增商品")
    @ApiImplicitParam(name = "fProid", value = "商品id", required = false, dataType = "String")
    public ResultBean insert(ProeratorVo proeratorVo) {
        Integer result = productService.insert(proeratorVo);
        return ResultBean.success(result);
    }
    
    @PostMapping(value="/upload/images")
    @ResponseBody
    public JSONObject uploadimages( MultipartFile[] file) throws IOException {
        JSONObject res = new JSONObject();
        List<String> imageurls=new ArrayList<>();
        String savePath = IMG_PATH_PREFIX; // 上传后的路径
        	for (MultipartFile files : file) {
	        	String filename = UUID.randomUUID().toString().replaceAll("-", "");
	            String ext = files.getOriginalFilename().substring(files.getOriginalFilename().lastIndexOf("."));
	            String filenames = filename + ext;
	            String tagFilePath = savePath + filenames;// 单文件
	            File dest = new File(tagFilePath);
	            if (!dest.getParentFile().exists()) {
	             dest.getParentFile().mkdirs();
	            }
	            // 执行流写入
	            files.transferTo(dest);
	            imageurls.add(photoPath+filenames);
			}
        res.put("data", imageurls);
        res.put("code", 0);
        res.put("msg", "上传成功");
        System.out.println(res);
        return res;
    }
    
    @PostMapping(value="/relgoods/images")
    @ResponseBody
    public Map relgoodsimages( ProeratorVo proeratorVo) throws IOException {
    	Map<String,Object> map=new HashMap<String, Object>();
        List<String> imageurls=new ArrayList<>();
        List<Files> list=new ArrayList<Files>();
        proeratorVo.setId(UUID.randomUUID().toString().replace("-", ""));
        //上传商品主图片
    	if(proeratorVo.getMainFile()!=null) {
    		MultipartFile files=FileUtil.base64ToMultipart(proeratorVo.getMainFile());
    		String mainUrl=uploadImage(files);
        	if(mainUrl!=null) {
        		proeratorVo.setMainimage(mainUrl);
        	}
    	}
    	
    	//将标签添加进数据表
    	String[] labelArr=proeratorVo.getLabelArray().split(",");
    	Integer result = productService.insert(proeratorVo);
    	Label label=new Label();
    	label.setProductid(proeratorVo.getId());
    	for (String string : labelArr) {
    		if(!string.isEmpty()) {
    			label.setLabel(string);
        		productService.insertDynamic(label);
    		}
		}
        if(proeratorVo.getFiles()!=null) {
        	//将页面显示轮播图片上传
        	Integer i=0;
        	for (String string : proeratorVo.getFiles()) {
            	MultipartFile files=FileUtil.base64ToMultipart(string);
                String filename = UUID.randomUUID().toString().replaceAll("-", "");
	            String ext = files.getOriginalFilename().substring(files.getOriginalFilename().lastIndexOf("."));
	            String filenames = filename + ext;
	            String tagFilePath = IMG_PATH_PREFIX + filenames;// 单文件
	            File dest = new File(tagFilePath);
	            if (!dest.getParentFile().exists()) {
	             dest.getParentFile().mkdirs();
	            }
	            // 执行流写入
	            files.transferTo(dest);
	            Files flgure=new Files();
                flgure.setId(UUID.randomUUID().toString().replaceAll("-",""));
                flgure.setCreatetime(new Date());
                flgure.setImagename(filenames);
                flgure.setImageurl(photoPath+filenames);
                flgure.setProductid(proeratorVo.getId());
                flgure.setSort(i);
                i++;
                list.add(flgure);
    		}
        }
        if(result>0) {
        	for (Files file : list) {
        		fileService.insertDynamic(file);
			}
        }
        map.put("code", 0);
        map.put("msg", "新增成功");
        map.put("data", imageurls);
        return map;
    }
    
    
    @PostMapping(value="/updateProduct")
    @ResponseBody
    public ResultBean updateProduct(ProeratorVo proeratorVo) throws IOException {
        return ResultBean.success(productService.update(proeratorVo));
    }
    
    @PostMapping(value="/updateWeights")
    @ResponseBody
    public ResultBean updateWeights(Product product) throws IOException {
    	
        return ResultBean.success(productService.updateDynamic(product));
    }
    
    @OperationLog("下架商品")
    @PostMapping("/{id}/disable")
    @ResponseBody
    @ApiOperation(value="下架商品", notes="下架商品")
    @ApiImplicitParam(name = "id", value = "商品ID", required = true, dataType = "String")
    public ResultBean disable(@PathVariable("id") String id) {
    	Product product=new Product();
    	product.setId(id);
    	product.setStatus(0);
    	product.setUpdatetime(new Date());
        return ResultBean.success(productService.updateDynamic(product));
    }

    @OperationLog("上架商品")
    @PostMapping("/{id}/enable")
    @ResponseBody
    @ApiOperation(value="上架商品", notes="上架商品")
    @ApiImplicitParam(name = "id", value = "商品ID", required = true, dataType = "String")
    public ResultBean enable(@PathVariable("id") String id) {
    	Product product=new Product();
    	product.setId(id);
    	product.setStatus(1);
    	product.setUpdatetime(new Date());
        return ResultBean.success(productService.updateDynamic(product));
    }
    
    @OperationLog("根据id删除商品")
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "根据id删除商品", notes="根据id删除商品")
    @ApiImplicitParam(name = "id", value = "商品id", required = false, dataType = "String")
    public ResultBean delete(@PathVariable("id") String id) {
    	Product product=new Product();
    	product.setId(id);
    	product.setStatus(2);
        return ResultBean.success(productService.updateDynamic(product));
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
		return photoPath+filenames;
    }
    
    @OperationLog("查询产品信息")
    @PostMapping("/wx/findProductById")
    @ResponseBody
    @ApiOperation(value = "查询产品信息", notes="查询产品信息")
    @ApiImplicitParam(name = "productId", value = "产品id",  required = true, dataType = "String")
    public ResultBean findProductById(HttpServletRequest request,@RequestParam String productId) {
    	return ResultBean.success(productService.selectById(productId));
    }

    
    
    @OperationLog("查询商城产品信息")
    @PostMapping("/wx/findProductSkuById")
    @ResponseBody
    public ResultBean findProductSkuById(HttpServletRequest request,@RequestBody Map<String,Object> map) {
    	return ResultBean.success(productService.findProductSkuByid(map.get("id").toString()));
    }
    
    @OperationLog("查询sku信息")
    @PostMapping("/wx/findSkuById")
    @ResponseBody
    public ResultBean findSkuById(HttpServletRequest request,@RequestBody Map<String,Object> map) {
    	return ResultBean.success(productService.findSkuById(map.get("skuId").toString()));
    }



    @OperationLog("根据输入的产品名模糊查询产品列表")
    @PostMapping("/wx/findProductByName")
    @ResponseBody
    public PageResultBean findProductNameListByName(@RequestBody Map<String,Object> map){
        List<Product> list=  productService.selectByName(map);
        PageInfo<Product> productPageInfo = new PageInfo<>(list);
        return new PageResultBean<>(productPageInfo.getTotal(), productPageInfo.getList());
    }

	/* @OperationLog("根据产品名查询产品列表")
	 @PostMapping("/")
	 @ResponseBody
     @ApiOperation(value = "根据产品名查询产品列表", notes="根据产品名查询产品列表")
     @ApiImplicitParam(name = "productName", value = "产品名称", required = true, dataType = "String")
     public List<String> findPListByProductName(){
	  return productService.selectByProductName();
     }*/



}
