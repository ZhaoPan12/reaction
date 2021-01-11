package com.net.system.service.imple.product;

import static org.hamcrest.CoreMatchers.nullValue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.net.system.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.net.common.util.FileUtil;
import com.net.system.mapper.order.OrderProductMapper;
import com.net.system.mapper.product.FileMapper;
import com.net.system.mapper.product.LabelMapper;
import com.net.system.mapper.product.ProductMapper;
import com.net.system.mapper.product.ProductSkuMapper;
import com.net.system.model.Files;
import com.net.system.model.Label;
import com.net.system.model.Product;
import com.net.system.model.ProductSku;
import com.net.system.model.ProductSkuKey;
import com.net.system.model.ProductSkuVal;
import com.net.system.model.Tup;



import com.net.system.model.vo.ProductSkuInfoVo;
import com.net.system.model.vo.ProductSkuKeyVo;
import com.net.system.model.vo.ProductSkuVo;
import com.net.system.model.vo.ProeratorVo;
import com.net.system.service.product.ProductService;

import cn.hutool.core.lang.UUID;

@Service
public class ProductServiceImple implements ProductService {

	@Value("${photo.resourceLocations}")
	private String IMG_PATH_PREFIX;

	@Value("${photo.photoPath}")
	private String photoPath;



	@Resource
	private ProductMapper productMapper;

	@Resource
	private LabelMapper labelMapper;

	@Resource
	private FileMapper fileMapper;
	
	
	@Resource
	private OrderProductMapper orderProductMapper;
	
	@Resource
	private ProductSkuMapper productSkuMapper;


	@Override
	public int delete(String id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(Product product) {
		product.setCreatetime(new Date());
		if(product.getType().equals("COMMODITY")) {
			product.setStatus(0);
		}else {
			product.setStatus(1);
		}
		
		// TODO Auto-generated method stub
		return productMapper.insertDynamic(product);
	}

	@Override
	public int insertDynamic(Product product) {
		product.setCreatetime(new Date());
		// TODO Auto-generated method stub
		return productMapper.insertDynamic(product);
	}

	@Override
	public int updateDynamic(Product product) {
		// TODO Auto-generated method stub
		return productMapper.updateDynamic(product);
	}

	@Override
	@Transactional
	public int update(ProeratorVo proeratorVo) throws IllegalStateException, IOException {
		List<Files> list = new ArrayList<Files>();
		// 先将图片地址转成base64编码的图片
		Integer i = 0;
		for (String files : proeratorVo.getFiles()) {
			boolean a = files.startsWith("http");
			if (a) {
				Files imageFile = new Files();
				imageFile.setId(UUID.randomUUID().toString().replaceAll("-", ""));
				imageFile.setImageurl(files);
				imageFile.setProductid(proeratorVo.getId());
				imageFile.setSort(i);
				list.add(imageFile);
			} else {
				// 将base64编码的图片转成MultipartFile类型
				MultipartFile file = FileUtil.base64ToMultipart(files);
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
				Files imageFile = new Files();
				imageFile.setId(UUID.randomUUID().toString().replaceAll("-", ""));
				imageFile.setImageurl(photoPath + filenames);
				imageFile.setProductid(proeratorVo.getId());
				imageFile.setSort(i);
				list.add(imageFile);
				System.out.println(photoPath + filenames);
			}
			i++;
		}
		List<String> deleteStr = new ArrayList<String>();
		for (String string : proeratorVo.getFiles()) {
			boolean a = string.startsWith("http");
			if (!a) {
				deleteStr.add(string);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", deleteStr);
		map.put("productid", proeratorVo.getId());

		// 上传商品主图片
		if (proeratorVo.getMainFile() != null) {
			boolean a = proeratorVo.getMainFile().startsWith("http");
			String base = "";
			if (!a) {
				base = proeratorVo.getMainFile();
				MultipartFile files = FileUtil.base64ToMultipart(base);
				String mainUrl = this.uploadImage(files);
				if (mainUrl != null) {
					proeratorVo.setMainimage(mainUrl);
				}
			} else {
				proeratorVo.setMainimage(proeratorVo.getMainFile());
			}
		}
		Integer result = productMapper.updateDynamic(proeratorVo);

		if (result > 0) {
			List<Files> imageFile = fileMapper.findPageWithResult(map);
			for (Files flgure : imageFile) {
				String str2 = flgure.getImageurl().substring(photoPath.length(), flgure.getImageurl().length());
				FileUtil.delFile(IMG_PATH_PREFIX, str2);
				System.out.println("删除" + str2 + "成功！");
			}
			// 先删除图片以及标签
			fileMapper.deleteByProductId(proeratorVo.getId());
			labelMapper.delete(proeratorVo.getId());
			for (Files flgure : list) {
				fileMapper.insertDynamic(flgure);
			}
			String[] labels = proeratorVo.getLabelArray().split(",");
			Label label = new Label();
			label.setProductid(proeratorVo.getId());
			for (String string : labels) {
				if (!string.isEmpty()) {
					label.setId(UUID.randomUUID().toString().replace("-", ""));
					label.setLabel(string);
					labelMapper.insertDynamic(label);
				}

			}

		}
		return 0;
	}

	@Override
	public Product selectById(String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		Product product = productMapper.selectById(id);
		map.put("productid", id);
		product.setImageList(fileMapper.findPageWithResult(map));
		// TODO Auto-generated method stub
		return product;
	}

	@Override
	public List<Product> findPageWithResult(Map<String, Object> map) {
		String page = (String) map.get("page");
		String rows = (String) map.get("limit");
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		// TODO Auto-generated method stub
		return productMapper.findPageWithResult(map);
	}

	@Override
	public Integer findPageWithCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tup> findTupList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return labelMapper.findTupList(map);
	}

	@Override
	public int insertDynamic(Label label) {
		label.setId(UUID.randomUUID().toString().replace("-", ""));
		// TODO Auto-generated method stub
		return labelMapper.insertDynamic(label);
	}

	@Override
	public List<Label> selectLabelByProductId(String productId) {
		// TODO Auto-generated method stub
		return labelMapper.selectLabelByProductId(productId);
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

	@Override
	public List<Product> findProductCard(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return productMapper.findPageWithResult(map);
	}

	@Override
	public Product selectBychannelId(String channelId) {
		// TODO Auto-generated method stub
		return productMapper.selectBychannelId(channelId);
	}


	@Override
	public Map<String, Object> findProductSkuByid(String id) {
		// TODO Auto-generated method stub
		ProductSkuInfoVo vo = productMapper.findProductSkuByid(id);

		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> goods = new HashMap<String, Object>();
		Map<String, Object> sku = new HashMap<String, Object>();
		List<ProductSkuKeyVo> skuKeyList = vo.getSpecsList();
		List oList = new ArrayList<>();
		for (ProductSkuKeyVo keyVo : skuKeyList) {
			Map<String, Object> keyMap = new HashMap<String, Object>();
			keyMap.put("k", keyVo.getName());
			keyMap.put("k_s",String.valueOf(keyVo.getId()));
			List skuvalList = new ArrayList<>();
			for (ProductSkuVal valVo : keyVo.getValList()) {
				Map<String, Object> valMap = new HashMap<String, Object>();
				valMap.put("id", valVo.getId());
				valMap.put("name", valVo.getName());
				valMap.put("imgUrl", valVo.getImg());
				valMap.put("previewImgUrl", valVo.getImg());
				skuvalList.add(valMap);
			}
			keyMap.put("v", skuvalList);
			oList.add(keyMap);
		}
		//
		List<ProductSkuKey> screenKey = screenKey(vo.getSpecsList());
		List<ProductSkuKey> screenVal = screenVal(vo.getSpecsList());

		List list = new ArrayList<>();
		for (ProductSku skuVo : vo.getSkuList()) {
			list.add(screenSku(skuVo, screenKey, screenVal));
		}
		sku.put("tree", oList);
		sku.put("list", list);
		sku.put("price",vo.getPrice());
		sku.put("stock_num",vo.getStock());
		if(vo.getSkuList().size()>0) {
			sku.put("collection_id",vo.getSkuList().get(0).getSkuId());
		}
		
		sku.put("hide_stock",false);
		goods.put("picture", vo.getMainimage());
		data.put("goods",goods);
		data.put("goodsId", vo.getId());
		data.put("quota",0);
		data.put("quotaUsed",0);
		data.put("fileList",vo.getFileList());
		data.put("subtitle", vo.getSubtitle());
		data.put("description", vo.getDescription());
		data.put("name", vo.getProductname());
		data.put("title", vo.getTitle());
		data.put("minimumPrice", vo.getPrice());
		data.put("salesVolume",orderProductMapper.countSalesVolume(vo.getId()));
		data.put("sku", sku);
		return data;
	}

	public static Map<String, Object> screenSku(ProductSku skuVo, List<ProductSkuKey> screenKey,
			List<ProductSkuKey> screenVal) {
		Map<String, Object> map = new HashMap<String, Object>();
		String[] kStr = skuVo.getCustomTitle().split(",");
		String[] vStr = skuVo.getCustomValue().split(",");

		for (int i = 0; i < kStr.length; i++) {
			map.put(findId(kStr[i], screenKey), findId(vStr[i], screenVal));
		}
		map.put("price", skuVo.getPrice().doubleValue()*100);
		map.put("id", ""+skuVo.getSkuId());
		map.put("stock_num", skuVo.getStock());
		return map;
	}

	public static String findId(String name, List<ProductSkuKey> screenList) {

		for (ProductSkuKey s : screenList) {
			if (name.equals(s.getName())) {
				return s.getId().toString();
			}
		}
		return "";
	}

	public static List<ProductSkuKey> screenKey(List<ProductSkuKeyVo> skuKeyList) {
		List<ProductSkuKey> list = new ArrayList<ProductSkuKey>();
		for (ProductSkuKeyVo keyVo : skuKeyList) {
			ProductSkuKey key = new ProductSkuKey();
			key.setId(keyVo.getId());
			key.setName(keyVo.getName());
			list.add(key);
		}
		return list;
	}

	public static List<ProductSkuKey> screenVal(List<ProductSkuKeyVo> skuKeyList) {
		List<ProductSkuKey> list = new ArrayList<ProductSkuKey>();
		for (ProductSkuKeyVo keyVo : skuKeyList) {
			for (ProductSkuVal valVo : keyVo.getValList()) {
				ProductSkuKey key = new ProductSkuKey();
				key.setId(valVo.getId());
				key.setName(valVo.getName());
				list.add(key);
			}
		}
		return list;
	}


	@Override
	public List<Product> selectByName(Map<String, Object> map) {

		String page=map.get("page").toString();
		String rows= map.get("limit").toString();
		PageHelper.startPage(Integer.parseInt(page), Integer.parseInt(rows));
		return productMapper.selectByName(map);
	}

	@Override
	public List<String> selectByProductName(String productName) {
		return productMapper.selectProductListByProductName(productName);
	}

	@Override
	public Product selectByRightDtlCode(String rightDtlCode) {
		// TODO Auto-generated method stub
		return productMapper.selectByRightDtlCode(rightDtlCode);
	}

	@Override
	public ProductSku findSkuById(String id) {
		// TODO Auto-generated method stub
		return productSkuMapper.selectBySkuId(id);
	}

}
