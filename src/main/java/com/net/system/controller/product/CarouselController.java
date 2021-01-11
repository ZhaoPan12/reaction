package com.net.system.controller.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.github.pagehelper.PageInfo;
import com.net.common.annotation.OperationLog;
import com.net.common.interceptor.SecurityParameter;
import com.net.common.util.AesEncryptUtil;
import com.net.common.util.PageResultBean;
import com.net.common.util.ResultBean;
import com.net.system.controller.order.OrderController;
import com.net.system.model.Carousel;
import com.net.system.model.Category;
import com.net.system.model.Product;
import com.net.system.model.vo.ProeratorVo;
import com.net.system.service.product.CarouselService;
import com.net.system.service.product.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequestMapping("/carousel")
@Api(tags = "CarouselController", description = "首页轮播管理类")
public class CarouselController {
	
	@Resource
	private CarouselService carouselService;

	@Resource
	private ProductService productService;
	
	private AESUtil aESUtil = new AESUtil();

	private Logger logger = LoggerFactory.getLogger(OrderController.class);

	private AesEncryptUtil aesEncryptUtil = AesEncryptUtil.getAesEncryptUtil();
	
	@GetMapping("/list")
    @ApiIgnore
    public String index(Model model) {
        return "carousel/carousel-list";
    }

	@OperationLog("新增首页轮播图")
    @PostMapping("/insert")
    @ResponseBody
    @ApiOperation(value = "新增首页轮播图", notes="新增首页轮播图")
    public ResultBean insert(Carousel carousel) {
        Integer result = carouselService.insert(carousel);
        return ResultBean.success(result);
    }

	@OperationLog("根据id删除轮播图")
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation(value = "根据id删除轮播图", notes="根据id删除轮播图")
    @ApiImplicitParam(name = "id", value = "轮播图id", required = false, dataType = "String")
    public ResultBean delete(@PathVariable("id") String id) {
        return ResultBean.success(carouselService.delete(id));
    }
	
	 @OperationLog("设置轮播图为不显示")
	 @PostMapping("/{id}/disable")
	 @ResponseBody
	 @ApiOperation(value="设置轮播图为不显示", notes="设置轮播图为不显示")
	 @ApiImplicitParam(name = "id", value = "商品ID", required = true, dataType = "String")
	 public ResultBean disable(@PathVariable("id") String id) {
		 	Carousel carousel=new Carousel();
		 	carousel.setId(id);
		 	carousel.setIsShow(0);
	        return ResultBean.success(carouselService.updateDynamic(carousel));
	 }

	 @OperationLog("设置轮播图为显示")
	 @PostMapping("/{id}/enable")
	 @ResponseBody
	 @ApiOperation(value="设置轮播图为显示", notes="设置轮播图为显示")
	 @ApiImplicitParam(name = "id", value = "轮播图ID", required = true, dataType = "String")
	 public ResultBean enable(@PathVariable("id") String id) {
		 	Carousel carousel=new Carousel();
		 	carousel.setId(id);
		 	carousel.setIsShow(1);
	        return ResultBean.success(carouselService.updateDynamic(carousel));
	}
	 
    @OperationLog("查询轮播图列表")
    @GetMapping("/findPageWithResult")
    @ResponseBody
    @ApiOperation(value = "查询轮播图列表", notes="查询轮播图列表")
    @ApiImplicitParam(name = "map", value = "参数实体",  required = true, dataType = "map")
    public PageResultBean<Carousel> findPageWithResult(HttpServletRequest request,@RequestParam Map<String,Object> map) {
    	List<Carousel> list= carouselService.findPageWithResult(map);
    	PageInfo<Carousel> carouselPageInfo = new PageInfo<>(list);
    	return new PageResultBean<>(carouselPageInfo.getTotal(), carouselPageInfo.getList());
    }
    
    @GetMapping
    @ApiIgnore
    public String add(Model model) {
    	Map<String,Object> map=new HashMap<String, Object>();
    	//查询商品的所有id和名字
    	//map.put("fStatus", "1");
//    	List<Product> product=productService.findWithResult(map);
//    	model.addAttribute("product", product);
        return "carousel/carousel-add";
    }
    
    @GetMapping("/edit/{id}")
    @ApiIgnore
    public String update(@PathVariable("id") String id, Model model) {
    	Map<String,Object> map=new HashMap<String, Object>();
    	map.put("fStatus", "1");
    	System.out.println(id);
    	Carousel carousel=carouselService.selectById(id);
    	model.addAttribute("carousel", carousel);
//    	List<Product> product=productService.findWithResult(map);
//    	model.addAttribute("product", product);
        return "carousel/carousel-add";
    }
    
    @OperationLog("修改轮播图")
    @PutMapping
    @ResponseBody
    @ApiOperation(value = "修改轮播图", notes="修改轮播图")
    @ApiImplicitParam(name = "carousel", value = "实体", required = true, dataType = "Carousel")
    public ResultBean update(Carousel carousel) {
    	carouselService.update(carousel);
        return ResultBean.success();
    }
    
    @OperationLog("小程序查询首页轮播图")
    @PostMapping("/wx/findCarouselResult")
    @ResponseBody
    @SecurityParameter(outEncode=true,inDecode=false)
    @ApiOperation(value = "小程序查询首页轮播图", notes="小程序查询首页轮播图")
    @ApiImplicitParam(name = "map", value = "参数实体",  required = true, dataType = "map")
    public ResultBean findCarouselResult(HttpServletRequest request,@RequestParam String param) {
    	Map<String, Object> map=new HashMap<String, Object>();
		try {
			map = AesEncryptUtil.transStringToMap(aesEncryptUtil.decrypt(param));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("isShow",1);
    	List<Carousel> list= carouselService.findPageWithResult(map);
        return ResultBean.success(list);
    }
	
}
