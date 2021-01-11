package com.net.system.service.imple.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.net.common.util.ResultBean;
import com.net.system.mapper.product.ProductAttrValMapper;
import com.net.system.mapper.product.ProductSkuKeyMapper;
import com.net.system.mapper.product.ProductSkuValMapper;
import com.net.system.model.ProductAttrVal;
import com.net.system.model.ProductSku;
import com.net.system.model.ProductSkuVal;
import com.net.system.model.vo.ProductAttrKeyVo;
import com.net.system.model.vo.ProductAttrValVo;
import com.net.system.model.vo.ProductSkuKeyVo;
import com.net.system.model.vo.ProductSkuVo;
import com.net.system.service.product.ProductAttrValService;

@Service
public class ProductAttrValServiceImpl implements ProductAttrValService {

	@Resource
	private ProductSkuValMapper productSkuValMapper;

	@Resource
	private ProductSkuKeyMapper productSkuKeyMapper;

	@Resource
	private ProductAttrValMapper productAttrValMapper;

	@Override
	public int delete(ProductAttrVal productAttrVal) {
		return productAttrValMapper.delete(productAttrVal);
	}

	@Override
	public ResultBean insert(ProductAttrVal productAttrVal) {
		// TODO Auto-generated method stub
		
	
		Integer c = productAttrValMapper.checkType("" + productAttrVal.getAttrId());
		if (productAttrVal.getType()==3 && c>0) {
			return ResultBean.error("自定义图片类型已经添加过了");
		}
		
		return ResultBean.success(productAttrValMapper.insert(productAttrVal));
	}

	@Override
	public ResultBean update(ProductAttrVal productAttrVal) {
		// TODO Auto-generated method stub
		Integer c = productAttrValMapper.checkType("" + productAttrVal.getAttrId());
		if (productAttrVal.getType()==3 && c>0) {
			return ResultBean.error("自定义图片类型已经添加过了");
		}
		
		return ResultBean.success(productAttrValMapper.updateDynamic(productAttrVal));

	}

	@Override
	public ProductAttrVal selectById(Integer id) {
		ProductAttrVal productAttrVal = new ProductAttrVal();
		productAttrVal.setId(id);
		return productAttrValMapper.selectById(id);
	}

	@Override
	public List<ProductAttrValVo> findPageWithResult(Map<String, Object> map) {
		Integer page = Integer.parseInt(map.get("page").toString());
		Integer limit = Integer.parseInt(map.get("limit").toString());
		PageHelper.startPage(page, limit);
		return productAttrValMapper.findPageWithResult(map);
	}

	@Override
	public List<ProductAttrValVo> findByAttrId(Integer attrId) {
		List<ProductAttrValVo> list = productAttrValMapper.findByAttrId(attrId);
		for (ProductAttrValVo vo : list) {
			List<ProductAttrVal> valList = new ArrayList<ProductAttrVal>();
			if (vo.getType() == 1) {
				String[] strList = vo.getValue().split(",");
				for (String name : strList) {
					ProductAttrVal val = new ProductAttrVal();
					val.setName(name);
					val.setType(1);
					valList.add(val);
				}
			} else if (vo.getType() == 3) {

				String[] imgList = vo.getImg().split(",");
				String[] strList = vo.getValue().split(",");
				for (int i = 0; i < imgList.length; i++) {
					ProductAttrVal val = new ProductAttrVal();
					val.setImg(imgList[i]);
					val.setName(strList[i]);
					valList.add(val);
					val.setType(2);
				}

			}
			vo.setValueList(valList);
		}
		return list;
	}

}
