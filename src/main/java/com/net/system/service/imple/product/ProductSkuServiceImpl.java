package com.net.system.service.imple.product;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.net.system.mapper.product.ProductMapper;
import com.net.system.mapper.product.ProductSkuKeyMapper;
import com.net.system.mapper.product.ProductSkuMapper;
import com.net.system.mapper.product.ProductSkuValMapper;
import com.net.system.model.Product;
import com.net.system.model.ProductSku;
import com.net.system.model.ProductSkuKey;
import com.net.system.model.ProductSkuVal;
import com.net.system.model.vo.ProductSkuKeyVo;
import com.net.system.model.vo.ProductSkuVo;
import com.net.system.service.product.ProductSkuService;

@Service
public class ProductSkuServiceImpl implements ProductSkuService {

	@Resource
	private ProductSkuValMapper productSkuValMapper;

	@Resource
	private ProductSkuKeyMapper productSkuKeyMapper;

	@Resource
	private ProductSkuMapper productSkuMapper;

	@Resource
	private ProductMapper productMapper;

	@Override
	public int setSpecs(ProductSkuVo skuVo) {

		// 删除之前的商品规格重新添加设置
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("productId", skuVo.getProductId());
		List<ProductSkuKey> list = productSkuKeyMapper.findPageWithResult(map);
		for (ProductSkuKey key : list) {
			productSkuValMapper.deleteByKeyId(key.getId());
			productSkuKeyMapper.delete(key.getId());
		}

		productSkuMapper.deleteByProductId(skuVo.getProductId());

		// 商品规格添加
		List<ProductSkuKeyVo> specsList = skuVo.getSpecsList();

		// 添加一级
		for (ProductSkuKeyVo keyVo : specsList) {
			keyVo.setProductId(skuVo.getProductId());
			productSkuKeyMapper.insert(keyVo);

			// 添加二级
			List<ProductSkuVal> valList = keyVo.getValList();
			for (ProductSkuVal val : valList) {
				val.setAttrKeyId(keyVo.getId());
				productSkuValMapper.insert(val);
			}
		}

		// 添加SKU商品
		List<ProductSku> skuList = skuVo.getSkuList();
		for (ProductSku skuProduct : skuList) {
			skuProduct.setProductId(skuVo.getProductId());
			map = new HashMap<String, Object>();
			map.put("productId", skuVo.getProductId());
			map.put("ids", skuProduct.getCustomValue().split(","));
			skuProduct.setDescribe(splicing(skuProduct.getCustomTitle(), skuProduct.getCustomValue()));
			skuProduct.setSkuId(productSkuValMapper.finSkuId(map));
			productSkuMapper.insertDynamic(skuProduct);
		}

		// 修改商品
		Product pro = new Product();
		pro.setId(skuVo.getProductId());
		pro.setStock(skuVo.getProStock());
		pro.setStatus(skuVo.getStatus());
		productMapper.updateDynamic(pro);
		return 1;
	}

	@Override
	public List<ProductSkuKeyVo> findSpecsByProductId(ProductSkuKey productSkuKey) {
		// TODO Auto-generated method stub
		return productSkuKeyMapper.findSpecsByProductId(productSkuKey);
	}

	@Override
	public List<ProductSku> findSkuByProductId(Map<String, Object> map) {

		return productSkuMapper.findPageWithResult(map);
	}

	public String splicing(String title, String val) {
		String[] titleArr = title.split(",");
		String[] valArr = val.split(",");
		String str = "";

		for (int i = 0; i < titleArr.length; i++) {
			str += titleArr[i] + ":" + valArr[i] + ";";
		}
		return str;
	}

	@Override
	public int update(ProductSkuVo skuVo) {
		// 修改SKU
		List<ProductSku> skuList = skuVo.getSkuList();
		for (ProductSku skuProduct : skuList) {
			productSkuMapper.updateDynamic(skuProduct);
		}
		// 修改商品
		Product pro = new Product();
		pro.setId(skuVo.getProductId());
		pro.setStock(skuVo.getProStock());
		pro.setStatus(skuVo.getStatus());
		productMapper.updateDynamic(pro);
		return 0;
	}

}
