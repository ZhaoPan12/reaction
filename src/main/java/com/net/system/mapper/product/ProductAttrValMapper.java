package com.net.system.mapper.product;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.ProductAttrVal;
import com.net.system.model.vo.ProductAttrValVo;

@Mapper
public interface ProductAttrValMapper {
	
    int delete(ProductAttrVal productAttrVal);

    int insert(ProductAttrVal productAttrVal);

    int insertDynamic(ProductAttrVal productAttrVal);

    int updateDynamic(ProductAttrVal productAttrVal);

    int update(ProductAttrVal productAttrVal);

    ProductAttrVal selectById(Integer id);

    List<ProductAttrValVo> findPageWithResult(Map<String,Object> map);
    
    List<ProductAttrValVo> findByAttrId(Integer attrId);
    
    Integer checkType(String id);

   
}